<!-- Research report gathered on 24 Sep 2026 for docs/PLAN.md. Every claim links to the page that was opened; items marked unverified could not be confirmed. -->

# Learning and motivation science for a daily ear-training app

**How this was done.** Every claim below comes from a page I opened with WebFetch; all of them are listed in section 8. WebFetch returns a model-written extract of each page, so the numbers came through that layer. I fetched the key tables (for example half-life regression) twice. PubMed pages were blocked by a CAPTCHA, so I read abstracts from publisher pages instead. **[unverified]** marks content from a third-party transcript, a press release or a title only. **[inference]** marks my own reasoning. I ran out of web searches near the end, so some gaps remain; each one is marked **[gap]**.

---

## 1. Optimal difficulty

- **What the 85% Rule actually claims.** It applies to learners that improve by stochastic gradient descent, doing two-choice (binary) classification with Gaussian noise. For those learners, the training error rate that maximizes learning is about 15.87%, which is about 85% accuracy. Other noise shapes give other optima: about 82% (Laplacian) and about 75% (Cauchy). Training at that fixed error rate makes precision grow like √t, against √(log t) at a fixed difficulty. The authors call this "exponential improvements in the rate of learning." They only showed it in simulations: a perceptron, a two-layer network on MNIST, and the Law & Gold model of perceptual learning. ([Wilson et al. 2019](https://www.nature.com/articles/s41467-019-12552-4))
- **Its limits, in the authors' own words.** It is restricted to binary classification and gradient-descent learners. They say no formal work has shown it applies to humans. They present the links to the zone of proximal development (ZPD), desirable difficulty and flow as suggestive only. They also note that batch training changes the optimum, and that a Bayesian learner with perfect memory would gain nothing from ordering by difficulty. ([Wilson et al. 2019](https://www.nature.com/articles/s41467-019-12552-4))
- **Later human data does not point to a single number.**
  - **Motor learning.** In reward-based motor learning, a schedule targeting 50% success produced *more* learning than one targeting 80% (achieved rates 49% vs 72%). Final-block learning was 0.52 vs 0.40, and reported motivation was the same (3.8/5). ([van der Kooij et al., Sci Rep 2026](https://www.nature.com/articles/s41598-026-39639-5))
  - **Online math game.** Across 10K- and 70K-player experiments, "the easier the game, the longer people played": +34% estimates and +18% time with larger targets, with success rates of 29–63%. But "the most engaging design conditions produced the slowest rates of learning." ([Lomas et al., CHI 2013](https://www.academia.edu/7000722/Optimizing_Challenge_in_an_Online_Learning_Game_Using_Large_Scale_Experiments))
  - **Follow-up by the same group.** When difficulty was assigned at random, the easiest games were the most motivating. When players chose, an inverted U appeared, driven by avoiding levels labeled "very easy." A moderate amount of novelty was best. Their summary: "not too hard, not too boring." ([Lomas et al., CHI 2017](https://www.researchgate.net/publication/313477156_Is_Difficulty_Overrated_The_Effects_of_Choice_Novelty_and_Suspense_on_Intrinsic_Motivation_in_Educational_Games))
  - **Math Garden children** (N=207, 6 weeks): the higher the preset success rate, the more problems they attempted and the larger their performance gain. Math anxiety improved equally in all conditions. The abstract does not give the exact rates. ([Jansen et al. 2013](https://eric.ed.gov/?id=EJ1008332&pg=4&q=Math+AND+anxiety))
- **ZPD and flow arguments.**
  - The ZPD is the space between what a learner can do alone and what they cannot do even with help; scaffolding is the support that lets them work inside it. ([Wikipedia: ZPD](https://en.wikipedia.org/wiki/Zone_of_proximal_development))
  - Flow needs clear goals, immediate feedback, and challenge matched to skill. Too much challenge gives anxiety; too little gives boredom. ([Wikipedia: Flow](https://en.wikipedia.org/wiki/Flow_(psychology)))
  - Self-determination theory (SDT) lists "optimal challenges, effectance-promoting feedback" among the conditions that raise intrinsic motivation. ([Ryan & Deci 2000](https://selfdeterminationtheory.org/SDT/documents/2000_RyanDeci_SDT.pdf))
  - The two definitions come from encyclopedia pages, not primary sources.
- **Staircase procedures.** Levitt's transformed up-down rules converge on fixed success rates:
  - 1-up/2-down (harder after two correct, easier after one wrong) converges on 70.7%.
  - 1-up/3-down converges on 79.4%.
  - Other response groupings converge on 84.1%.
  - Source: ([Levitt 1971](http://bdml.stanford.edu/twiki/pub/Haptics/DetectionThreshold/psychoacoustics.pdf)). Wilson et al. note that psychophysics staircases are "commonly designed to produce about 80–85% accuracy." ([Wilson et al. 2019](https://www.nature.com/articles/s41467-019-12552-4))
- **In perceptual learning, the mix of easy and hard trials matters more than one target number.**
  - Easy training transfers broadly; hard training stays specific to the trained stimulus; easy conditions "guide the learning of hard ones" (the "eureka" effect). ([Ahissar & Hochstein 1997](https://www.nature.com/articles/387401a0))
  - Training only at low accuracy without feedback gave no learning. Mixing in high-accuracy trials gave learning comparable to trial-by-trial feedback. ([Liu, Lu & Dosher 2012](https://www.sciencedirect.com/science/article/pii/S004269891100410X))
  - In auditory training (time-compressed speech), adaptive protocols generalized better than fixed ones, and starting easy beat starting hard. ([Gabay, Karni & Banai 2017](https://journals.plos.org/plosone/article?id=10.1371%2Fjournal.pone.0176488))
- **Takeaway [inference].** No human optimum has been validated. The evidence supports:
  - adaptive difficulty around 70–85% success,
  - an easy-first ramp,
  - easy items mixed in throughout,
  - knowing that the difficulty that maximizes engagement is easier than the one that maximizes learning.

## 2. Spacing, interleaving, retrieval, desirable difficulties, feedback — and whether they hold for musical skills

- **Review verdicts (mostly verbal and academic material).** Practice testing and distributed practice rated "high utility." Interleaved practice rated "moderate." Rereading, highlighting and summarization rated "low." ([Dunlosky et al. 2013](https://www.psychologicalscience.org/publications/journals/pspi/learning-techniques.html))
- **Spacing.**
  - A meta-analysis of 839 assessments, 317 experiments and 184 articles: spaced beats massed, and the best gap between study sessions grows with how long you need to remember. It covers *verbal recall only*. ([Cepeda et al. 2006](https://www.yorku.ca/ncepeda/publications/CPVWR2006.html))
  - With more than 1,350 participants, the best gap was about 20% of the test delay for delays of a few weeks, and about 5% for a one-year delay. ([Cepeda et al. 2008](https://escholarship.org/uc/item/0kp5q19x))
- **Interleaving.**
  - Interleaving helps learners tell similar concepts apart. The evidence is limited, but "at least a portion of the exposures should be interleaved." ([Rohrer 2012](https://link.springer.com/article/10.1007/s10648-012-9201-3))
  - Meta-analysis (59 papers, 238 effects, N=8,466): overall g=0.42. By material: paintings 0.67, photos 0.35, math 0.34, texts not significant, word lists −0.39 (blocking was better). The benefit grows when the categories resemble each other. **No auditory or music studies were included.** ([Brunmair & Richter 2019](https://www.psychologie.uni-wuerzburg.de/fileadmin/06020400/2019/Brunmair_Richter_in_press__2019_META-ANALYSIS_OF_INTERLEAVED_LEARNING.pdf))
- **Desirable difficulties.** Spacing, interleaving, testing and varied conditions make performance during training worse but retention better. They become *undesirable* "if the learner does not have the background knowledge or skills to respond to them successfully." Learners misjudge blocked practice as better, for example when learning painters' styles. ([Bjork & Bjork 2011](https://bjorklab.psych.ucla.edu/wp-content/uploads/sites/13/2016/04/EBjork_RBjork_2011.pdf))
- **Retrieval practice.**
  - Prose passages: testing beat restudying at 2 days (68% vs 54%) and 1 week (56% vs 42%), though restudying won at 5 minutes (81% vs 75%). Restudiers were more confident. ([Roediger & Karpicke 2006](https://www.hendrix.edu/uploadedFiles/Academics/Faculty_Resources/Faculty_Development_Newsletters/Testing%20effect.pdf))
  - It also holds for perceptual categories: testing with feedback improved classification of new bird examples (53% vs 45%). ([Jacoby et al. 2010](https://web.colby.edu/memoryandlanguagelab/files/2012/07/Jacoby-Wahlheim-Coane-2010-JEPLMC.pdf))
- **Feedback timing and form.**
  - Meta-analysis of computer-based learning (51 studies, 160 effects): immediate vs delayed feedback makes no average difference (g=0.03, not significant). The effect depends on context, and few studies delayed feedback by a day or more. ([Kandemir et al. 2026](https://link.springer.com/article/10.1007/s10648-026-10117-8))
  - One study found an advantage for feedback delayed 2 days on complex concepts. ([Corral et al. 2021](https://journals.sagepub.com/doi/10.1177/1747021820977739))
  - Shute's review: immediate feedback for difficult or new tasks, weaker learners and procedural skill; delayed feedback may help simple tasks, strong learners and transfer. Avoid praise and comparisons with others. ([Shute](https://myweb.fsu.edu/vshute/pdf/shute%202007_f.pdf))
  - Across 607 effects, the average benefit was d=.41, but more than a third of feedback interventions *reduced* performance. Effectiveness falls as feedback draws attention to the self rather than the task. ([Kluger & DeNisi 1996](https://cris.huji.ac.il/en/publications/the-effects-of-feedback-interventions-on-performance-a-historical/))
  - Motor skill: feedback on 50% of trials, faded over time, beat feedback on every trial at 24-hour retention (35% less error). This is the "guidance hypothesis": learners come to depend on feedback that is always there. ([Winstein & Schmidt 1990](https://www.krigolsonteaching.com/uploads/4/3/8/4/43848243/reduced_frequency_of_kr_1990_winstein_schmidt.pdf))
- **Evidence specific to music and hearing.**
  - **Piano melodies.** Random-order practice was worse during learning but better 2 days later. Learners, including trained musicians, wrongly believed fixed order was better and preferred it. ([Abushanab & Bishara 2013](https://link.springer.com/article/10.3758/s13421-013-0311-z))
  - **Clarinet.** With 10 advanced clarinetists, whenever raters saw a difference, the interleaved pieces were rated better; still, 6 of 10 preferred blocked practice. The sample is small. ([Carter & Grahn 2016](https://www.frontiersin.org/journals/psychology/articles/10.3389/fpsyg.2016.01251/full))
  - **Musical intervals.** For telling a perfect fourth apart from nearby intervals (1,080 trials over 3 days):
    - continuous practice alone produced **no** learning;
    - practice alternated with periods of just hearing the sounds went from 68.9% to 88.2%, and transferred to identifying intervals;
    - practice alternated with silence did not work.

    ([Little, Cheng & Wright 2019](https://link.springer.com/article/10.3758/s13414-018-1584-x))
  - **Daily amount.** One study found pitch (frequency) discrimination needed more than 360 trials per day to improve ([Wright & Sabin 2007](https://link.springer.com/article/10.1007/s00221-007-0898-z)). Another pitch study found 100 trials per day (about 8 minutes) gave *faster* early learning than 200–800 trials per day, attributed to consolidation between sessions ([Molloy et al. 2012](https://journals.plos.org/plosone/article?id=10.1371%2Fjournal.pone.0036929)). The two conflict. Treat the daily amount as something to tune.
- **Bottom line [inference].** Replaying a melody from memory is already retrieval practice. Interleaving confusable categories and spacing are likely helpful for hearing skills but less proven. The strongest ear-training findings are:
  - combine active trials with passive listening;
  - sessions can be short, but the daily amount needs tuning;
  - learners' own preferences push them toward blocked practice, which retains worse.
  - **[gap]** I found no meta-analytic evidence on interleaving auditory categories.

## 3. Adaptive models used by learning apps

- **Half-life regression (HLR — Duolingo, Settles & Meeder 2016).**
  - *What it models:* per-word memory. Recall probability p = 2^(−Δ/h), where Δ is time since last practice; half-life h = 2^(θ·x), where x is a feature vector.
  - *Data needed:* time since last practice, counts of exposures, correct and incorrect answers, plus optional ~20k word-tag features. It was trained on 12.9M practice traces.
  - *Results:* mean absolute error 0.128 vs 0.235 for the Leitner box system, i.e. "45%+" lower error.
  - *Live tests:* against Leitner (about 1M users, 6 weeks), daily activity +0.3% (not significant) and practice sessions −7.3%. Dropping the overfit word-tag features gave +12.0% daily activity; that is the source of the "12% engagement" in the abstract.
  - *Ease:* very simple, a single weighted sum. Sources: ([paper](https://research.duolingo.com/papers/settles.acl16.pdf)); code and 13M traces are MIT-licensed ([GitHub](https://github.com/duolingo/halflife-regression)).
  - *Caveat:* on independent Anki data, HLR separates remembered from forgotten items poorly (AUC 0.633). ([benchmark](https://expertium.github.io/Benchmark.html))
- **Birdbrain (Duolingo, 2020–2023).**
  - *V1:* logistic regression "inspired by item response theory" (IRT, the psychometric model of ability vs item difficulty). An exercise's difficulty is the sum of its parts (exercise type, words and so on). Each answer triggers one gradient step. A wrong answer lowers the learner's ability and raises the exercise's difficulty, like an Elo update.
  - *V2:* a recurrent (LSTM) network that compresses a learner's history into a 40-number vector.
  - *Use:* the session generator picks exercises "neither too easy nor too hard." Scale is about 1B exercises per day ([IEEE Spectrum 2023](https://spectrum.ieee.org/duolingo)). Over 20% of lessons were personalized by October 2020 ([Duolingo blog 2020](https://blog.duolingo.com/learning-how-to-help-you-learn-introducing-birdbrain)).
  - *Not published:* a target success rate.
  - *Ease:* V1 is simple; V2 needs data at Duolingo's scale.
- **Elo / Math Garden.**
  - An Elo-derived model that scores both accuracy and response time. Ability and item difficulty are both updated after every answer, and items are chosen for a **0.75 success probability**. 3,648 children solved 3.5M problems, with good reliability and satisfaction. ([Klinkenberg et al. 2011](https://eric.ed.gov/?id=EJ925823); [author page](https://www.klinkenberg.amsterdam/publication/math-garden/))
  - A review calls Elo "simple, robust, and effective" for adaptive learning systems. ([Pelánek 2016](https://www.sciencedirect.com/science/article/abs/pii/S036013151630080X))
  - *Newer update step:* a trend-based dynamic K (K = |T|, with T an exponentially smoothed sign of the prediction errors). At equal error it converged faster than a fixed K (35.6 vs 44.6 iterations) and followed sudden ability jumps. It is deployed in Math Garden. ([Vermeiren et al. 2025](https://link.springer.com/article/10.1007/s11257-025-09439-z))
  - *Data needed:* outcomes only. *Ease:* about a dozen lines of code.
- **Glicko-2 / Lichess puzzles.**
  - Puzzles use the same Glicko-2 rating as games: each attempt is a match, and puzzle ratings are learned from about 3M attempts per day. Lichess says "calibrating difficulty is the job of the rating system." Themed puzzles pay fewer points because the theme is a hint. ([Lichess blog 2020](https://lichess.org/@/lichess/blog/new-puzzles-are-here/X-S6gRUA))
  - Glicko-2 adds volatility on top of Elo, and new players start at 1500. ([Lichess rating systems](https://lichess.org/page/rating-systems))
  - The difficulty presets are rating offsets relative to the player: Easiest −600, Easier −300, Normal 0, Harder +300, Hardest +600 ([lila source](https://github.com/lichess-org/lila/blob/master/modules/puzzle/src/main/PuzzleDifficulty.scala)). **[inference]** On the standard Elo curve, and ignoring rating uncertainty, "Normal" means about 50% expected success and "Easier" about 85%.
- **Bayesian Knowledge Tracing (BKT).**
  - A hidden Markov model per skill (known or not known); the tutor gives exercises "until the student has 'mastered' each rule." ([Corbett & Anderson 1994](https://link.springer.com/article/10.1007/BF01099821))
  - Four parameters: prior knowledge P(L0), learning P(T), guess P(G), slip P(S). Observations are binary. ([Wikipedia: BKT](https://en.wikipedia.org/wiki/Bayesian_knowledge_tracing)) The standard set has no forgetting term.
  - *Ease:* updates are trivial; fitting the parameters needs data from many learners. It is a mastery model, not a difficulty picker.
- **FSRS (the scheduler in Anki).**
  - *What it models:* Retrievability (probability of recall), Stability (days for recall to fall from 100% to 90%) and Difficulty. FSRS-6 has 21 parameters. Defaults were fit on several hundred million reviews from about 10k users. A per-user optimizer fits the parameters. The user sets a desired retention (70–97% recommended). ([ABC of FSRS](https://github.com/open-spaced-repetition/awesome-fsrs/wiki/ABC-of-FSRS))
  - In Anki since 23.10; needs "fewer reviews than … SM-2" for the same retention. ([Anki FAQ](https://faqs.ankiweb.net/what-spaced-repetition-algorithm))
  - *Benchmark* (about 350M reviews): FSRS-6 beats SM-2 for 99.6% of users; a neural network (RWKV) beats FSRS-6. ([benchmark](https://expertium.github.io/Benchmark.html))
  - *Libraries:* TypeScript, Go, Rust, Swift, Dart, Python and others, plus C/C++ and Java bindings. **No Kotlin port is listed.** ([GitHub org](https://github.com/open-spaced-repetition))
  - *Data needed:* a review log per card with grade and timestamp. Best suited to separate memorizable cards.
- **Learning-progress bandits (ZPDES).** Picks activities by measured learning progress, using a multi-armed bandit (an algorithm that balances trying options against exploiting the best one). It needs little up-front modelling of the student and was tested with 400 schoolchildren. I did not extract the quantitative results. ([Clement et al. 2015](https://jedm.educationaldatamining.org/index.php/JEDM/article/view/JEDM111); [arXiv](https://arxiv.org/abs/1310.3174))

## 4. Streaks and habit

- **Duolingo's own streak results (company blog).**
  - **Streak made to need one lesson (Nov 2020).** The streak was separated from the daily XP goal; one lesson now extends it. Results: day-14 retention +3.3%, daily active users +1%, learners on 20-day streaks +10.5%, new learners on streaks +19%. Their conclusion: "lowering the barriers to building a consistent daily habit is more important … than how much you learn each day." ([Anton Yu](https://blog.duolingo.com/improving-the-streak))
  - **Animations and freezes (Jan 2022).** Milestone animations gave +1.7% 7-day retention among new learners. Allowing 2 equipped streak freezes instead of 1 gave +0.38% daily active users. The post cites loss aversion and says "slack" motivates more than rigid rules. ([Osman Mansur](https://blog.duolingo.com/how-duolingo-streak-builds-habit))
  - **Friend Streak (Sep 2024).** Shared with up to 5 friends. Learners with at least one are 22% more likely to finish their daily lesson; this is observational. ([blog](https://blog.duolingo.com/product-lessons-friend-streak/))
- **Interview with Duolingo's streak product lead (Jackson Shuttleworth).**
  - The episode page lists more than 600 streak experiments. ([Lenny's show page](https://www.lennysnewsletter.com/p/behind-the-product-duolingo-streaks))
  - The following come from a **third-party transcript [unverified]** ([transcript](https://github.com/ChatPRD/lennys-podcast-transcripts/blob/main/episodes/jackson-shuttleworth/transcript.md)):
    - over 9M users have a streak longer than a year;
    - giving 2 streak freezes at the start of a new streak was "one of our biggest wins";
    - "Earn Back" restores a lost streak after a few lessons done within a time window;
    - changing a button from "Continue" to "Commit to my goal" was "a massive win";
    - users with a 30-day streak are about 7× more likely to finish the course.
- **Hearts → Energy: verified, it happened in 2025.**
  - Q2 2025 shareholder letter: "Energy is a pacing system for free users that replaces Hearts. Unlike Hearts, which penalize mistakes, Energy is usage-based and rewards success." On iOS it raised daily active users, median learning time and paid conversion. ([Q2 2025 letter](https://investors.duolingo.com/static-files/0b55110c-2eb9-466d-8549-5459e0851290))
  - Earnings call, 6 Aug 2025: learners start with 25 units and spend 1 per exercise, right or wrong. More than half of iOS daily users had switched; fewer than half on Android; completion expected in "a couple of months." ([Motley Fool transcript](https://www.fool.com/earnings/call-transcripts/2025/08/06/duolingo-duol-q2-2025-earnings-call-transcript/)) Whether the rollout finished is **[unverified]**.
- **Jorge Mazal, "How Duolingo reignited user growth" (Lenny's Newsletter, 2023).** ([article](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth))
  - A model of user states: new, current, reactivated, resurrected, at-risk, dormant.
  - The current-user retention rate (CURR: users active today and on at least one of the previous 6 days) had "5 times the impact" on daily active users of the next-best metric. CURR rose 21%, meaning daily churn among the best users fell by more than 40%.
  - Daily active users grew 4.5× over about 4 years. The share of daily users with a 7+ day streak nearly tripled, to more than half.
  - Leaderboards raised learning time 17% and tripled "highly engaged" learners.
  - A late-night streak-saver notification showed there was upside in streak work.
  - They set a rule to "protect the channel": no increase in notification volume.
  - Copying the move counter from the game Gardenscapes was "completely neutral."
- **Independent research on streaks and resets.**
  - Showing *intact* streaks in an activity log increases later engagement compared with showing *broken* ones, regardless of actual past behavior. The effect is stronger when people blame themselves for the break, and weaker when the streak can be "repaired" by resuming. ([Silverman & Barasch, JCR 2023](https://academic.oup.com/jcr/article-abstract/49/6/1095/6623414))
  - Press summary: people shift from their original goal to keeping the streak itself; apps might offer alternate ways to keep a streak going; "what counts as a streak is malleable." It mentions a follow-up, "Hot streak!" (OBHDP 2023); its content is **[unverified]**. ([phys.org](https://phys.org/news/2024-03-streaks.html))
- **Emergency reserves and flexibility (Milkman and colleagues).**
  - Goals with built-in slack ("emergency reserves," a few allowed misses) raised persistence after failure. In a field study with step goals, reserves met the goal 40% more days per week than a hard goal. Persistence after a first failure was .60 vs .38. A reserve used *after* a failure beat one planned *in advance* (78.2% vs 54.7%). ([Sharif & Shu, OBHDP 2021](https://anderson-review.ucla.edu/wp-content/uploads/2021/03/Sharif-Shu_EmergencyReserveFailure_OBHDP2019.pdf); [CHIBE](https://chibe.upenn.edu/publications/nudging-persistence-after-failure-through-emergency-reserves/))
  - A megastudy of more than 60,000 gym members tested 53 programs. The best included bonus rewards for **returning after a missed workout**. ([NIH summary](https://www.nih.gov/news-events/nih-research-matters/testing-ways-encourage-exercise))
  - Paying for gym visits only in a fixed daily 2-hour window produced fewer visits than flexible payment, both during and after the program. ([Beshears, Lee, Milkman et al. 2021](https://ideas.repec.org/a/inm/ormnsc/v67y2021i7p4139-4171.html))
  - **[gap]** I found no Milkman paper on app streaks specifically.
- **How long habits take, and self-determination theory.**
  - **Habit timeline.** In 82 people, the median time to reach 95% of the automaticity plateau was 66 days (range 18–254). Missing one day "did not materially affect" habit formation. Exercise took about 91 days, eating and drinking habits 59–65. ([Lally et al. 2010](https://www.academia.edu/2475072/How_are_habits_formed_Modelling_habit_formation_in_the_real_world))
  - **SDT basics.** People need competence, autonomy and relatedness. Expected tangible rewards tied to performance undermine intrinsic motivation. Competence raises intrinsic motivation only when paired with autonomy. ([Ryan & Deci 2000](https://selfdeterminationtheory.org/SDT/documents/2000_RyanDeci_SDT.pdf))
  - **Rewards meta-analysis.** Tangible rewards lowered intrinsic motivation (d = −0.40 / −0.36 / −0.28, depending on what the reward was tied to). Positive feedback raised it (+0.33, college students only). ([Deci, Koestner & Ryan 1999](https://home.ubalt.edu/tmitch/642/articles%20syllabus/Deci%20Koestner%20Ryan%20meta%20IM%20psy%20bull%2099.pdf))
  - **Games.** Autonomy, competence and relatedness predict game enjoyment and future play. ([Ryan, Rigby & Przybylski 2006](https://link.springer.com/article/10.1007/s11031-006-9051-8))
  - **Gamification experiments.** Badges, leaderboards and performance graphs raised feelings of competence; no element raised feelings of free choice. ([Sailer et al. 2017](https://www.academia.edu/34911167/How_gamification_motivates_An_experimental_study_of_the_effects_of_specific_game_design_elements_on_psychological_need_satisfaction))
  - **A backfire case.** A gamified course with a leaderboard and badges *lowered* motivation, satisfaction and final-exam scores. ([Hanus & Fox 2015](https://www.academia.edu/12454798/Assessing_the_Effects_of_Gamification_in_the_Classroom_A_Longitudinal_Study_on_Intrinsic_Motivation_Social_Comparison_Satisfaction_Effort_and_Academic_Performance))
  - **Overall effect size.** Gamification meta-analysis: g = 0.49 on learning, 0.36 on motivation, 0.25 on behavior; the last two are less stable. ([Sailer & Homner 2020](https://eric.ed.gov/?id=EJ1245270))

## 5. Notifications

- **Duolingo's notification bandit (Yancey & Settles 2020).**
  - The algorithm chooses which reminder template to send. "Sleeping" means some templates only apply to some users. "Recovering" means templates seen recently lose impact; reusing a template cost 0.5% of the reward.
  - Reward = a lesson completed within 2 hours of the reminder. It optimizes millions of reminders per day for 300M+ users.
  - Live test: daily active users +0.5%, lessons +0.4%, new-user day-1 retention +2.2%, day-7 retention +2.0%.
  - Source: ([KDD 2020](https://research.duolingo.com/papers/yancey.kdd20.pdf))
- **Duolingo's timing and volume.**
  - The practice reminder goes "23 and a half hours after you practice the day before." A "last chance" streak saver goes at 10 pm if the streak isn't extended. Both are from the **third-party transcript [unverified]**. ([transcript](https://github.com/ChatPRD/lennys-podcast-transcripts/blob/main/episodes/jackson-shuttleworth/transcript.md))
  - Mazal: optimize timing and copy but never add volume, because users who opt out "remain opted out forever." ([Lenny's](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth))
- **Home-screen widget (observational).** Half of widget users have a streak of at least 6 months. Retention was better even after controlling for commitment. Users said it was "just as effective as" push notifications. ([Duolingo blog](https://blog.duolingo.com/widget-feature))
- **Randomized evidence on prompts.**
  - **Workplace well-being app.** A micro-randomized trial (each notification randomly sent or withheld) with 1,255 users over 89 days: a notification made engagement in the next 24 hours 3.9% more likely. The effect was 8.7% on weekends vs 2.5% on weekdays (the weekday effect was not significant), and largest at 12:30 pm on weekends (11.8%). ([Bidargaddi et al. 2018](https://researchnow-admin.flinders.edu.au/ws/portalfiles/portal/31737554/document_2_.pdf))
  - **HeartSteps (step counts).** Suggestions raised 30-minute step counts 14% on average. On day 1 the effect was +66%; it fell about 2% per day and was undetectable by day 28. Prompts wear off. ([Klasnja et al. 2019](https://academic.oup.com/abm/article/53/6/573/5091257))
- **Reminders vs habits.**
  - Reminders "supported repetition but hindered habit development"; cues tied to an existing event raised automaticity; positive reinforcement did not help. ([Stawarz et al. 2015](https://discovery.ucl.ac.uk/1468224/))
  - Habit-app users became dependent on the app; their behaviors "collapsed" when they stopped using it. ([Renfree et al. 2016](https://discovery.ucl.ac.uk/id/eprint/1477627/1/Chi%202016%20LBW%202.1%20camera%20ready.pdf))
  - If-then plans ("if situation Y, then I do X") had an average effect of d=.65 across 94 tests. ([Gollwitzer & Sheeran 2006](https://kops.uni-konstanz.de/handle/123456789/10973))
- **Timing at the user's habitual time — [gap].** I found no controlled study comparing reminders at a learner's usual time against fixed times. Duolingo's 23.5-hour rule is company practice, not a published experiment as far as I could verify. What *is* supported:
  - time of day and weekday matter (Bidargaddi);
  - novel copy matters (Yancey & Settles);
  - effects wear off (HeartSteps);
  - cues tied to existing routines build automaticity (Stawarz; [Lally](https://www.academia.edu/2475072/How_are_habits_formed_Modelling_habit_formation_in_the_real_world)).

## 6. Serious products whose progress feels earned

- **Anki.**
  - Statistics include: a forecast of future reviews, a calendar heatmap, review counts and time, card counts, intervals, FSRS memory measures, success by hour of day ("percentage … passed during given hours"), answer-button use, and a **True Retention** table. ([Anki manual](https://docs.ankiweb.net/stats.html))
  - The user chooses their target retention. ([Anki FAQ](https://faqs.ankiweb.net/what-spaced-repetition-algorithm))
  - Michael Nielsen: 15–20 minutes a day; "Anki makes memory a *choice*"; about 4–7 minutes of total review per card over 20 years. ([Nielsen essay](http://augmentingcognition.com/ltm.html))
- **Lichess.**
  - The headline number is a Glicko-2 rating calibrated against puzzles whose own ratings are learned from attempts. Users are told not to downvote puzzles for being too easy or too hard. ([blog](https://lichess.org/@/lichess/blog/new-puzzles-are-here/X-S6gRUA))
  - The dashboard shows the 3 strongest and 3 weakest themes, each with performance rating, solve % and count. Tapping a theme starts practice on it. The mobile version was merged in Feb 2026, mirroring the web. ([mobile PR](https://github.com/lichess-org/mobile/pull/2651))
- **Chess.com.**
  - Oct 2025: it dropped an inflated puzzle rating (minimum +5 per solve, partial credit) because it didn't reflect real strength. The new Glicko-style rating makes a 1200 player equal a 1200 puzzle. Difficulty presets: Standard ("challenging, but you should get most of them right"), Hard, Extra Hard. The new system was checked against about 17B historical attempts. ([Chess.com news](https://www.chess.com/news/view/announcing-new-puzzles-rating-system))
  - Engagement rewards now live in a separate "Puzzle Points" system with tiers, daily bonuses and streak and speed bonuses. ([help article](https://support.chess.com/en/articles/9681952-what-are-puzzle-points-on-chess-com))
- **Strava.** Fitness & Freshness uses an impulse-response model of training load. It shows Fitness, Fatigue, and Form = Fitness − Fatigue. Strava says "overall numbers aren't as important as general trends" and suggests comparing with your own past. ([Strava help](https://support.strava.com/hc/en-us/articles/216918477-Fitness-Freshness))
- **WHOOP.** Strain runs 0–21 on a logarithmic scale; 21 is practically unreachable. A daily Strain Target is set from that morning's Recovery score (sleep, heart-rate variability, resting heart rate), aiming to "avoid overtraining while still progressing." ([WHOOP](https://www.whoop.com/us/en/thelocker/strain-coach/))
- **Brilliant** is not free of game mechanics.
  - It has streaks (3 problems or one lesson per day), automatic "Streak Charges" that protect the streak, and weekly XP leagues of 30 learners across 10 levels. ([help](https://brilliant.org/help/features/))
  - Its own stated difference is interactive problem solving: "develop intuition through interaction," "that perfect difficulty curve that keeps you in flow," "Mario moments." Every problem gets human review because "a single wrong problem can shake a learner's confidence." ([Brilliant blog 2025](https://blog.brilliant.org/hand-crafted-machine-made/); [about page](https://brilliant.org/about/))
- **Write-ups on meaningful vs points-based gamification.**
  - Nicholson contrasts reward-based "BLAP" (badges, levels, achievements, points) with meaningful gamification built on Play, Exposition, Choice, Information, Engagement and Reflection. He warns: "once you start giving someone a reward, you have to keep her in that reward loop forever." ([Nicholson preprint](https://scottnicholson.com/pubs/recipepreprint.pdf))
  - Performance graphs raise feelings of competence. ([Sailer et al. 2017](https://www.academia.edu/34911167/How_gamification_motivates_An_experimental_study_of_the_effects_of_specific_game_design_elements_on_psychological_need_satisfaction))
- **Synthesis [inference].** Progress feels earned when:
  1. the headline number is a calibrated estimate of real ability that *can go down*;
  2. it is compared with your own past;
  3. it tells you what to practice next;
  4. engagement rewards, if any, sit on a separate track so they cannot inflate the skill number.

---

## 7. Design implications for the Hearing Trainer

1. **Aim for about 80% first-try melody success by default, tunable 70–90%.** This hedges between several findings: the 85% theory ([Wilson](https://www.nature.com/articles/s41467-019-12552-4)); Math Garden's 0.75 in production ([Klinkenberg](https://eric.ed.gov/?id=EJ925823)); higher success bringing more practice and bigger gains ([Jansen](https://eric.ed.gov/?id=EJ1008332&pg=4&q=Math+AND+anxiety)); and lower success sometimes learning faster ([van der Kooij](https://www.nature.com/articles/s41598-026-39639-5); [Lomas](https://www.academia.edu/7000722/Optimizing_Challenge_in_an_Online_Learning_Game_Using_Large_Scale_Experiments)). **[inference]** About 80% success on 5-note melodies implies about 95% per-note accuracy. Keep the targets in `core/Config.kt`. If a session drops below about 60% success, step down fast; difficulty stops being "desirable" once the learner can't respond ([Bjork](https://bjorklab.psych.ucla.edu/wp-content/uploads/sites/13/2016/04/EBjork_RBjork_2011.pdf)).
2. **The per-user model: Elo over melody features, in the style of Birdbrain V1 and Math Garden, not a neural network.**
   - Predicted success is p = σ(θ − Σβ_f): the user's ability θ minus the summed difficulties β of the melody's features (length, largest interval, range, accidentals, tempo, whether a reference note is given).
   - After each answer, take one update step on both θ and the β values ([IEEE](https://spectrum.ieee.org/duolingo); [Klinkenberg](https://www.klinkenberg.amsterdam/publication/math-garden/)).
   - Use the trend-based dynamic K so the first sessions converge fast ([Vermeiren 2025](https://link.springer.com/article/10.1007/s11257-025-09439-z)).
   - **[inference]** For a target success p*, pick features so that Σβ ≈ θ − ln(p*/(1−p*)); for 80% that is θ − 1.39. The score can be the fraction of notes right on the first try.
   - Start with one θ and split it into per-skill θ's (pitch, memory length, rhythm, tonality) only once the data shows they diverge.
   - This is pure Kotlin in `core/` and about 100 lines. The history of θ *is* the user's fitted learning curve.
3. **Placement: a short staircase to seed θ.** Use 1-up/3-down on melody length, which converges near 79.4% ([Levitt](http://bdml.stanford.edu/twiki/pub/Haptics/DetectionThreshold/psychoacoustics.pdf)). Start below the estimated level, since easy-first improves transfer ([Ahissar & Hochstein](https://www.nature.com/articles/387401a0); [Gabay 2017](https://journals.plos.org/plosone/article?id=10.1371%2Fjournal.pone.0176488)).
4. **Session shape: short by default, with easy items mixed in.**
   - About 5–8 minutes, starting with one or two warm-up items. Keep about 20% of items well below the user's level throughout ([Liu, Lu & Dosher](https://www.sciencedirect.com/science/article/pii/S004269891100410X); [Molloy](https://journals.plos.org/plosone/article?id=10.1371%2Fjournal.pone.0036929)).
   - Never cap practice.
   - Keep the daily amount in Config, because pitch discrimination may need more trials per day ([Wright & Sabin](https://link.springer.com/article/10.1007/s00221-007-0898-z)).
5. **Interleave confusable categories after a short block.** Introduce a new interval with 2–3 items in a row, then mix it with its neighbors, for example M3 and P4 ([Brunmair & Richter](https://www.psychologie.uni-wuerzburg.de/fileadmin/06020400/2019/Brunmair_Richter_in_press__2019_META-ANALYSIS_OF_INTERLEAVED_LEARNING.pdf); [Rohrer](https://link.springer.com/article/10.1007/s10648-012-9201-3)). Make mixed practice the default path; learners prefer blocked practice even though it retains worse ([Abushanab & Bishara](https://link.springer.com/article/10.3758/s13421-013-0311-z)).
6. **Add passive listening, behind a feature flag in `core/Features.kt`.** After each answer, auto-play the target and then the user's answer. Add occasional listen-only interludes. For musical intervals, practice alone gave no learning, while practice plus listening did ([Little, Cheng & Wright 2019](https://link.springer.com/article/10.3758/s13414-018-1584-x)).
7. **Review scheduling: keep it simple first.** There are two reasonable options:
   - **Recommended:** a half-life per skill component (interval class, scale degree, rhythm cell) in the HLR form p = 2^(−Δ/h) ([Settles & Meeder](https://research.duolingo.com/papers/settles.acl16.pdf)), filling 20–30% of items from the components with the lowest predicted recall. The 20–30% figure and the update rule are my inference.
   - **Later option:** FSRS, only if you add separate memorization cards. It has no Kotlin port ([GitHub org](https://github.com/open-spaced-repetition)), and the spacing evidence is mostly from verbal recall ([Cepeda](https://www.yorku.ca/ncepeda/publications/CPVWR2006.html)).
8. **Feedback: immediate, note by note, about the task.**
   - Mark the wrong notes and name the interval: "played P5, target m6."
   - No praise of the person and no comparisons with others ([Shute](https://myweb.fsu.edu/vshute/pdf/shute%202007_f.pdf); [Kluger & DeNisi](https://cris.huji.ac.il/en/publications/the-effects-of-feedback-interventions-on-performance-a-historical/)).
   - No penalty for mistakes. Duolingo replaced hearts, which punished mistakes, with a system that "rewards success" ([Q2 2025 letter](https://investors.duolingo.com/static-files/0b55110c-2eb9-466d-8549-5459e0851290)).
   - Fade support (extra replays, a given first note) as θ rises. **[extrapolated from motor learning]** ([Winstein & Schmidt](https://www.krigolsonteaching.com/uploads/4/3/8/4/43848243/reduced_frequency_of_kr_1990_winstein_schmidt.pdf))
9. **A streak that is easy to keep and hard to lose.**
   - One short session counts ([Duolingo one-lesson change](https://blog.duolingo.com/improving-the-streak); [Brilliant](https://brilliant.org/help/features/)).
   - Two automatic, earned "rest days," applied *after* a miss ([Sharif & Shu](https://anderson-review.ucla.edu/wp-content/uploads/2021/03/Sharif-Shu_EmergencyReserveFailure_OBHDP2019.pdf); [Duolingo freezes](https://blog.duolingo.com/how-duolingo-streak-builds-habit)).
   - A repair window, for example 2 sessions within 3 days ([Silverman & Barasch](https://academic.oup.com/jcr/article-abstract/49/6/1095/6623414); Earn Back; the numbers are my inference).
   - Show "5 of the last 7 days" and a calendar next to the streak ([Beshears et al.](https://ideas.repec.org/a/inm/ormnsc/v67y2021i7p4139-4171.html); [Lally](https://www.academia.edu/2475072/How_are_habits_formed_Modelling_habit_formation_in_the_real_world)).
   - The skill rating never resets when a streak breaks.
   - Never sell streak protection, and use no guilt-driven copy **[inference, based on SDT]**.
10. **No leaderboards, hearts, gems or loot in version 1.** This is a deliberate trade: leaderboards gave Duolingo +17% learning time ([Mazal](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth)), but they hurt motivation in a course study ([Hanus & Fox](https://www.academia.edu/12454798/Assessing_the_Effects_of_Gamification_in_the_Classroom_A_Longitudinal_Study_on_Intrinsic_Motivation_Social_Comparison_Satisfaction_Effort_and_Academic_Performance)), and tangible rewards undermine intrinsic motivation ([Deci et al.](https://home.ubalt.edu/tmitch/642/articles%20syllabus/Deci%20Koestner%20Ryan%20meta%20IM%20psy%20bull%2099.pdf)). If social features come later, prefer a cooperative friend streak behind a flag ([Duolingo](https://blog.duolingo.com/product-lessons-friend-streak/)).
11. **At most one notification per day, at the user's time, with varied copy.**
    - Default to about 23.5 hours after the last session **[Duolingo practice, unverified]**, or to a routine the user names in onboarding ("after dinner") ([Gollwitzer & Sheeran](https://kops.uni-konstanz.de/handle/123456789/10973); [Stawarz](https://discovery.ucl.ac.uk/1468224/)).
    - Rotate 10 or more templates and never repeat a recent one ([Yancey & Settles](https://research.duolingo.com/papers/yancey.kdd20.pdf)).
    - Make the copy informational, e.g. "today: minor 6ths."
    - Expect the effect to wear off and never escalate volume ([HeartSteps](https://academic.oup.com/abm/article/53/6/573/5091257); [Mazal](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth)).
    - Make the late-night streak saver opt-in. Consider a home-screen widget later ([Duolingo widget](https://blog.duolingo.com/widget-feature)).
12. **The stats page shows calibrated skill, not points.**
    - An "ear rating" with an uncertainty band, marked "provisional" early on ([Lichess](https://lichess.org/page/rating-systems)).
    - A 30- and 90-day trend compared with the user's own past ([Strava](https://support.strava.com/hc/en-us/articles/216918477-Fitness-Freshness)).
    - Strongest and weakest intervals and scale degrees, with "practice this" ([Lichess dashboard](https://github.com/lichess-org/mobile/pull/2651)).
    - An interval confusion matrix **[inference]**.
    - Retention: accuracy on material not seen for 7 or more days ([Anki True Retention](https://docs.ankiweb.net/stats.html)).
    - A calendar heatmap, minutes practiced, and accuracy by hour of day ([Anki](https://docs.ankiweb.net/stats.html)).
    - Keep any points off this page ([Chess.com](https://www.chess.com/news/view/announcing-new-puzzles-rating-system)). Performance graphs support feelings of competence ([Sailer](https://www.academia.edu/34911167/How_gamification_motivates_An_experimental_study_of_the_effects_of_specific_game_design_elements_on_psychological_need_satisfaction)).
13. **Give autonomy without putting settings on the default path.** "Start" goes straight into adaptive practice. Optional: a challenge level (like Anki's desired retention, [FSRS](https://github.com/open-spaced-repetition/awesome-fsrs/wiki/ABC-of-FSRS), or Lichess's rating offsets, [source](https://github.com/lichess-org/lila/blob/master/modules/puzzle/src/main/PuzzleDifficulty.scala)) and a focus area. Use neutral labels, never "very easy" ([Lomas 2017](https://www.researchgate.net/publication/313477156_Is_Difficulty_Overrated_The_Effects_of_Choice_Novelty_and_Suspense_on_Intrinsic_Motivation_in_Educational_Games)).
14. **Record everything from day one.** Log each item's features, predicted p, outcome, replays and response time. That lets you:
    - fit the feature difficulties;
    - check calibration, meaning predicted vs actual success by decile, as the FSRS benchmark does ([benchmark](https://expertium.github.io/Benchmark.html));
    - track a CURR-style retention rate ([Mazal](https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth)).

    Response time could later feed the score, as Math Garden does ([Klinkenberg](https://eric.ed.gov/?id=EJ925823)). Put new mechanics such as rest days, listening interludes and reminders behind `Features.kt` flags.

---

## 8. URLs opened

**Opened and used**
- https://www.nature.com/articles/s41467-019-12552-4
- https://www.nature.com/articles/s41598-026-39639-5
- https://www.academia.edu/7000722/Optimizing_Challenge_in_an_Online_Learning_Game_Using_Large_Scale_Experiments
- https://www.researchgate.net/publication/313477156_Is_Difficulty_Overrated_The_Effects_of_Choice_Novelty_and_Suspense_on_Intrinsic_Motivation_in_Educational_Games
- https://eric.ed.gov/?id=EJ1008332&pg=4&q=Math+AND+anxiety
- http://bdml.stanford.edu/twiki/pub/Haptics/DetectionThreshold/psychoacoustics.pdf
- https://www.nature.com/articles/387401a0
- https://www.sciencedirect.com/science/article/pii/S004269891100410X
- https://journals.plos.org/plosone/article?id=10.1371%2Fjournal.pone.0176488
- https://en.wikipedia.org/wiki/Zone_of_proximal_development
- https://en.wikipedia.org/wiki/Flow_(psychology)
- https://www.psychologicalscience.org/publications/journals/pspi/learning-techniques.html
- https://www.yorku.ca/ncepeda/publications/CPVWR2006.html
- https://escholarship.org/uc/item/0kp5q19x
- https://link.springer.com/article/10.1007/s10648-012-9201-3
- https://www.psychologie.uni-wuerzburg.de/fileadmin/06020400/2019/Brunmair_Richter_in_press__2019_META-ANALYSIS_OF_INTERLEAVED_LEARNING.pdf
- https://bjorklab.psych.ucla.edu/wp-content/uploads/sites/13/2016/04/EBjork_RBjork_2011.pdf
- https://www.hendrix.edu/uploadedFiles/Academics/Faculty_Resources/Faculty_Development_Newsletters/Testing%20effect.pdf
- https://web.colby.edu/memoryandlanguagelab/files/2012/07/Jacoby-Wahlheim-Coane-2010-JEPLMC.pdf
- https://link.springer.com/article/10.1007/s10648-026-10117-8
- https://journals.sagepub.com/doi/10.1177/1747021820977739
- https://myweb.fsu.edu/vshute/pdf/shute%202007_f.pdf
- https://cris.huji.ac.il/en/publications/the-effects-of-feedback-interventions-on-performance-a-historical/
- https://www.krigolsonteaching.com/uploads/4/3/8/4/43848243/reduced_frequency_of_kr_1990_winstein_schmidt.pdf
- https://link.springer.com/article/10.3758/s13421-013-0311-z
- https://www.frontiersin.org/journals/psychology/articles/10.3389/fpsyg.2016.01251/full
- https://link.springer.com/article/10.3758/s13414-018-1584-x
- https://link.springer.com/article/10.1007/s00221-007-0898-z
- https://journals.plos.org/plosone/article?id=10.1371%2Fjournal.pone.0036929
- https://research.duolingo.com/papers/settles.acl16.pdf
- https://github.com/duolingo/halflife-regression
- https://expertium.github.io/Benchmark.html
- https://blog.duolingo.com/learning-how-to-help-you-learn-introducing-birdbrain
- https://spectrum.ieee.org/duolingo
- https://eric.ed.gov/?id=EJ925823
- https://www.klinkenberg.amsterdam/publication/math-garden/
- https://www.sciencedirect.com/science/article/abs/pii/S036013151630080X
- https://link.springer.com/article/10.1007/s11257-025-09439-z
- https://lichess.org/@/lichess/blog/new-puzzles-are-here/X-S6gRUA
- https://lichess.org/page/rating-systems
- https://github.com/lichess-org/lila/blob/master/modules/puzzle/src/main/PuzzleDifficulty.scala
- https://link.springer.com/article/10.1007/BF01099821
- https://en.wikipedia.org/wiki/Bayesian_knowledge_tracing
- https://github.com/open-spaced-repetition/awesome-fsrs/wiki/ABC-of-FSRS
- https://faqs.ankiweb.net/what-spaced-repetition-algorithm
- https://github.com/open-spaced-repetition
- https://jedm.educationaldatamining.org/index.php/JEDM/article/view/JEDM111
- https://arxiv.org/abs/1310.3174
- https://blog.duolingo.com/improving-the-streak
- https://blog.duolingo.com/how-duolingo-streak-builds-habit
- https://blog.duolingo.com/product-lessons-friend-streak/
- https://www.lennysnewsletter.com/p/behind-the-product-duolingo-streaks
- https://github.com/ChatPRD/lennys-podcast-transcripts/blob/main/episodes/jackson-shuttleworth/transcript.md
- https://investors.duolingo.com/static-files/0b55110c-2eb9-466d-8549-5459e0851290
- https://www.fool.com/earnings/call-transcripts/2025/08/06/duolingo-duol-q2-2025-earnings-call-transcript/
- https://www.lennysnewsletter.com/p/how-duolingo-reignited-user-growth
- https://academic.oup.com/jcr/article-abstract/49/6/1095/6623414
- https://phys.org/news/2024-03-streaks.html
- https://anderson-review.ucla.edu/wp-content/uploads/2021/03/Sharif-Shu_EmergencyReserveFailure_OBHDP2019.pdf
- https://chibe.upenn.edu/publications/nudging-persistence-after-failure-through-emergency-reserves/
- https://www.nih.gov/news-events/nih-research-matters/testing-ways-encourage-exercise
- https://ideas.repec.org/a/inm/ormnsc/v67y2021i7p4139-4171.html
- https://www.academia.edu/2475072/How_are_habits_formed_Modelling_habit_formation_in_the_real_world
- https://selfdeterminationtheory.org/SDT/documents/2000_RyanDeci_SDT.pdf
- https://home.ubalt.edu/tmitch/642/articles%20syllabus/Deci%20Koestner%20Ryan%20meta%20IM%20psy%20bull%2099.pdf
- https://link.springer.com/article/10.1007/s11031-006-9051-8
- https://www.academia.edu/34911167/How_gamification_motivates_An_experimental_study_of_the_effects_of_specific_game_design_elements_on_psychological_need_satisfaction
- https://www.academia.edu/12454798/Assessing_the_Effects_of_Gamification_in_the_Classroom_A_Longitudinal_Study_on_Intrinsic_Motivation_Social_Comparison_Satisfaction_Effort_and_Academic_Performance
- https://eric.ed.gov/?id=EJ1245270
- https://research.duolingo.com/papers/yancey.kdd20.pdf
- https://blog.duolingo.com/widget-feature
- https://researchnow-admin.flinders.edu.au/ws/portalfiles/portal/31737554/document_2_.pdf
- https://academic.oup.com/abm/article/53/6/573/5091257
- https://discovery.ucl.ac.uk/1468224/
- https://discovery.ucl.ac.uk/id/eprint/1477627/1/Chi%202016%20LBW%202.1%20camera%20ready.pdf
- https://kops.uni-konstanz.de/handle/123456789/10973
- https://docs.ankiweb.net/stats.html
- http://augmentingcognition.com/ltm.html
- https://github.com/lichess-org/mobile/pull/2651
- https://www.chess.com/news/view/announcing-new-puzzles-rating-system
- https://support.chess.com/en/articles/9681952-what-are-puzzle-points-on-chess-com
- https://support.strava.com/hc/en-us/articles/216918477-Fitness-Freshness
- https://www.whoop.com/us/en/thelocker/strain-coach/
- https://brilliant.org/help/features/
- https://brilliant.org/about/
- https://blog.brilliant.org/hand-crafted-machine-made/
- https://rive.app/blog/how-brilliant-org-motivates-learners-with-rive-animations
- https://scottnicholson.com/pubs/recipepreprint.pdf

**Opened, but little usable content or not cited**
- https://lichess.org/forum/general-chess-discussion/how-lichess-puzzle-rating-works-
- https://pubmed.ncbi.nlm.nih.gov/5541744/
- https://symposium.music.org/61-2/item/11525-does-retrieval-practice-enhance-memorization-of-piano-melodies.html
- https://www.katymilkman.com/highlights/creating-exercise-habits-using-incentives-the-trade-off-between-flexibility-and-routinization
- https://journals.sagepub.com/doi/10.1177/00222437221143755
- https://psych.substack.com/p/the-eighty-five-percent-rule
- https://aclanthology.org/P16-1174/
- https://lichess.org/training
- https://blog.duolingo.com/tips-for-maintaining-streak
- https://vicki.substack.com/p/duo-the-push-and-the-bandits

**Attempted but blocked or failed** (CAPTCHA, 403/429, timeouts): ResearchGate (Lomas 2013), pact.cs.cmu.edu, dl.acm.org, PubMed 9163425 / 22227159 / 37029510, the Kornell & Bjork PDF on bjorklab, two Class Central pages, Lally on Wiley, Semantic Scholar, mhealth.jmir.org, the Ryan & Deci copy on academia.edu, learntechlib, learningstuff.org, jov.arvojournals, PMC4041850, and Springer 10.1007/s11257-016-9185-7.
