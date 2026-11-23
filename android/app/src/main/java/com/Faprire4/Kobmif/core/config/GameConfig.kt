package com.Faprire4.Kobmif.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val COURTIER_COUNT = 5
    const val CHAPTER_COUNT = 6
    const val START_PATIENCE = 3
    const val INSIGHT_PER_LOCK = 1
    const val UNMASK_COST = 3
    const val WRONG_LOCK_PATIENCE_COST = 1
    const val MAX_GENERATION_ATTEMPTS = 20
    const val VERDICT_REVEAL_MS = 2400L
    const val FORCED_VOTE_DELAY_MS = 1200L
    const val COUNCIL_VOTE_GRACE_MS = 3000L
    const val SEED_CHAPTER_FACTOR = 7919
    const val SEED_ATTEMPT_FACTOR = 104729
    const val CONFIDENCE_BASE = 30
    const val CONFIDENCE_PER_LOCK = 15
    const val CONFIDENCE_PER_QUESTION_LEFT = 6
    const val CONFIDENCE_PER_UNMASK = 8
    const val CONFIDENCE_PER_WRONG_LOCK = 12
    const val CONFIDENCE_TRUE_CROWN = 80
    const val CONFIDENCE_QUIET_CORONATION = 45
    const val GRAPH_ROTATION_STEPS = 5
    const val GRAPH_ROTATION_STEP_DEG = 18f
}
