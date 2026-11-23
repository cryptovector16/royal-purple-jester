package com.Faprire4.Kobmif.domain.usecase

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.Claim
import com.Faprire4.Kobmif.domain.model.Ending

class DetectContradictionUseCase(private val solve: SolveCaseUseCase) {

    operator fun invoke(
        first: Claim,
        firstTrue: Boolean,
        second: Claim,
        secondTrue: Boolean,
        maskedCount: Int
    ): Boolean {
        if (!first.core || !second.core) return false
        if (first.id == second.id) return false
        val worlds = solve.allWorlds(maskedCount)
        val firstOk = worlds.any { solve.fitsMark(first, firstTrue, it) }
        val secondOk = worlds.any { solve.fitsMark(second, secondTrue, it) }
        if (!firstOk || !secondOk) return false
        return worlds.none {
            solve.fitsMark(first, firstTrue, it) && solve.fitsMark(second, secondTrue, it)
        }
    }
}

class ScoreVerdictUseCase {

    operator fun invoke(locks: Int, questionsLeft: Int, unmasked: Int, wrongLocks: Int): Int {
        val raw = GameConfig.CONFIDENCE_BASE +
            locks * GameConfig.CONFIDENCE_PER_LOCK +
            questionsLeft * GameConfig.CONFIDENCE_PER_QUESTION_LEFT +
            unmasked * GameConfig.CONFIDENCE_PER_UNMASK -
            wrongLocks * GameConfig.CONFIDENCE_PER_WRONG_LOCK
        return raw.coerceIn(0, 100)
    }
}

class ResolveEndingUseCase {

    operator fun invoke(accused: Boolean, correct: Boolean, confidence: Int): Ending = when {
        !accused -> Ending.SILENT_COUNCIL
        !correct -> Ending.USURPER_CROWNED
        confidence >= GameConfig.CONFIDENCE_TRUE_CROWN -> Ending.TRUE_CROWN
        confidence >= GameConfig.CONFIDENCE_QUIET_CORONATION -> Ending.QUIET_CORONATION
        else -> Ending.DOUBTED_HEIR
    }
}
