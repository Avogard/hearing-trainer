#!/usr/bin/env python3
"""
Renders the app's piano samples: one WAV per MIDI note, res/raw/piano_<midi>.wav.

Why synthesized, not recorded: see docs/DECISIONS.md ("Synthesized piano samples instead of
real recordings" and "Per-note rendered samples + AudioTrack mixer"). The design goal is a
clear, soft, quickly-decaying piano tone for pitch recognition -- not a concert grand.

The tone is a physically-informed additive model. Per note:
  * partials at f_k = k * f0 * sqrt(1 + B k^2) (string stiffness => slight inharmonicity),
  * amplitudes shaped by the hammer strike point (a comb that notches every ~8th partial)
    and a soft-hammer low-pass (how hard the key is hit sets the brightness),
  * a two-stage decay per partial (fast "prompt sound", slower "aftersound" -- the piano's
    signature envelope), with higher partials dying faster,
  * 2-3 slightly detuned unison strings per note (the gentle shimmer of a real piano),
  * a short soundboard "knock" on the attack and a small amount of body/room reflections.

Usage:
    python3 tools/render_piano_samples.py            # writes app/src/main/res/raw/piano_*.wav
    python3 tools/render_piano_samples.py --demo DIR # also writes audition clips into DIR
    python3 tools/render_piano_samples.py --preset bright

Requires numpy only. Re-run after changing any constant below; the app just plays the files.
"""

from __future__ import annotations

import argparse
import os
import struct
import sys
import wave

import numpy as np

SAMPLE_RATE = 48_000
LOWEST_NOTE = 48   # C3
HIGHEST_NOTE = 84  # C6
MAX_SECONDS = 2.0  # hard cap on a sample's length; tails below the noise floor are trimmed
TRIM_DB = -72.0    # trim the tail once its peak stays below this

# ---------------------------------------------------------------------------------------------
# Tone presets. "soft" is the app's sound; the others exist so a different taste is one flag away.
# ---------------------------------------------------------------------------------------------
PRESETS = {
    # Clear, soft, short. Hammer low-pass ~3 kHz at middle C; C4 is 40 dB down within 0.6 s.
    "soft": dict(hammer_cutoff_hz=3000.0, hammer_order=2.0, partial_rolloff=0.5, t60_c4=1.4,
                 room_db=-21.0, knock_db=-26.0, detune_cents=1.4),
    # Same instrument hit harder: more upper partials, slightly longer ring.
    "bright": dict(hammer_cutoff_hz=5000.0, hammer_order=1.8, partial_rolloff=0.2, t60_c4=1.6,
                   room_db=-23.0, knock_db=-24.0, detune_cents=1.6),
    # Bone dry and even shorter, for people who find any room sound muddy.
    "dry": dict(hammer_cutoff_hz=3000.0, hammer_order=2.0, partial_rolloff=0.5, t60_c4=1.1,
                room_db=-60.0, knock_db=-30.0, detune_cents=1.2),
}


def midi_to_hz(midi: int) -> float:
    return 440.0 * 2.0 ** ((midi - 69) / 12.0)


def db(x: float) -> float:
    return 10.0 ** (x / 20.0)


def render_note(midi: int, p: dict, rng: np.random.Generator) -> np.ndarray:
    f0 = midi_to_hz(midi)
    rel = f0 / midi_to_hz(60)  # 1.0 at middle C

    # --- how long, how fast it dies -----------------------------------------------------------
    # Fundamental decay: longer in the bass, shorter in the treble, like a real piano.
    t60_fund = p["t60_c4"] * rel ** (-0.35)
    sigma0 = 6.91 / t60_fund  # 6.91 = ln(1000): amplitude factor for 60 dB
    n = int(min(MAX_SECONDS, t60_fund * 1.25 + 0.35) * SAMPLE_RATE)
    t = np.arange(n) / SAMPLE_RATE

    # --- partials -----------------------------------------------------------------------------
    inharmonicity = 0.00028 * rel ** 1.1
    k = np.arange(1, 200)
    f_k = k * f0 * np.sqrt(1.0 + inharmonicity * k ** 2)
    f_k = f_k[f_k < 9000.0]
    k = k[: len(f_k)]

    strike_point = 0.12 if midi < 72 else 0.09          # fraction of string length
    comb = np.abs(np.sin(np.pi * k * strike_point))       # partials with a node at the hammer vanish
    comb = np.maximum(comb, 0.06)                         # ...not quite, in practice
    cutoff = p["hammer_cutoff_hz"] * rel ** 0.5           # treble hammers are harder/brighter
    hammer_lp = (1.0 + (f_k / cutoff) ** 2) ** (-p["hammer_order"] / 2.0)
    amp = comb * hammer_lp / k ** p["partial_rolloff"]
    amp[0] = max(amp[0], 0.55)                            # keep the fundamental firmly present

    # Higher partials decay faster: mostly by partial index, plus a little air absorption.
    sigma_slow = sigma0 * (1.0 + 0.25 * (k - 1) + 0.02 * (k - 1) ** 2) + 0.0005 * f_k
    sigma_fast = 3.0 * sigma_slow

    # --- unison strings -----------------------------------------------------------------------
    strings = 2 if midi < 55 else 3
    detune = p["detune_cents"] * (1.0 if midi < 72 else 0.75)
    offsets_cents = [0.0, detune, -0.7 * detune][:strings]
    decay_scale = [1.0, 0.9, 1.12][:strings]

    out = np.zeros(n)
    for cents, dscale in zip(offsets_cents, decay_scale):
        ratio = 2.0 ** (cents / 1200.0)
        phase = rng.uniform(-0.3, 0.3, size=len(k))     # near-coherent onset, not perfectly aligned
        env = (0.5 * np.exp(-np.outer(t, sigma_fast * dscale))
               + 0.5 * np.exp(-np.outer(t, sigma_slow * dscale)))
        out += (env * amp * np.sin(2.0 * np.pi * np.outer(t, f_k * ratio) + phase)).sum(axis=1)
    out /= strings

    # --- attack ----------------------------------------------------------------------------
    attack_s = float(np.clip(0.004 * rel ** (-0.3), 0.002, 0.008))
    a = int(attack_s * SAMPLE_RATE)
    out[:a] *= 0.5 - 0.5 * np.cos(np.pi * np.arange(a) / a)

    # Soundboard knock: three damped low resonances, very short. Bandlimited on purpose --
    # a noise burst here is what makes synthesized pianos sound like a click.
    knock = np.zeros(n)
    kt = t[: int(0.12 * SAMPLE_RATE)]
    for hz, level in ((95.0, 1.0), (165.0, 0.7), (270.0, 0.45)):
        knock[: len(kt)] += level * np.exp(-kt * 6.91 / 0.05) * np.sin(2 * np.pi * hz * kt)
    knock[:a] *= 0.5 - 0.5 * np.cos(np.pi * np.arange(a) / a)
    out += db(p["knock_db"]) * knock / np.abs(knock).max() * np.abs(out).max()

    # --- body / room -------------------------------------------------------------------------
    if p["room_db"] > -50:
        out = add_room(out, db(p["room_db"]), rng)

    # --- level, trim, fade -------------------------------------------------------------------
    out /= np.abs(out).max()
    tilt = db((60 - midi) * 0.12)                          # bass a touch louder, treble a touch softer
    out *= min(0.85 * tilt, 0.95)
    out = trim_tail(out)
    fade = min(int(0.02 * SAMPLE_RATE), len(out))
    out[-fade:] *= np.linspace(1.0, 0.0, fade)
    return out


def add_room(x: np.ndarray, wet: float, rng: np.random.Generator) -> np.ndarray:
    """A few hundred ms of soft early reflections: enough to stop the tone sounding
    'inside your head' on headphones, little enough to keep it clear."""
    ir_len = int(0.25 * SAMPLE_RATE)
    it = np.arange(ir_len) / SAMPLE_RATE
    ir = rng.standard_normal(ir_len) * np.exp(-it * 6.91 / 0.22)
    ir[: int(0.005 * SAMPLE_RATE)] = 0.0                   # 5 ms pre-delay keeps the attack crisp
    ir = one_pole_lowpass(ir, 3000.0)
    ir = ir - one_pole_lowpass(ir, 200.0)                  # no low-end mud
    ir *= wet / np.sqrt(np.sum(ir ** 2))
    n = len(x) + ir_len - 1
    y = np.fft.irfft(np.fft.rfft(x, n) * np.fft.rfft(ir, n), n)[: len(x)]
    return x + y


def one_pole_lowpass(x: np.ndarray, cutoff_hz: float) -> np.ndarray:
    a = np.exp(-2.0 * np.pi * cutoff_hz / SAMPLE_RATE)
    y = np.empty_like(x)
    acc = 0.0
    for i, v in enumerate(x):
        acc = (1.0 - a) * v + a * acc
        y[i] = acc
    return y


def trim_tail(x: np.ndarray) -> np.ndarray:
    win = int(0.01 * SAMPLE_RATE)
    peaks = np.abs(x[: len(x) // win * win]).reshape(-1, win).max(axis=1)
    above = np.nonzero(peaks > db(TRIM_DB))[0]
    if len(above) == 0:
        return x
    end = min(len(x), (above[-1] + 2) * win)
    return x[:end]


def write_wav(path: str, x: np.ndarray) -> None:
    # TPDF dither before truncating to 16-bit so the quiet tail doesn't turn grainy.
    dither = (np.random.default_rng(0).uniform(-1, 1, len(x)) + np.random.default_rng(1).uniform(-1, 1, len(x))) / 65536.0
    pcm = np.clip(np.round((x + dither) * 32767.0), -32768, 32767).astype("<i2")
    with wave.open(path, "wb") as w:
        w.setnchannels(1)
        w.setsampwidth(2)
        w.setframerate(SAMPLE_RATE)
        w.writeframes(pcm.tobytes())


# ---------------------------------------------------------------------------------------------
# Audition clips: the same mixing the app does (notes released at 85% of a beat, 60 ms fade).
# ---------------------------------------------------------------------------------------------
def mix_sequence(samples: dict, events, gate=0.85, release_s=0.06, voice_gain=0.7) -> np.ndarray:
    """events: (midi, start_seconds, duration_seconds or None for 'let it ring')."""
    end = max(s + (d or 0) + len(samples[m]) / SAMPLE_RATE for m, s, d in events) + 0.5
    out = np.zeros(int(end * SAMPLE_RATE))
    for midi, start, dur in events:
        smp = samples[midi].copy()
        if dur is not None:
            off = int(dur * SAMPLE_RATE)
            rel = int(release_s * SAMPLE_RATE)
            if off < len(smp):
                env = np.ones(len(smp))
                env[off:off + rel] = np.linspace(1, 0, min(rel, len(smp) - off))
                env[off + rel:] = 0
                smp = (smp * env)[: off + rel]
        i = int(start * SAMPLE_RATE)
        out[i:i + len(smp)] += voice_gain * smp
    return soft_clip(out)


def soft_clip(x: np.ndarray, knee=0.7) -> np.ndarray:
    y = x.copy()
    over = np.abs(x) > knee
    y[over] = np.sign(x[over]) * (knee + (1 - knee) * np.tanh((np.abs(x[over]) - knee) / (1 - knee)))
    return y


def write_demos(samples: dict, demo_dir: str, tag: str, gate=0.85) -> None:
    os.makedirs(demo_dir, exist_ok=True)
    beat = 60.0 / 90.0
    melody = [60, 64, 62, 67]  # C4 E4 D4 G4 at 90 BPM, as the app plays a melody
    write_wav(os.path.join(demo_dir, f"{tag}_melody_90bpm.wav"),
              mix_sequence(samples, [(m, i * beat, gate * beat) for i, m in enumerate(melody)]))
    fast = 60.0 / 150.0
    write_wav(os.path.join(demo_dir, f"{tag}_melody_150bpm.wav"),
              mix_sequence(samples, [(m, i * fast, gate * fast) for i, m in enumerate([60, 62, 64, 65, 67, 65, 64, 62, 60])]))
    # Keys tapped by hand: no note-off, notes ring out naturally and overlap.
    write_wav(os.path.join(demo_dir, f"{tag}_keys_tapped.wav"),
              mix_sequence(samples, [(m, i * 0.45, None) for i, m in enumerate([60, 64, 67, 72, 67, 64, 60])]))
    # Whole range, one note per 0.4 s, so the timbre can be judged top to bottom.
    write_wav(os.path.join(demo_dir, f"{tag}_full_range.wav"),
              mix_sequence(samples, [(m, i * 0.4, None) for i, m in enumerate(range(LOWEST_NOTE, HIGHEST_NOTE + 1, 2))]))


def main(argv=None) -> int:
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--preset", choices=sorted(PRESETS), default="soft")
    ap.add_argument("--out", default=os.path.join(os.path.dirname(__file__), "..", "app", "src", "main", "res", "raw"),
                    help="directory for piano_<midi>.wav files")
    ap.add_argument("--demo", metavar="DIR", help="also write audition clips here")
    ap.add_argument("--no-samples", action="store_true", help="only write demos, don't touch --out")
    args = ap.parse_args(argv)

    p = PRESETS[args.preset]
    samples = {}
    total_bytes = 0
    for midi in range(LOWEST_NOTE, HIGHEST_NOTE + 1):
        rng = np.random.default_rng(1000 + midi)  # deterministic: same file every run
        samples[midi] = render_note(midi, p, rng)
        total_bytes += 2 * len(samples[midi]) + 44
    if not args.no_samples:
        os.makedirs(args.out, exist_ok=True)
        for midi, x in samples.items():
            write_wav(os.path.join(args.out, f"piano_{midi:03d}.wav"), x)
        print(f"wrote {len(samples)} samples ({total_bytes / 1e6:.1f} MB) to {os.path.abspath(args.out)}")
    if args.demo:
        write_demos(samples, args.demo, args.preset)
        print(f"wrote demo clips to {os.path.abspath(args.demo)}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
