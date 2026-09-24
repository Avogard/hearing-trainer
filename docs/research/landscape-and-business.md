<!-- Research report gathered on 24 Sep 2026 for docs/PLAN.md. Every claim links to the page that was opened; items marked unverified could not be confirmed. -->

# Competitive landscape and business model: Android ear-training app ("play it by ear"), researched 24 Sep 2026

## 0. Method and caveats
- Every fact below comes from a page I opened with WebFetch on 24 Sep 2026. Each claim has an inline link, and the date of the source is given where the page shows one.
- **[UNVERIFIED]** marks anything I could not confirm on an opened page.
- **[competitor-authored]** marks comparison blogs written by a rival app. Sonofield's blog discloses that its author owns Sonofield. Fifths' blog is about its own app.
- **Reddit could not be opened.** WebFetch returned "SITE_BLOCKED" on old.reddit.com. Forum signal comes from app-store reviews, the JustinGuitar community and review blogs. The PianoWorld and TalkBass threads would not render.
- **The shared WebSearch quota (200 calls) ran out part-way through Section 3.** After that I only fetched URLs I already knew, so a few items stay unverified: Demucs weight licensing, Melodics prices and funding, any published size for the music-learning market, and Perfect Ear's revenue.
- WebFetch extracts page content through a summarizer. Store rating numbers are the Google Play header figures as of today. Prices vary by country, platform and A/B test.

---

## 1. Ear-training apps

### 1.1 Established apps
The Google Play rating, review count and download band are in the Platform column, with the listing's "updated" date.

| App | Platform (Play rating / reviews / downloads) | Price | Tonal context? | Input | Adaptive? | Stats / streak | Notable complaint | Source URL |
|---|---|---|---|---|---|---|---|---|
| **Perfect Ear** (Crazy Ootka Software AB) | Android, iOS. Play 4.7 / 86.2K / 5M+ (upd. 2026-09-16) | Free + in-app purchases. iOS: Premium $14.99, Full Exercise Pack $4.99, custom editor $1.99, $0.99 packs | Mostly isolated drills; the listing does not mention key or cadence. Sonofield: "quite traditional… limited contextual training" [competitor-authored] | On-screen piano and guitar, note-singing (mic), rhythm tapping, "plug-in your MIDI keyboard" | Guided courses by level; no adaptive engine found | iOS: "Daily Streaks", streak-freeze protection, special icon at 30 days | Rhythm-tap glitches; app closes when you hit Repeat; singing exercises skip; "time mode… not challenging" | [Play](https://play.google.com/store/apps/details?id=com.evilduck.musiciankit&hl=en_US), [App Store](https://apps.apple.com/us/app/perfect-ear-music-rhythm/id1440768353), [Sonofield 2026-03-16](https://sonofield.com/blog/best-ear-training-apps-2026) |
| **Complete Ear Trainer** (Binary Guilt, BE/FR) | Android, iOS, web via "Complete Music Trainer". Play 4.7 / 7.18K / 1M+ (upd. 2026-03-20) | First chapter free; one-time $5.99 on Play (site: €5.99 / $6.99) | **No.** The developer says the goal is to "hear the 'color' of chords regardless of its context" | On-screen answers with sampled grand piano plus 7 sound banks; no mic or MIDI mentioned | Fixed ladder: 150+ drills over 4 levels / 28 chapters, plus Easy (50+) and Arcade (21) modes | Global stats, cloud sync, 25 Play Games achievements, private teacher leaderboards; no streaks | Star gating: "even though I get 31 correct answers out of 32… it's still 2 stars" | [Play](https://play.google.com/store/apps/details?id=com.binaryguilt.completeeartrainer&hl=en_US), [site](https://completeeartrainer.com/) |
| **Functional Ear Trainer** (mobile by Kaizen9 / Serhii Korchan; method and free desktop version by Alain Benbassat, miles.be) | Android, iOS; desktop freeware for Windows/Mac/Linux. Play 4.8 / ~23K / 1M+ (upd. 2026-09-01) | Free core; iOS "Plus" $19.99 one-time; no ads. Sonofield says it is moving to a subscription [UNVERIFIED; iOS still lists the one-time Plus] | **Yes.** Cadence, then a note to identify "in the context of a particular musical key" | Answer buttons labelled with letters, numbers or movable-do; hands-free listening in the paid version | Progressive levels: half scale, octave, multi-octave; major/minor plus chromatics | Stats per level; daily reminders (iOS); no streaks | Users ask for "login/practice streaks" and "a leveling system"; the cadence plays before every note | [Play](https://play.google.com/store/apps/details?id=com.kaizen9.fet.android&hl=en_US), [App Store](https://apps.apple.com/us/app/functional-ear-trainer/id1088761926), [AdvancingMusician 2026-08-06](https://advancingmusician.com/functional-ear-trainer-app/), [miles.be](https://www.miles.be/software/functional-ear-trainer-v2/) |
| **MyEarTraining** (myrApps, SK) | Android, iOS. Play 4.8 / 6.57K / 500K+ (upd. 2026-06-16) | Free + in-app purchase; iOS Premium $14.99 one-time | **Yes.** "Functional exercises (sounds in tonal context)" | Virtual piano, buttons; singing exercises on iOS | 100+ course exercises, custom exercises, daily challenge | Stats synced across devices | Global "exercise length" setting removed; repeated rating prompts; bugs on screen rotation | [Play](https://play.google.com/store/apps/details?id=com.myrapps.eartraining&hl=en_US), [App Store](https://apps.apple.com/by/app/myeartraining-ear-trainer/id885622580) |
| **EarMaster** | iOS, Android, macOS, Windows, Chromebook. Play 4.6 / 1.3K / 100K+ (upd. 2026-08-31) | Free starter content. iOS bundles $7.99–$14.99, single items $1.99–$3.99. An all-access subscription exists, but no price on official pages ($5.99/mo or $43.80/yr per Sonofield [UNVERIFIED]) | Cadence mode available (per Fifths [competitor-authored]) | Mic singing, clapping/tapping, MIDI (iOS; the Play listing says "newly added" on Android), on-screen keyboard, fingerboard, tone ladder | Vendor claim: "AI-powered system adapts exercises to your performance"; 4000+ exercises | Detailed progress tracking; no streaks mentioned | Pitch detector "misidentifies non-octave vocal harmonics" for baritones; display bug on some devices; "clunky and dated" interface | [site](https://www.earmaster.com/), [Play](https://play.google.com/store/apps/details?id=com.earmaster.android&hl=en_US), [App Store](https://apps.apple.com/us/app/earmaster-music-theory/id1105030163), [mobile page](https://www.earmaster.com/products/ear-training-sight-singing/earmaster-for-mobile.html) |
| **Tenuto / musictheory.net** | Tenuto: iOS only. Web exercises free | Tenuto $4.99 one-time; web free | Limited or none (Fifths) | Piano keyboard, fretboard, staff, Bluetooth MIDI | Customizable, not adaptive | No progress tracking mentioned | No lessons; can't pin or hide exercises; chord trainer only asks for the chord's quality | [App Store (v5.1, 2025-04-28)](https://apps.apple.com/us/app/tenuto/id459313476), [musictheory.net](https://www.musictheory.net/exercises) |
| **ToneGym** (SoundGym Ltd, IL) | Web plus new native iOS/Android apps. Android app: 2.8 / 5 reviews / 100+ (upd. 2026-08-06) | Pro $13.95/mo, $74.95/yr ("most popular"), $255 lifetime; "17 music games" | Not specified | Not documented | "Personalized workouts" | Daily workouts, weekly coin contests, levels, a "TPI" performance index, leaderboards; "progress, streak, and TPI stay in sync" | "pay to win"; "exercises are quite basic", dated interface (Sonofield) | [shop](https://www.tonegym.co/shop/index), [home](https://www.tonegym.co/), [apps FAQ](https://www.tonegym.co/help/item?id=is-there-a-tonegym-mobile-app), [Play](https://play.google.com/store/apps/details?id=com.hk.tonegym&hl=en_US) |
| **Sonofield** | iOS, Android, macOS. Play 4.8 / 866 / 10K+ (upd. 2026-06-30) | Free; "Lifetime Pro" one-time purchases listed at $14.99–$24.99 on iOS | **Yes.** Drone-based scale degrees | Touch; singing (Voice mode); hands-free Pocket mode; Free Play instrument | Progression paths and difficulty levels | Pro has "detailed game statistics"; no streaks yet ("stats in a future update") | "could seriously dent a beginner's confidence"; dark-mode UX; no chord training | [Play](https://play.google.com/store/apps/details?id=com.sonofield.et&hl=en_US), [App Store](https://apps.apple.com/us/app/sonofield-ear-trainer/id6740409139), [site](https://sonofield.com/apps/ear-trainer) |
| **Meludia** (FR) | Web, plus the "Meludia Melody" mobile app | Web €14.90/mo or €149/yr, 14-day trial. Melody app €9.99 lifetime | **Yes.** Perception-first with no sheet music; the Melody app uses a drone tonic | Web; Melody app uses a virtual keyboard | Modules from Discovery to Expert | Melody app: best/worst score per module; no streaks | Melody app (3.3/5 from 20 ratings, last update 2022-09-02): notes don't stop, giving a "dissonant mess"; drone and timer can't be turned off; repetitive | [pricing](https://meludia.com/en/how-much-does-it-cost-to-subscribe-to-meludia/), [home](https://meludia.com/en/), [App Store](https://apps.apple.com/us/app/meludia-melody-ear-training/id1263474921) |
| **Chet** (Ensemble Education) | iOS, iPadOS, macOS only | Free | Uses "professionally recorded music". Sonofield: "focuses more on absolute pitches than contextual relationships" | Virtual piano, guitar and bass; MIDI; sight-singing | Learning path | Daily Challenge with a rating leaderboard and leagues; global leaderboards | Melodic-minor handling; landscape-only | [App Store (v2026.3)](https://apps.apple.com/us/app/chet-ear-training/id1405525467), [site](https://www.ensemble-education.com/chet) |
| **Teoria.com** | Web | Free; memberships ended July 2021 | Limited | Browser; MIDI keyboards in Chrome, Firefox, Edge, Opera | No | Scores and graphs, daily practice time | "Older interface; minimal curriculum" (Fifths) | [2021 notice](https://www.teoria.com/en/help/teoria_2021.php), [exercises](https://www.teoria.com/en/exercises/) |
| **Auralia** (Rising Software) | Cloud, any device; sold to schools | Schools: "Auralia & Musition First" $4 per student (50-student minimum), 6- or 12-month terms | Cadences are one of the topics | Not specified | "Carefully graded levels" | Teacher tracking, assignments, exams | Trial only for teachers and schools; not a consumer product | [Auralia](https://www.risingsoftware.com/auralia), [First](https://www.risingsoftware.com/first) |
| **Better Ears** (Android: MAMP GmbH) | Android: 3.1 / 44 / 1K+ (last update 2023-09-01) | Not listed | Not stated | Virtual keyboard, fretboard, MIDI | Not stated | Right/wrong count per question | Settings dark-on-dark and unreadable; correct answers marked wrong; notation errors | [Play](https://play.google.com/store/apps/details?id=de.appsolute.betterearspremium&hl=en_US) |
| **Earpeggio** (Blazing Apps) | iOS only; 4.9 from 9.7K ratings | Free | Not stated; traditional drills | Virtual piano only | Goal-based tests; 9/10 needed to pass | "Broad statistics" | Audio glitches; login and friend-invite prompts; chord-naming errors; forum: "a shame it's only on Apple" | [App Store](https://apps.apple.com/us/app/earpeggio/id884775105), [JustinGuitar 2024-02-06](https://community.justinguitar.com/t/ear-training-app-earpeggio/288812) |
| **EarBeater** | iOS | [UNVERIFIED] | — | — | — | — | Both App Store pages returned 404 and the website has a TLS error, so it may be delisted | — |
| **Hooktheory Chord Crush** (plus Hookpad) | Web; iOS/Android "not yet" | Free: 5 puzzles/day plus 1 Rush game. $49/yr Standard, $79/yr Premium. Hookpad $7.99/mo or $199 lifetime | **Yes.** Chord progressions from real songs in the Hooktheory database | Drag chord options into the gaps | **Yes.** Difficulty follows your rating | Daily puzzle cap, rating | None found. Beta opened 2021-08-27 | [Chord Crush](https://www.hooktheory.com/chord-crush), [pricing](https://www.hooktheory.com/pricing), [forum](https://forum.hooktheory.com/t/introducing-chord-crush-beta-a-chord-progression-ear-trainer-by-hooktheory/4961) |
| **Solfy** (two different apps share the name) | Solfy SAS (FR): Play 4.0–4.1 / 38 / 10K+ (upd. 2026-09-09). Xatet Technologies (ES): 4.8 / 70 / 1K+ | Free + subscription (price not shown) | Not stated | Not stated | Adaptive exercises | Solfy SAS claims 20,000+ users and 3M exercises done | Xatet version: "a very poorly made cash grab" | [solfy.io](https://solfy.io/en/app), [Play (Solfy SAS)](https://play.google.com/store/apps/details?id=com.app.solfy&hl=en-US), [Play (Xatet)](https://play.google.com/store/apps/details?id=com.xatet.solfyApp&hl=en_US) |

**Exercise coverage in brief:**
- **Perfect Ear:** intervals, scales, chords, rhythm, melodic dictation, sight reading, absolute pitch, note singing, a scale dictionary and theory articles ([Play](https://play.google.com/store/apps/details?id=com.evilduck.musiciankit&hl=en_US)).
- **Complete Ear Trainer:** 24 intervals, 36 chord types, inversions, 28 scales, melodic dictation and progressions ([site](https://completeeartrainer.com/)).
- **EarMaster:** intervals, chords, scales, dictation, sight-singing, rhythm, jazz workshops, plus ABRSM and RCM exam prep ([site](https://www.earmaster.com/)).
- **Earpeggio:** ten exercises, including melodic and rhythm dictation ([App Store](https://apps.apple.com/us/app/earpeggio/id884775105)).
- **Meludia:** 600 exercises; claims 100,000 users in 162 countries and 300 musical excerpts, including "applied listening" on well-known songs ([home](https://meludia.com/en/)).

**Download history published by developers:**
- Complete Ear Trainer's developer reported 200K Play downloads in July 2017, and more than 2M across his three apps in August 2021 ([dev blog](https://stephanedupont.com/category/complete-ear-trainer/)).

### 1.2 Newer entrants (2024–2026)

| App | Platform | Price | Tonal context? | Input | Adaptive? | Stats / streak | Notable complaint | Source URL |
|---|---|---|---|---|---|---|---|---|
| **Trill: Ear Training Daily** (gmol6.studio; app id suggests 2025) | iOS; 4.6 from 1,300+ ratings | Weekly $9.99; monthly $4.99–$12.99; yearly $14.99–$59.99 | Not stated | Multiple choice | Difficulty levels | Streaks, daily quests, XP and gems, badges, "Focus Lock" that blocks other apps until you train | "doesn't really TEACH anything"; low notes sound "like a hammer hitting a bell"; forced trial after the onboarding quiz | [App Store](https://apps.apple.com/app/id6742758221) |
| **Fifths** (launched 2025) [competitor-authored] | Web, iOS, Android | $4.99/mo Pro; $99 lifetime for the first 100 buyers | **Yes.** Cadence-primed scale degrees in all 12 keys | Touch; pitch detection in its Voice section | Spaced repetition | n/a | n/a | [fifths.io 2026-05](https://fifths.io/blog/best-ear-training-apps/) |
| **jamjam** (Pierre Blouet) | iOS | Free | Not stated | Mic or instrument | Levels organized as worlds | Stars; ~5-minute daily challenges | Not enough ratings yet | [App Store](https://apps.apple.com/mx/app/jamjam-ear-training/id6747981641) |
| **EarTune** (Eli Chen, 2025) | iOS, macOS, visionOS | Free | No: intervals, chords, frequency in Hz | Tap | "Adaptive coach" | Daily streaks, accuracy charts | Not enough ratings yet | [App Store](https://apps.apple.com/mx/app/eartune/id6743134928) |
| **Play by Ear** (David Rowthorn) | Android; 100+ downloads (upd. 2026-03-27) | Free + in-app purchases | Generated phrases, from simple intervals up to "chromatic passing notes" | You play it back on your own instrument and judge yourself; no automated scoring | Customizable | Scoring deliberately left out | — | [Play](https://play.google.com/store/apps/details?id=com.playbyear.app&hl=en) |
| **Play By Ear Training** (Tomer Boyarski) | Android; 1K+ (upd. 2026-09-14) | Free | Not stated | Any instrument or voice (per listing) | "Adaptive difficulty" | Game modes | — | [Play](https://play.google.com/store/apps/details?id=com.eartraining.ear_training&hl=en-US) |
| **Ear Trainer by Songsterr** | iOS (v2.3.5, 2023) | Free | Match melodies over Blues, Swing, Bossa Nova and Funk Fusion backing tracks; phrases played by live musicians | Virtual piano | Levels | — | The solo line is hard to hear over the backing | [App Store](https://apps.apple.com/us/app/-/id1600207375) |
| **Piano by Ear – Play Instantly** | iOS | $12.99 | Major and minor modes | Select notes; "latch mode" for practising on a real piano | Levels | None | Only one rating | [App Store](https://apps.apple.com/us/app/id1635145345) |
| **Use Your Ear** ("practice on full songs", "AI-powered") | [UNVERIFIED] | — | — | — | — | — | Page content couldn't be extracted | [site](https://www.useyourear.com/use-your-ear-app) |

### 1.3 Complaints that recur across ear-training apps
- **Skills don't transfer to real songs.** Sonofield's own blog (2025-10-26, updated 2026-03-16) gives three reasons. Changing harmony "shifts how each scale degree feels", real instrumentation varies, and the listening mindset in songs differs from drills ([Sonofield](https://sonofield.com/blog/apps-to-real-music)). JustinGuitar members recommend transcribing real songs alongside app drills ([JustinGuitar](https://community.justinguitar.com/t/ear-training-app-earpeggio/288812)).
- **Hostile to beginners:**
  - Complete Ear Trainer's star gating ([Play](https://play.google.com/store/apps/details?id=com.binaryguilt.completeeartrainer&hl=en_US)).
  - Sonofield's warning about denting beginners' confidence ([Play](https://play.google.com/store/apps/details?id=com.sonofield.et&hl=en_US)).
  - Trill jumps straight to random octaves, leaving beginners "forced to merely guess" ([App Store](https://apps.apple.com/app/id6742758221)).
- **Poor sound or buggy grading:**
  - Trill's synth sounds ([App Store](https://apps.apple.com/app/id6742758221)).
  - Earpeggio's audio glitches ([App Store](https://apps.apple.com/us/app/earpeggio/id884775105)).
  - Meludia notes that keep ringing ([App Store](https://apps.apple.com/us/app/meludia-melody-ear-training/id1263474921)).
  - Better Ears marking correct answers wrong ([Play](https://play.google.com/store/apps/details?id=de.appsolute.betterearspremium&hl=en_US)).
- **Monetization friction:**
  - "pay to win" on ToneGym ([Play](https://play.google.com/store/apps/details?id=com.hk.tonegym&hl=en_US)).
  - "cash grab" on Solfy (Xatet) ([Play](https://play.google.com/store/apps/details?id=com.xatet.solfyApp&hl=en_US)).
  - Trill's forced trial ([App Store](https://apps.apple.com/app/id6742758221)).
- **Apps with a strong method lack engagement features.** Functional Ear Trainer users ask for streaks and levelling ([Play](https://play.google.com/store/apps/details?id=com.kaizen9.fet.android&hl=en_US)). Sonofield has no stats or streaks yet ([Play](https://play.google.com/store/apps/details?id=com.sonofield.et&hl=en_US)).
- **Pitch detection is unreliable**, e.g. EarMaster with low male voices ([App Store](https://apps.apple.com/us/app/earmaster-music-theory/id1105030163)).

---

## 2. Song-learning and practice apps

"Tonal context?" is read here as "does it teach playing by ear?".

| App | Platform (Play rating / reviews / downloads) | Price | Tonal context / by-ear teaching? | Input | Adaptive? | Stats / streak | Notable complaint | Source URL |
|---|---|---|---|---|---|---|---|---|
| **Yousician** | Android, iOS; can also be bought on the web. Play 4.5 / 526K / 50M+ (upd. 2026-09-10) | Premium $14.99/mo or $89.99/yr (one instrument). Premium+ $29.99/mo or $139.99/yr. Family $44.99/mo or $209.99/yr. 7-day trial | Play-along to tabs and notation; by-ear not mentioned | **Mic**: "listens as you play"; pitch feedback for singing | Lesson path | Daily practice tracker ("not overly pushy") | "you can't get very far without a subscription"; chord recognition fails in noisy rooms; popular songs only in Premium+ | [Play](https://play.google.com/store/apps/details?id=com.yousician.yousician&hl=en_US), [support](https://support.yousician.com/hc/en-us/articles/115005189525-Premium-membership-options-in-Yousician), [Guitar Chalk 2026-04-21](https://www.guitarchalk.com/yousician-cost/), [HackerNoon 2026-02-24](https://hackernoon.com/best-piano-learning-apps-in-2026-an-in-depth-comparison-of-music-education-technology) |
| **Simply Piano** (Simply, formerly JoyTunes) | Android, iOS. Play 4.6 / 982K / 50M+ (upd. 2026-08-12) | $17.90/mo or $169.90/yr; Family $23.90/mo or $209.90/yr (review site, 2026). Trial is 7 days per the Play listing, "14-day… on the yearly plans" per the review | Not taught | **Mic or MIDI** | Personalized 5-minute workouts | Routine building | Part of lesson 1 locked to the Family plan; next note highlights too fast; can't rewind; thin beyond early-intermediate | [Play](https://play.google.com/store/apps/details?id=com.joytunes.simplypiano&hl=en_US), [PianoStartGuide 2026-09-02](https://www.pianostartguide.com/simply-piano-review/) |
| **flowkey** | Android, iOS. Play 4.6 / 45.8k / 5M+ (upd. 2026-09-17) | Classic $16.49/mo or $99.99/yr; Premium $24.99/mo or $149.99/yr; Family $37.49/mo or $224.99/yr. 7-day trial only on yearly plans; 14-day money-back; no lifetime plan | Not taught | **Mic or MIDI** | Not stated | No streaks mentioned | Charged immediately when starting a "trial"; notes not recognized on an out-of-tune piano; almost nothing usable for free | [help](https://help.flowkey.com/en/articles/4466337-which-subscription-plans-are-available), [Play](https://play.google.com/store/apps/details?id=com.flowkey.app&hl=en_GB) |
| **Skoove** | Platforms not verified | $29.99/mo; $59.99 per 3 months; $149.99/yr; 14-day trial; lifetime via support (2026-02-11) | Not taught | Mic and MIDI (HackerNoon) | 1-on-1 instructor support | — | Smaller song library; expensive month-to-month | [help](https://help.skoove.com/en/articles/5417607-what-is-skoove-premium-and-how-much-does-it-cost), [HackerNoon](https://hackernoon.com/best-piano-learning-apps-in-2026-an-in-depth-comparison-of-music-education-technology) |
| **Melodics** | Drums, MIDI keys, finger-drumming pads | Standard and Premium tiers (Premium adds 180+ song lessons); 7-day free trial. "$9.99/mo" entry price from a third-party site [UNVERIFIED] | No | MIDI instruments and e-drums (as referenced on the plans page) | — | — | — | [plans](https://melodics.com/plans), [support](https://support.melodics.com/en/articles/8044669-how-to-subscribe-to-melodics), [Subger](https://subger.com/en/us/service/melodics) |
| **Playground Sessions** | iOS, Android, Mac, Windows | $24.99/mo; $149.99/yr; $349.99 lifetime (includes 2 years of unlimited songs) | Not taught | MIDI cable ("100% accuracy") or device Mic Feedback ("accuracy is lower") | — | — | Arrangements heavily simplified; Play rating 3.7 per HackerNoon [not verified on Play] | [pricing](https://support.playgroundsessions.com/hc/en-us/articles/360001017446-How-much-does-a-Playground-Sessions-membership-cost), [mic](https://support.playgroundsessions.com/hc/en-us/articles/40277286306196-How-do-I-get-feedback-and-scores-without-using-a-cable) |
| **Duolingo Music** | iOS (Oct 2023), later Android | Free with ads; Super removes ads | Some listening work: "pitch, meter, rhythm, and interval"; mostly reading and songs | On-screen keyboard; 2025 "instruments tab" to practise on a real piano; Loog x Duolingo piano ($249, 37 keys, USB-C) | Duolingo path | Shared Duolingo streaks, quests and leagues | No bass clef; concepts not explained; phone playback "choppy and staccato" | See 2.1 |

HackerNoon (2026-02-24) says none of the mic- or MIDI-based piano apps teaches playing by ear. It notes flowkey "teaches mimicry over independent musicianship" ([HackerNoon](https://hackernoon.com/best-piano-learning-apps-in-2026-an-in-depth-comparison-of-music-education-technology)).

### 2.1 Duolingo Music in 2025–2026: not cut, being rebuilt
- **Launch.** Released Oct 2023, iOS only at first, in English and Spanish, on an on-screen keyboard with 200+ public-domain songs ([press release 2023-11-09](https://investors.duolingo.com/news-releases/news-release-details/duolingo-launches-music-and-math-its-flagship-app)). "You don't need to own an instrument" ([blog](https://blog.duolingo.com/music-course/)). Free with ads, or ad-free with Super ([MusicRadar 2023-09-08](https://www.musicradar.com/news/duolingo-music-lessons)).
- **2025 additions.**
  - A beginner piano section.
  - An "instruments tab so you can practice songs with a real piano".
  - Popular-artist tracks brought to the Android music courses ([Duolingo 2025 highlights, 2025-12-10](https://blog.duolingo.com/product-highlights/)).
  - The Loog x Duolingo piano ($249) connects to the app over USB-C, and is currently sold out ([store](https://store.duolingo.com/products/loog-x-duolingo-piano)).
- **Aug 2025: NextBeat team acquired.** Duolingo took on 23 staff from NextBeat, makers of *Beatstar* and *Country Star*: over 100M downloads and nearly $200M revenue. The team includes music-licensing expertise, and Duolingo cited "millions of learners" of Music ([GlobeNewswire 2025-08-06](https://www.globenewswire.com/news-release/2025/08/06/3128622/0/en/Duolingo-Doubles-Down-on-Delight-with-Acquisition-of-Music-Gaming-Startup-NextBeat-s-Innovative-Team.html)).
  - Reported price $34.5M, with NextBeat's own games shut by Oct 2025 ([Naavik 2025-08-31](https://naavik.co/digest/duolingos-next-beat/), citing Class Central). The price is [not confirmed in Duolingo filings I opened].
- **Feb 2026.** The Q4 2025 letter says: "Our music course is getting a full revamp to make it more fun and game-like, thanks to our acquisition of the team behind NextBeat" ([letter](https://investors.duolingo.com/static-files/961ce633-3cee-49d0-bd7a-2c63731d45fb)).
  - The Q1 2026 letter lists chess, music and math as "next engines of growth" ([SEC](https://www.sec.gov/Archives/edgar/data/1562088/000162828026029790/q1fy26duolingo3-31x26share.htm)).
  - The Q2 2026 letter (2026-08-05) doesn't mention Music ([SEC](https://www.sec.gov/Archives/edgar/data/1562088/000162828026053299/q2fy26duolingo6-30x26share.htm)).
  - No Music user numbers have been disclosed. By contrast, Chess reached "more than seven million DAUs in less than a year" ([Q4 letter](https://investors.duolingo.com/static-files/961ce633-3cee-49d0-bd7a-2c63731d45fb)).
- **Musician reviews.**
  - Rocknerd (2024-11-12): "does what it says on the tin" as a free introduction, but no bass clef, no written explanations, and hard to perform on a phone ([Rocknerd](https://rocknerd.co.uk/2024/11/12/duolingo-music-review/)).
  - Duoplanet (2023-10-30): lacks theory explanations; playing on a phone is "clunky… choppy and staccato" ([Duoplanet](https://duoplanet.com/duolingo-music/)).

---

## 3. Tools people use to pick out songs, and ML building blocks

### 3.1 Consumer tools

| Tool | Platform / Play stats | Price | What it does | Limitation / complaint | Source URL |
|---|---|---|---|---|---|
| **Moises** | Android, iOS, web. Play 4.6 / 431K / 50M+ (upd. 2026-09-21) | Free with ads and limits; Premium $3.99/mo; Pro $9.99/mo (third-party review, updated 2026-03-19) | Stem separation (vocals, drums, guitar, bass, piano, strings), chord detection, key/BPM, pitch and speed change, lyrics, smart metronome | Stem separation on Android "simply doesn't work" for some users; credit system unclear | [Play](https://play.google.com/store/apps/details?id=ai.moises&hl=en_US), [StemSplit](https://stemsplit.io/blog/moises-ai-review) |
| **Chordify** | Android, iOS, web. Play 4.3 / 42.5K / 10M+ (upd. 2026-09-07) | Tiers Basic / Premium / Premium Plus ("Toolkit"). Premium $8/mo or $3.50/mo billed yearly per Guitar Chalk (2026-04-19) | Chords synced to the song; transpose, loop, tempo, MIDI export | Full-page ads; free tier cut to 1 song; surprise annual renewal | [Play](https://play.google.com/store/apps/details?id=net.chordify.chordify&hl=en_US), [support](https://support.chordify.net/hc/en-us/articles/360002273238-What-are-the-subscription-options), [Guitar Chalk](https://www.guitarchalk.com/chordify-review/) |
| **Chord ai** (Machinita, Graz) | Android, iOS. Play 4.8 / 59.1K / 1M+ (upd. 2026-09-13) | Freemium plus PRO subscription | Live chord recognition from mic or device audio, works offline; beat, tempo and key; lyrics via Whisper; 4 stems; **audio-to-MIDI using Spotify's Basic Pitch** | "gone from being absolutely amazing to a complete mess"; wrong BPM; crashes | [Play](https://play.google.com/store/apps/details?id=com.chordai&hl=en_US), [site](https://chordai.net/) |
| **Transcribe!** | Windows, Mac, Linux | $39 one-time; 30-day evaluation | Slow down without pitch change (1/20x to 2x), spectrum view with note and chord guessing, EQ, loops, video | Desktop only | [overview](https://www.seventhstring.com/xscribe/overview.html), [buy](https://www.seventhstring.com/xscribe/buy.html) |
| **Anytune** | Android 100K+, 4.3–4.4 from ~1.2K reviews (upd. 2026-04-09) | Free plus a Pro upgrade | Slow down and loop Spotify / Apple Music streams; transpose ±24 semitones | No note or chord detection; crashes | [Play](https://play.google.com/store/apps/details?id=app.anytune.musicplayer&hl=en_US) |
| **Soundslice** | Web | Free; Plus $5/mo or $50/yr; Teacher $20/mo; Licensing $100/mo | Notation synced to real recordings, slow-down, loop, tools for manual transcription | On automatic transcription: "No software in the world does that with any reasonable degree of accuracy" | [plans](https://www.soundslice.com/plans/), [transcribe](https://www.soundslice.com/transcribe/) |
| **Ultimate Guitar Pro** | Android, iOS, web | $99/yr (Guitar Chalk, 2026-04-16; monthly price unclear) | Official interactive tabs, playback, transpose, loop, courses in the app | No ear training | [help 2026-05-22](https://help.ultimate-guitar.com/en/articles/6741560-what-is-ultimate-guitar-pro-subscription), [Guitar Chalk](https://www.guitarchalk.com/ultimate-guitar-pro-review/) |

### 3.2 ML building blocks for on-device use

| Component | What it does | Licence | Android readiness | Source URL |
|---|---|---|---|---|
| **Basic Pitch** (Spotify) | Polyphonic, instrument-agnostic audio-to-MIDI with pitch bends; "<20 MB peak memory and <17K parameters"; "works best on one instrument at a time" | Apache-2.0 | Model shipped for TensorFlow, CoreML, **TFLite and ONNX**; TypeScript port (basic-pitch-ts); already used inside the Chord ai mobile app | [GitHub](https://github.com/spotify/basic-pitch), [Spotify 2022-06-01](https://engineering.atspotify.com/2022/06/meet-basic-pitch), [Chord ai](https://chordai.net/) |
| **Demucs v4** (HT Demucs) | Stem separation, 9.0 dB SDR on MUSDB HQ | MIT (code); licence of the pretrained weights [UNVERIFIED] | **Repo archived 2025-01-01**, "not maintained". Runs at about 1.5x track length on a CPU; GPU needs at least 3 GB. The community port demucs.onnx (MIT) runs on ONNX Runtime in C++ but only ships a Linux build script. Too heavy for phones today | [GitHub](https://github.com/facebookresearch/demucs), [demucs.onnx](https://github.com/sevagh/demucs.onnx) |
| (Moises Live) | Announced real-time on-device stem separation | Proprietary | Shows it can be done with in-house models | [MBW 2025-01-22](https://www.musicbusinessworldwide.com/music-ai-raises-40m-in-series-a-round-as-its-moises-platform-hits-50m-users/) |
| **pYIN** | Single-voice pitch (f0) plus voiced/unvoiced probability, using YIN candidates and Viterbi smoothing | librosa implementation is ISC-licensed; licence of the original Vamp plugin [UNVERIFIED; site blocked] | Python only; no Kotlin library found. It's classical DSP, so porting it to Kotlin is feasible (my assessment) | [librosa docs](https://librosa.org/doc/main/generated/librosa.pyin.html), [librosa licence](https://github.com/librosa/librosa) |
| **CREPE** | Single-voice pitch network; 5 sizes from tiny to full; 10 ms hop | MIT | Keras/TensorFlow. **onnxcrepe (MIT)** provides ONNX versions of all sizes, usable with ONNX Runtime on Android | [CREPE](https://github.com/marl/crepe), [onnxcrepe](https://github.com/yqzhishen/onnxcrepe) |
| **SPICE** (Google) | Self-supervised pitch model, "a mobile-compatible pitch extraction model to recognize the dominant pitch in sung audio"; 16 kHz mono input | Tutorial code Apache-2.0; model licence [UNVERIFIED] | TF Hub and Kaggle | [TF tutorial](https://www.tensorflow.org/hub/tutorials/spice), [Kaggle](https://www.kaggle.com/models/google/spice), [Google Research 2019-11-14](https://research.google/blog/spice-self-supervised-pitch-estimation/) |
| **PESTO** (Sony CSL) | Lightweight real-time pitch model; accuracy "close to… CREPE (with 800x more parameters)"; streaming support | **LGPL-3.0** | Built-in ONNX export at ~0.7 ms per inference; v2 released 2025 | [GitHub](https://github.com/SonyCSLParis/pesto) |
| **TarsosDSP** | Java DSP: YIN, FastYin, McLeod (MPM), dynamic wavelet, AMDF, onset detection, time-stretch | **GPL-3.0** | Android support since v2.0; last release v2.5 (2023-01-09) | [GitHub](https://github.com/JorenSix/TarsosDSP) |
| **ONNX Runtime Mobile** | Runs models on Android | MIT | `onnxruntime-android` package, Java/C/C++, NNAPI and XNNPACK acceleration; custom builds shrink the library from ~24 MB to ~7.5 MB | [docs](https://onnxruntime.ai/docs/tutorials/mobile/), [GitHub](https://github.com/microsoft/onnxruntime) |
| **LiteRT** (formerly TFLite) | Runs models on Android | Apache-2.0 | Kotlin and C++ APIs; CPU (XNNPack), GPU and NPU support; v2.1.5 (2026-05-18) | [GitHub](https://github.com/google-ai-edge/LiteRT) |

**What the licences mean for a closed-source app:**
- **Apache-2.0, MIT and ISC** (Basic Pitch, CREPE/onnxcrepe, librosa, ONNX Runtime, LiteRT) can be used commercially if you keep their notices.
- **LGPL** (PESTO) needs the library to stay replaceable, so it should be linked dynamically.
- **GPL-3.0** (TarsosDSP) is effectively ruled out.
- **Model weights can carry a different licence from the code.** Check Demucs and SPICE before shipping [UNVERIFIED].
- **Real songs need music licensing.** Duolingo launched with public-domain songs ([PR](https://investors.duolingo.com/news-releases/news-release-details/duolingo-launches-music-and-math-its-flagship-app)) and later hired music-licensing expertise with NextBeat ([GlobeNewswire](https://www.globenewswire.com/news-release/2025/08/06/3128622/0/en/Duolingo-Doubles-Down-on-Delight-with-Acquisition-of-Music-Gaming-Startup-NextBeat-s-Innovative-Team.html)).

---

## 4. Duolingo as a business and mechanics reference

### 4.1 Latest reported numbers

| Metric | Q2 2026 (letter 2026-08-05) | Q1 2026 (2026-05-04) | Q4 2025 | FY2025 |
|---|---|---|---|---|
| Daily active users (DAU) | 58.7M (+23% YoY) | 56.5M (+21%) | 52.7M (+30%) | — |
| Monthly active users (MAU) | 140.6M (+10%) | 137.8M (+5.9%) | 133.1M (+14%) | — |
| Paid subscribers | 12.7M (+17%) | 12.5M (+21%) | 12.2M (+28%) | 12.2M |
| Revenue | $298.5M (+18%) | $292.0M (+27%) | $282.9M (+35%) | $1,037.6M |
| Bookings | $289.1M (+8%); subscription bookings $250.3M | $308.5M (+14%) | — | $1,158.4M; subscription $996.3M |
| DAU/MAU (my calculation) | 41.7% | 41.0% | 39.6% | — |
| Paid share of MAU (my calculation) | 9.0% | 9.1% | 9.2% | — |

Sources: [Q2 2026](https://www.sec.gov/Archives/edgar/data/1562088/000162828026053299/q2fy26duolingo6-30x26share.htm), [Q1 2026](https://www.sec.gov/Archives/edgar/data/1562088/000162828026029790/q1fy26duolingo3-31x26share.htm), [Q4/FY2025 letter](https://investors.duolingo.com/static-files/961ce633-3cee-49d0-bd7a-2c63731d45fb), [Q4 press release 2026-02-26](https://investors.duolingo.com/news-releases/news-release-details/duolingo-reports-fourth-quarter-and-full-year-2025-results).

**Other figures from the Q2 2026 letter** ([SEC](https://www.sec.gov/Archives/edgar/data/1562088/000162828026053299/q2fy26duolingo6-30x26share.htm)):
- Retention of current users (CURR) is at "an all-time high of 84%".
- FY2026 guidance as extracted: bookings ~$1,285M, revenue ~$1,207M, adjusted EBITDA ~$320M.

### 4.2 Strategy in 2026
- **Growth over monetization.** Duolingo "decided to prioritize user growth over monetization". It estimates "more than $50M of foregone bookings from friction" and moved part of the monetization team to top-of-funnel growth ([Q4 letter](https://investors.duolingo.com/static-files/961ce633-3cee-49d0-bd7a-2c63731d45fb)).
- **Goal: 100M DAU in 2028** ([Q1 2026](https://www.sec.gov/Archives/edgar/data/1562088/000162828026029790/q1fy26duolingo3-31x26share.htm)). The stock fell 14% on the 2026 guidance ([TIKR 2026-02-28](https://www.tikr.com/blog/duolingo-nasdaq-duol-stock-slides-14-following-disappointing-2026-revenue-outlook)).
- **Q2 2026 changes:**
  - "Offering longer free trials" ([Q2 letter](https://www.sec.gov/Archives/edgar/data/1562088/000162828026053299/q2fy26duolingo6-30x26share.htm)). The earnings-call summary says trials went from 7 days to one month.
  - An ad-supported "Super Lite" at about half Super's price is in testing.
  - Video Call's cost fell from ~$0.30 to under $0.01 per call after switching to open-source models ([BigGo 2026-08-05](https://finance.biggo.com/news/US_DUOL_2026-08-05)).

### 4.3 Current mechanics
- **Path, placement test and Practice Hub.**
  - The path is units of increasing difficulty.
  - A placement test lets you "skip some units to match what you already know".
  - The Practice Hub (mistake review, skill focus) is for Super subscribers only ([Duolingo 101, 2024-12-02](https://blog.duolingo.com/duolingo-101-how-to-learn-a-language-on-duolingo)).
- **Streak and streak freeze.**
  - Freezes are bought with gems ([Duolingo 101](https://blog.duolingo.com/duolingo-101-how-to-learn-a-language-on-duolingo)).
  - Third-party analyses say you can hold up to 2 (more for long streaks), with milestones at 7/30/100/365 days, and that freezes apply automatically ([Deconstructor of Fun 2026-05-15](https://duolingo.deconstructoroffun.com/mechanics/streaks); [Digia 2026-06-08](https://www.digia.tech/post/duolingo-habit-forming-reminders-retention-architecture/)).
  - A June 2026 "streak revival" event brought back 15.4M learners' streaks ([Q2 letter](https://www.sec.gov/Archives/edgar/data/1562088/000162828026053299/q2fy26duolingo6-30x26share.htm)); restoring took three lessons ([BigGo](https://finance.biggo.com/news/US_DUOL_2026-08-05)).
- **Friend Streaks.** Up to 5 per user. Learners with at least one are "22% more likely to complete their daily lesson", and 57% of users have a friend on the app ([Duolingo 2024-09-20](https://blog.duolingo.com/product-lessons-friend-streak/)).
- **Leagues and leaderboards.**
  - Weekly XP leagues with promotion ([Duolingo 101](https://blog.duolingo.com/duolingo-101-how-to-learn-a-language-on-duolingo)).
  - Friends were added to leaderboards in 2025 ([highlights](https://blog.duolingo.com/product-highlights/)).
  - Effect: total learning time rose 17% and highly engaged learners tripled ([Lenny's Newsletter 2023-02-28](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth)).
- **Daily Quests.** Three a day; rewards for completing all three were raised in 2025. Friends Quests now use universal matching ([highlights](https://blog.duolingo.com/product-highlights/)).
- **Energy instead of Hearts (July 2025).**
  - Each lesson uses energy, and it recharges in about a day. Runs of correct answers earn it back.
  - Refills cost a rewarded ad or gems.
  - Rationale: beginners were "2X more likely to run out of hearts mid-lesson" ([Duolingo 2025-07-03](https://blog.duolingo.com/duolingo-energy/)).
- **Pricing.** Prices vary by platform, region and A/B test.

  | Plan | Price | Source |
  |---|---|---|
  | Super | ~$12.99/mo; $83.99–$95.99/yr | [DealNews, updated to Sep 2026](https://www.dealnews.com/features/duolingo/cost/) |
  | Super (iOS, Sep 2026) | $16.99/mo; $119.99/yr | [LanguageAppGuide 2026-09-06](https://languageappguide.com/pricing/duolingo-cost/) |
  | Family | $119.99–$143.99/yr | DealNews; LanguageAppGuide |
  | Max | ~$29.99/mo or ~$168/yr, now being phased out for new subscribers; about 10% of paid subscribers | [Motley Fool 2026-09-14](https://www.fool.com/investing/2026/09/14/the-green-owl-is-ending-duolingos-own-cash-cow/) |
  | "Lite" test | $35.99/yr | LanguageAppGuide |

  Video Call is moving into Super ([Q2 letter](https://www.sec.gov/Archives/edgar/data/1562088/000162828026053299/q2fy26duolingo6-30x26share.htm)).
- **Other:** Chess player-vs-player with Elo ratings; "Duolingo Score" ([highlights](https://blog.duolingo.com/product-highlights/)).

### 4.4 Notifications: the "passive-aggressive" owl and how it's tuned
- **Escalating tone.** Messages escalate to a final "These reminders don't seem to be working. We'll stop sending them for now." ([Debugger/Medium 2020-12-03](https://debugger.medium.com/duolingo-needs-to-chill-8f1832745ca0)).
- **Template selection by bandit algorithm.** Templates are chosen by a multi-armed bandit that learns from ~200M reminders. It only compares messages among users eligible for them (e.g. streak messages go to users with streaks) ([Duolingo 2020-09-03](https://blog.duolingo.com/hi-its-duo-the-ai-behind-the-meme/)).
- **The KDD 2020 paper** ("recovering difference softmax"):
  - Recency penalty with a 15-day half-life (γ=0.017).
  - Results: **+0.5% DAU**, **+2.0% new-user 7-day retention**, +0.4% lessons completed ([paper](https://research.duolingo.com/papers/yancey.kdd20.pdf)).
- **"Protect the channel."** Notification volume is strictly capped, and raising it needed CEO approval. The streak-saver notification was pivotal ([Lenny's Newsletter](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth)).
- **Timing is inferred from behaviour.** Letting users pick their own reminder times tested worse ([Digia, secondary source](https://www.digia.tech/post/duolingo-habit-forming-reminders-retention-architecture/)).
- **Engagement trend.** Around 2024, roughly a third of monthly users were daily users, up about 10 points in five years ([Sherwood 2024-08-09](https://sherwood.news/tech/duolingo-q2-earnings-monthly-active-users-milestone/)).

### 4.5 Controversies in 2025–2026
- **"AI-first" memo (late April 2025).**
  - It said Duolingo would "gradually stop using contractors to do work that AI can handle" ([PR Daily](https://www.prdaily.com/the-scoop-duolingo-ceo-walks-back-ai-first-memo/)).
  - Users cancelled subscriptions and deleted streaks, and Duolingo removed all its TikTok and Instagram videos ([Wikipedia](https://en.wikipedia.org/wiki/Duolingo)).
  - In late May the CEO backtracked: "I didn't do that well" ([PR Daily](https://www.prdaily.com/the-scoop-duolingo-ceo-walks-back-ai-first-memo/)).
  - Third-party data showed DAU growth slowing from 56% in February to 37% in June 2025; the link to the memo is speculative ([Slashdot citing Motley Fool](https://it.slashdot.org/story/25/06/28/2036249/duolingo-stock-plummets-after-slowing-user-growth-possibly-caused-by-ai-first-backlash)).
- **Energy system.**
  - Complaints: energy drains with every exercise, not just mistakes ("Without mistakes, I run out of energy during the third lesson"); practice restores only 5 energy; it reads as a push to pay. In a poll, 62% disliked it ([Android Authority 2025-10-01](https://www.androidauthority.com/quitting-duolingo-energy-system-3599842/)).
  - Management called the Energy rollout part of a "tough compare" for H1 2026 ([Q4 2025 call](https://www.fool.com/earnings/call-transcripts/2026/05/04/duolingo-duol-q4-2025-earnings-transcript/)). I found no reversal.

---

## 5. Market and monetization

### 5.1 Market size: what could be verified
- **No published figure for music-learning or ear-training app revenue was found.** The Grand View Research page had no data ([GVR](https://www.grandviewresearch.com/industry-analysis/online-music-education-market-report)); Naavik shows a Sensor Tower MAU chart but no numbers ([Naavik](https://naavik.co/digest/duolingos-next-beat/)). [UNVERIFIED]
- **Education apps overall:** $6.4B revenue in 2025 (+6.7%), over 1B downloads; Duolingo alone had 172M downloads in 2025 ([Business of Apps 2026-06-11](https://www.businessofapps.com/data/education-app-market/)).
- **Scale proxies from Google Play download bands:**
  - Yousician 50M+ and Simply Piano 50M+ ([Play](https://play.google.com/store/apps/details?id=com.yousician.yousician&hl=en_US), [Play](https://play.google.com/store/apps/details?id=com.joytunes.simplypiano&hl=en_US)).
  - Moises 50M+ on Play and 70M users across platforms ([Moises 2025-12-22](https://moises.ai/newsroom/company-milestones/2025-year-in-review/)).
  - Rocksmith+ is weak: 7K average mobile DAU per Sensor Tower ([Naavik](https://naavik.co/digest/duolingos-next-beat/)).
- **Ear-training niche: my own rough estimate, not a published figure.**
  - Lower bounds of Play download bands: Perfect Ear 5M+, Complete Ear Trainer 1M+, Functional Ear Trainer 1M+, MyEarTraining 500K+, EarMaster 100K+, Sonofield 10K+. That's at least ~7.6M cumulative Android installs.
  - Applying RevenueCat's 2.1% freemium download-to-paid rate ([RevenueCat 2026](https://www.revenuecat.com/state-of-subscription-apps/)) and a $10–15 one-time price gives about **$1.6–2.4M lifetime Android revenue** for this group.
  - Conclusion: the one-time-purchase ear-training niche is in the single-digit millions, an order of magnitude below the song-learning apps. Subscription players add to it, but none publishes revenue (ToneGym claims 400K+ musicians, Meludia 100K users).

### 5.2 Price points and trial designs in the category

| Product | Monthly | Annual | One-time / lifetime | Trial / free design | Source |
|---|---|---|---|---|---|
| RevenueCat Education median (2026) | $9.99 | $44.99 | — | 50.3% of Education trials run 5–9 days | [RevenueCat 2026](https://www.revenuecat.com/state-of-subscription-apps/) |
| Duolingo Super | $12.99–16.99 | $83.99–119.99 | — | 7 days, moving to 1 month | [DealNews](https://www.dealnews.com/features/duolingo/cost/), [BigGo](https://finance.biggo.com/news/US_DUOL_2026-08-05) |
| Yousician Premium / Premium+ | $14.99 / $29.99 | $89.99 / $139.99 | — | 7 days | [Guitar Chalk](https://www.guitarchalk.com/yousician-cost/) |
| Simply Piano | $17.90 | $169.90 | — | 7 days (Play) or 14 days on yearly (review) | [PianoStartGuide](https://www.pianostartguide.com/simply-piano-review/) |
| flowkey | $16.49–24.99 | $99.99–149.99 | None | 7 days, yearly plans only | [help](https://help.flowkey.com/en/articles/4466337-which-subscription-plans-are-available) |
| Skoove | $29.99 | $149.99 | Via support | 14 days | [help](https://help.skoove.com/en/articles/5417607-what-is-skoove-premium-and-how-much-does-it-cost) |
| Playground Sessions | $24.99 | $149.99 | $349.99 | — | [support](https://support.playgroundsessions.com/hc/en-us/articles/360001017446-How-much-does-a-Playground-Sessions-membership-cost) |
| ToneGym | $13.95 | $74.95 | $255 | Free plan | [shop](https://www.tonegym.co/shop/index) |
| Meludia (web) | €14.90 | €149 | €9.99 mobile app | 14 days | [Meludia](https://meludia.com/en/how-much-does-it-cost-to-subscribe-to-meludia/) |
| Chord Crush | — | $49 / $79 | — | Free 5 puzzles/day | [Hooktheory](https://www.hooktheory.com/pricing) |
| Moises | $3.99 / $9.99 | — | — | Free tier with ads | [StemSplit](https://stemsplit.io/blog/moises-ai-review) |
| Trill | $4.99–12.99; weekly $9.99 | $14.99–59.99 | — | Forced trial after onboarding quiz | [App Store](https://apps.apple.com/app/id6742758221) |
| One-time ear trainers | — | — | Complete Ear Trainer $5.99; FET Plus $19.99; Perfect Ear Premium $14.99; MyEarTraining $14.99; Sonofield $14.99–24.99; Tenuto $4.99 | Free core | Section 1 sources |

**Other trial and conversion benchmarks:**
- Hard paywalls convert 5x better than freemium at day 35 (10.7% vs 2.1%). Retention for both models "is nearly identical" after a year ([RevenueCat 2026](https://www.revenuecat.com/state-of-subscription-apps/)).
- Over 80% of Education and Health & Fitness trials last 5–9 days or more. Education refund rate 4.86%; median ARPU $0.27 at day 14 and $0.40 at day 60, as extracted ([RevenueCat 2025](https://www.revenuecat.com/state-of-subscription-apps-2025/)).

### 5.3 Google Play subscription essentials
- **Offers.**
  - Free-trial phases run from **3 days to 3 years**.
  - Introductory price phases can be absolute, a fixed discount or a percentage off.
  - Eligibility can be new customers, upgrades, or developer-determined (e.g. win-back).
  - Offers apply only to auto-renewing base plans; limit of 250 base plans and offers per subscription, 50 active ([Play Console help](https://support.google.com/googleplay/android-developer/answer/12154973)).
- **One free trial per app by default.** Play verifies a payment method before the trial, which can show as a temporary hold ([Android developers](https://developer.android.com/google/play/billing/subscriptions)).
- **Payment failures and lifecycle:**
  - Grace period is on by default and configurable.
  - **Account hold defaults to 60 days minus the grace period.**
  - Pause lasts 1–3 months (1–4 weeks for weekly plans).
  - Apps **must support Restore**.
  - New purchases must be **acknowledged within 3 days** or they are auto-refunded ([lifecycle](https://developer.android.com/google/play/billing/lifecycle/subscriptions), [Create and manage subscriptions](https://support.google.com/googleplay/android-developer/answer/140504)).
  - The exact default grace-period length was not stated on the pages I opened [UNVERIFIED].
- **Policy** ([Subscriptions policy](https://support.google.com/googleplay/android-developer/answer/9900533)):
  - You must "clearly and accurately describe the terms of your offer, including the duration, pricing…".
  - One named violation is "Annual subscriptions that most prominently display their pricing in terms of monthly cost".
  - You must provide "an easy-to-use, online method to cancel".
  - Subscriptions must give "sustained or recurring value".
- **Fees.**
  - 15% on auto-renewing subscriptions.
  - For the EEA, UK and US from **2026-06-30**: "10% + 5% billing fee", where the billing fee applies when paying through Play Billing ([service fees](https://support.google.com/googleplay/android-developer/answer/112622)).

### 5.4 Revenue and funding examples

| Company / app | Published numbers | Source |
|---|---|---|
| Simply (Simply Piano) | $97M venture capital in total; $1B valuation in 2021 | [Wikipedia](https://en.wikipedia.org/wiki/Simply_(software_company)) |
| Yousician | $28M Series B; €35M raised in total by end-2021; 126 employees; revenue not found [UNVERIFIED] | [Wikipedia](https://en.wikipedia.org/wiki/Yousician) |
| Music AI / Moises | $40M Series A (Jan 2025); 50M users then 70M by Dec 2025; "$50+ million raised in 2025" | [MBW](https://www.musicbusinessworldwide.com/music-ai-raises-40m-in-series-a-round-as-its-moises-platform-hits-50m-users/), [Moises](https://moises.ai/newsroom/company-milestones/2025-year-in-review/) |
| NextBeat (Beatstar, Country Star) | Over 100M downloads, ~$200M revenue; team acquired by Duolingo (reported $34.5M) | [GlobeNewswire](https://www.globenewswire.com/news-release/2025/08/06/3128622/0/en/Duolingo-Doubles-Down-on-Delight-with-Acquisition-of-Music-Gaming-Startup-NextBeat-s-Innovative-Team.html), [Naavik](https://naavik.co/digest/duolingos-next-beat/) |
| Complete Ear Trainer | 200K downloads (2017); over 2M across 3 apps (2021); no revenue published | [dev blog](https://stephanedupont.com/category/complete-ear-trainer/) |
| Perfect Ear | 5M+ Play downloads; revenue not public (Swedish company registry sites blocked) [UNVERIFIED] | [Play](https://play.google.com/store/apps/details?id=com.evilduck.musiciankit&hl=en_US) |
| ToneGym / Meludia / Chord ai | Claimed users: 400K+ / 100K / 500K+ | [ToneGym](https://www.tonegym.co/), [Meludia](https://meludia.com/en/), [Chord ai](https://chordai.net/) |
| Melodics | Founded 2014 by ex-Serato CEO Sam Gribben; funding and revenue not found [UNVERIFIED] | [about](https://melodics.com/about) |

---

## Gaps and opportunities
Sourced facts are linked; the product suggestions are my analysis.

1. **Nobody on Android trains "hear a real song, then find it on your instrument" end to end.**
   - The closest are iOS-only or web-only: Chet uses real recordings ([App Store](https://apps.apple.com/us/app/chet-ear-training/id1405525467)); Songsterr's trainer uses styled backing tracks but hasn't been updated since 2023 ([App Store](https://apps.apple.com/us/app/-/id1600207375)); Chord Crush has no native app ([Hooktheory](https://www.hooktheory.com/chord-crush)).
   - Suggestion: a ladder of song-shaped melodic dictation, from tonal context to hooks to phrases over accompaniment to real excerpts.
2. **The transfer problem is openly acknowledged but not solved in any product.** Changing harmony and instrumentation break drill skills ([Sonofield](https://sonofield.com/blog/apps-to-real-music)). Suggestion: add chord changes, bass, drums and timbre variety step by step.
3. **The key-context method works, but it's scattered across apps.**
   - FET does cadence-per-note ([AdvancingMusician](https://advancingmusician.com/functional-ear-trainer-app/)).
   - Sonofield uses a drone and has no chords ([Sonofield](https://sonofield.com/blog/best-ear-training-apps-2026)).
   - Mainstream drills are context-free ([Complete Ear Trainer](https://play.google.com/store/apps/details?id=com.binaryguilt.completeeartrainer&hl=en_US)).
   - Suggestion: make scale degrees in a key the backbone, with cadences that fade out over time.
4. **There is room for a Duolingo-grade daily habit loop on top of real teaching.**
   - Apps with a strong method lack streaks ([FET](https://play.google.com/store/apps/details?id=com.kaizen9.fet.android&hl=en_US), [Sonofield](https://play.google.com/store/apps/details?id=com.sonofield.et&hl=en_US)).
   - Heavily gamified newcomers get "doesn't really TEACH anything" ([Trill](https://apps.apple.com/app/id6742758221)).
   - Friend Streaks alone lift daily lesson completion by 22% ([Duolingo](https://blog.duolingo.com/product-lessons-friend-streak/)).
5. **Let people answer by singing or on their instrument, not only by tapping.** Keyboard-only dictation feels restrictive ([Earpeggio](https://apps.apple.com/us/app/earpeggio/id884775105)), and detectors fail on some voices ([EarMaster](https://apps.apple.com/us/app/earmaster-music-theory/id1105030163)). Permissively licensed pitch models (CREPE/onnxcrepe MIT, Basic Pitch Apache-2.0) run on Android ([onnxcrepe](https://github.com/yqzhishen/onnxcrepe), [Basic Pitch](https://github.com/spotify/basic-pitch)).
6. **Be kind to beginners: a placement test plus adaptive difficulty instead of star gating.** See the complaints in 1.3. Chord Crush's rating-based adaptation ([Hooktheory](https://www.hooktheory.com/chord-crush)) and Duolingo's placement test ([Duolingo](https://blog.duolingo.com/duolingo-101-how-to-learn-a-language-on-duolingo)) are the models.
7. **Sound quality is an easy win.** Sampled piano with proper note-offs, plus several timbres, avoids the complaints about Trill, Earpeggio and Meludia (1.3) and trains recognition that doesn't depend on timbre.
8. **Android is underserved at the top end.** Chet, Earpeggio and Tenuto are iOS-only ([JustinGuitar](https://community.justinguitar.com/t/ear-training-app-earpeggio/288812)), and ToneGym's Android app has only 100+ installs and a 2.8 rating ([Play](https://play.google.com/store/apps/details?id=com.hk.tonegym&hl=en_US)).
9. **"Bring your own song," analysed on the device.** Chord ai already runs Basic Pitch in a mobile app ([Chord ai](https://chordai.net/)). Automatic transcription isn't reliable enough to be the answer key ([Soundslice](https://www.soundslice.com/transcribe/)), so use it as a hint and a grader. Audio stays on the user's device; get legal review before launch [UNVERIFIED].
10. **Position as the complement to the play-along giants.** Yousician, Simply Piano and flowkey listen but don't teach playing by ear ([HackerNoon](https://hackernoon.com/best-piano-learning-apps-in-2026-an-in-depth-comparison-of-music-education-technology)).
11. **Watch Duolingo.** Its Music course is shallow for musicians ([Rocknerd](https://rocknerd.co.uk/2024/11/12/duolingo-music-review/)), but it is being rebuilt as a game by the NextBeat rhythm-game team ([Q4 letter](https://investors.duolingo.com/static-files/961ce633-3cee-49d0-bd7a-2c63731d45fb)). The "real-world ear" end of the market is where to differentiate.
12. **Pricing whitespace.**
    - Most ear trainers are $5–25 one-time, so revenue per user is low (5.2).
    - Subscription ear trainers charge $75–€149 a year, against an Education median of $9.99/mo and $44.99/yr ([RevenueCat](https://www.revenuecat.com/state-of-subscription-apps/)).
    - Suggestion: a free daily loop plus a Pro tier at about $5–10/mo or $40–60/yr, with an optional lifetime price.
13. **Trial design and policy.** Education trials cluster at 5–9 days, and Duolingo moved to 1-month trials. Hard paywalls convert better at day 35 but make no difference to one-year retention and draw backlash (Trill). Google Play forbids leading with the monthly equivalent of an annual price ([policy](https://support.google.com/googleplay/android-developer/answer/9900533)).
14. **Reminders done right.** Rotate templates with a recency penalty, cap volume, learn each user's practice time, and back off automatically when they stop responding ([KDD paper](https://research.duolingo.com/papers/yancey.kdd20.pdf), [Lenny's Newsletter](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth)). Avoid the guilt-trip tone that users mock ([Debugger](https://debugger.medium.com/duolingo-needs-to-chill-8f1832745ca0)).
15. **Progress that means something (analysis).** Competitors show accuracy per exercise ([MyEarTraining](https://play.google.com/store/apps/details?id=com.myrapps.eartraining&hl=en_US)). An "ear profile" showing which scale degrees, keys, registers and timbres you confuse, plus how quickly you answer, would show readiness for real songs.

---

## URLs opened

**Opened successfully:**
- https://play.google.com/store/apps/details?id=com.evilduck.musiciankit&hl=en_US
- https://apps.apple.com/us/app/perfect-ear-music-rhythm/id1440768353
- https://play.google.com/store/apps/details?id=com.binaryguilt.completeeartrainer&hl=en_US
- https://completeeartrainer.com/
- https://stephanedupont.com/category/complete-ear-trainer/
- https://play.google.com/store/apps/details?id=com.kaizen9.fet.android&hl=en_US
- https://apps.apple.com/us/app/functional-ear-trainer/id1088761926
- https://advancingmusician.com/functional-ear-trainer-app/
- https://www.miles.be/
- https://www.miles.be/software/functional-ear-trainer-v2/
- https://play.google.com/store/apps/details?id=com.myrapps.eartraining&hl=en_US
- https://apps.apple.com/by/app/myeartraining-ear-trainer/id885622580
- https://www.earmaster.com/
- https://www.earmaster.com/support/knowledge-base/frequently-asked-questions/orders-and-subscriptions.html
- https://www.earmaster.com/products/ear-training-sight-singing/earmaster-for-mobile.html
- https://play.google.com/store/apps/details?id=com.earmaster.android&hl=en_US
- https://apps.apple.com/us/app/earmaster-music-theory/id1105030163
- https://apps.apple.com/us/app/tenuto/id459313476
- https://www.musictheory.net/exercises
- https://www.tonegym.co/
- https://www.tonegym.co/shop/index
- https://www.tonegym.co/help/item?id=what-is-the-price-of-tonegym
- https://www.tonegym.co/help/item?id=is-there-a-tonegym-mobile-app
- https://play.google.com/store/apps/details?id=com.hk.tonegym&hl=en_US
- https://play.google.com/store/apps/details?id=com.sonofield.et&hl=en_US
- https://apps.apple.com/us/app/sonofield-ear-trainer/id6740409139
- https://sonofield.com/apps/ear-trainer
- https://sonofield.com/blog/best-ear-training-apps-2026
- https://sonofield.com/blog/apps-to-real-music
- https://meludia.com/en/how-much-does-it-cost-to-subscribe-to-meludia/
- https://meludia.com/en/
- https://apps.apple.com/us/app/meludia-melody-ear-training/id1263474921
- https://apps.apple.com/us/app/chet-ear-training/id1405525467
- https://www.ensemble-education.com/chet (redirected from chetapp.io)
- https://www.teoria.com/en/help/teoria_2021.php
- https://www.teoria.com/en/exercises/
- https://www.risingsoftware.com/auralia
- https://www.risingsoftware.com/first
- https://play.google.com/store/apps/details?id=de.appsolute.betterearspremium&hl=en_US
- https://apps.apple.com/us/app/earpeggio/id884775105
- https://community.justinguitar.com/t/ear-training-app-earpeggio/288812
- https://community.justinguitar.com/t/ear-training-app-help/102726
- https://www.hooktheory.com/pricing
- https://www.hooktheory.com/chord-crush
- https://forum.hooktheory.com/t/introducing-chord-crush-beta-a-chord-progression-ear-trainer-by-hooktheory/4961
- https://solfy.io/en/app
- https://play.google.com/store/apps/details?id=com.app.solfy&hl=en-US
- https://play.google.com/store/apps/details?id=com.xatet.solfyApp&hl=en_US
- https://fifths.io/blog/best-ear-training-apps/
- https://musiciangoods.com/en-us/blogs/music-theory/best-ear-training-apps
- https://www.androidally.com/best-ear-training-apps/
- https://apps.apple.com/app/id6742758221
- https://apps.apple.com/mx/app/jamjam-ear-training/id6747981641
- https://apps.apple.com/mx/app/eartune/id6743134928
- https://play.google.com/store/apps/details?id=com.playbyear.app&hl=en
- https://play.google.com/store/apps/details?id=com.eartraining.ear_training&hl=en-US
- https://apps.apple.com/us/app/-/id1600207375
- https://apps.apple.com/us/app/id1635145345
- https://www.useyourear.com/use-your-ear-app
- https://play.google.com/store/apps/details?id=com.yousician.yousician&hl=en_US
- https://support.yousician.com/hc/en-us/articles/115005189525-Premium-membership-options-in-Yousician
- https://www.guitarchalk.com/yousician-cost/
- https://play.google.com/store/apps/details?id=com.joytunes.simplypiano&hl=en_US
- https://www.pianostartguide.com/simply-piano-review/
- https://en.wikipedia.org/wiki/Simply_(software_company)
- https://help.flowkey.com/en/articles/4466337-which-subscription-plans-are-available
- https://play.google.com/store/apps/details?id=com.flowkey.app&hl=en_GB
- https://help.skoove.com/en/articles/5417607-what-is-skoove-premium-and-how-much-does-it-cost
- https://melodics.com/plans
- https://support.melodics.com/en/articles/8044669-how-to-subscribe-to-melodics
- https://subger.com/en/us/service/melodics
- https://melodics.com/about
- https://support.playgroundsessions.com/hc/en-us/articles/360001017446-How-much-does-a-Playground-Sessions-membership-cost
- https://support.playgroundsessions.com/hc/en-us/articles/40277286306196-How-do-I-get-feedback-and-scores-without-using-a-cable
- https://hackernoon.com/best-piano-learning-apps-in-2026-an-in-depth-comparison-of-music-education-technology
- https://blog.duolingo.com/music-course/
- https://investors.duolingo.com/news-releases/news-release-details/duolingo-launches-music-and-math-its-flagship-app
- https://www.musicradar.com/news/duolingo-music-lessons
- https://blog.duolingo.com/product-highlights/
- https://store.duolingo.com/products/loog-x-duolingo-piano
- https://www.globenewswire.com/news-release/2025/08/06/3128622/0/en/Duolingo-Doubles-Down-on-Delight-with-Acquisition-of-Music-Gaming-Startup-NextBeat-s-Innovative-Team.html
- https://naavik.co/digest/duolingos-next-beat/
- https://rocknerd.co.uk/2024/11/12/duolingo-music-review/
- https://duoplanet.com/duolingo-music/
- https://lingoly.io/duolingo-for-music/
- https://duocon.duolingo.com/
- https://www.globenewswire.com/news-release/2025/09/16/3150882/0/en/Duolingo-Unveils-Major-Product-Updates-that-Turn-Learning-into-Real-World-Power-at-Duocon-2025.html
- https://www.sec.gov/Archives/edgar/data/1562088/000162828026053299/q2fy26duolingo6-30x26share.htm
- https://www.sec.gov/Archives/edgar/data/1562088/000162828026029790/q1fy26duolingo3-31x26share.htm
- https://investors.duolingo.com/static-files/961ce633-3cee-49d0-bd7a-2c63731d45fb
- https://investors.duolingo.com/news-releases/news-release-details/duolingo-reports-fourth-quarter-and-full-year-2025-results
- https://www.fool.com/earnings/call-transcripts/2026/05/04/duolingo-duol-q4-2025-earnings-transcript/
- https://finance.biggo.com/news/US_DUOL_2026-08-05
- https://www.fool.com/investing/2026/09/14/the-green-owl-is-ending-duolingos-own-cash-cow/
- https://www.tikr.com/blog/duolingo-nasdaq-duol-stock-slides-14-following-disappointing-2026-revenue-outlook
- https://en.wikipedia.org/wiki/Duolingo
- https://blog.duolingo.com/duolingo-energy/
- https://www.androidauthority.com/quitting-duolingo-energy-system-3599842/
- https://www.prdaily.com/the-scoop-duolingo-ceo-walks-back-ai-first-memo/
- https://it.slashdot.org/story/25/06/28/2036249/duolingo-stock-plummets-after-slowing-user-growth-possibly-caused-by-ai-first-backlash
- https://research.duolingo.com/papers/yancey.kdd20.pdf
- https://blog.duolingo.com/hi-its-duo-the-ai-behind-the-meme/
- https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth
- https://debugger.medium.com/duolingo-needs-to-chill-8f1832745ca0
- https://tinomwadeyi.substack.com/p/how-duolingo-perfected-the-art-of
- https://sherwood.news/tech/duolingo-q2-earnings-monthly-active-users-milestone/
- https://www.digia.tech/post/duolingo-habit-forming-reminders-retention-architecture/
- https://blog.duolingo.com/product-lessons-friend-streak/
- https://blog.duolingo.com/duolingo-101-how-to-learn-a-language-on-duolingo
- https://duolingo.deconstructoroffun.com/mechanics/streaks
- https://www.dealnews.com/features/duolingo/cost/
- https://languageappguide.com/pricing/duolingo-cost/
- https://play.google.com/store/apps/details?id=ai.moises&hl=en_US
- https://stemsplit.io/blog/moises-ai-review
- https://moises.ai/newsroom/company-milestones/2025-year-in-review/
- https://www.musicbusinessworldwide.com/music-ai-raises-40m-in-series-a-round-as-its-moises-platform-hits-50m-users/
- https://play.google.com/store/apps/details?id=net.chordify.chordify&hl=en_US
- https://support.chordify.net/hc/en-us/articles/360002273238-What-are-the-subscription-options
- https://www.guitarchalk.com/chordify-review/
- https://play.google.com/store/apps/details?id=com.chordai&hl=en_US
- https://chordai.net/
- https://www.seventhstring.com/xscribe/overview.html
- https://www.seventhstring.com/xscribe/buy.html
- https://play.google.com/store/apps/details?id=app.anytune.musicplayer&hl=en_US
- https://www.soundslice.com/plans/
- https://www.soundslice.com/transcribe/
- https://help.ultimate-guitar.com/en/articles/6741560-what-is-ultimate-guitar-pro-subscription
- https://www.guitarchalk.com/ultimate-guitar-pro-review/
- https://www.songscription.ai/blog/best-music-transcription-software-2026
- https://github.com/spotify/basic-pitch
- https://engineering.atspotify.com/2022/06/meet-basic-pitch
- https://github.com/facebookresearch/demucs
- https://github.com/sevagh/demucs.onnx
- https://github.com/marl/crepe
- https://github.com/yqzhishen/onnxcrepe
- https://librosa.org/doc/main/generated/librosa.pyin.html
- https://github.com/librosa/librosa
- https://github.com/JorenSix/TarsosDSP
- https://github.com/SonyCSLParis/pesto
- https://www.tensorflow.org/hub/tutorials/spice
- https://www.kaggle.com/models/google/spice
- https://research.google/blog/spice-self-supervised-pitch-estimation/
- https://onnxruntime.ai/docs/tutorials/mobile/
- https://github.com/microsoft/onnxruntime
- https://github.com/google-ai-edge/LiteRT
- https://www.revenuecat.com/state-of-subscription-apps-2025/
- https://www.revenuecat.com/state-of-subscription-apps/
- https://developer.android.com/google/play/billing/lifecycle/subscriptions
- https://developer.android.com/google/play/billing/subscriptions
- https://support.google.com/googleplay/android-developer/answer/12154973
- https://support.google.com/googleplay/android-developer/answer/9900533
- https://support.google.com/googleplay/android-developer/answer/140504
- https://support.google.com/googleplay/android-developer/answer/112622
- https://en.wikipedia.org/wiki/Yousician
- https://www.businessofapps.com/data/education-app-market/

**Opened, but the page had no usable content:**
- https://www.grandviewresearch.com/industry-analysis/online-music-education-market-report
- https://account.yousician.com/plans (needs JavaScript)
- https://forum.pianoworld.com/ubbthreads.php/topics/3208578/ear-training-programs-vs-songs.html (needs JavaScript)
- https://www.talkbass.com/threads/functional-ear-trainer-app-help.1286745/
- https://www.teoria.com/en/exercises/ie.php
- https://en.wikipedia.org/wiki/Melodics (wrong article)

**Blocked or failed:**
- old.reddit.com (site blocked)
- perfectear.app (robots.txt)
- earbeater.com (TLS error)
- apps.apple.com EarBeater pages id724557406 and id762493165 (404)
- Meludia Melody on Google Play (404)
- solfy.music and pianopassionblog.com (DNS)
- duolingo.fandom.com (402)
- classcentral.com (403)
- forum.duome.eu (404)
- hooktheory.com/earTraining (404)
- code.soundsoftware.ac.uk (robots.txt)
- allabolag.se and proff.se (403)
- Google Play store search (robots.txt)
- Guessed Play IDs for Skoove and Melodics (404)
