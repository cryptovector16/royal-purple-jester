package com.Faprire4.Kobmif.domain.usecase

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.CaseResult
import com.Faprire4.Kobmif.domain.model.CaseSetup
import com.Faprire4.Kobmif.domain.model.Ending

class ResolveVerdictUseCase(private val computeInsight: ComputeInsightUseCase) {

    operator fun invoke(
        setup: CaseSetup,
        accusedId: Int,
        contradictions: Int,
        motivesRevealed: Int,
        questionsLeft: Int,
        favour: Int
    ): CaseResult {
        val correct = accusedId == setup.heirId
        val insight = computeInsight(contradictions, motivesRevealed, questionsLeft, favour)
        val ending = when {
            correct && insight >= GameConfig.INSIGHT_THRESHOLD -> Ending.CROWN_RESTORED
            correct -> Ending.QUIET_CORONATION
            contradictions >= GameConfig.REQUIRED_CONTRADICTIONS -> Ending.JESTER_LAUGHS
            else -> Ending.COURT_IN_SHADOW
        }
        val exposed = setup.claimants
            .filter { setup.liarIds.contains(it.id) }
            .map { it.motiveRes }
        return CaseResult(
            chapterIndex = setup.chapterIndex,
            chapterTitleRes = setup.chapterTitleRes,
            correct = correct,
            ending = ending,
            insight = insight,
            contradictions = contradictions,
            questionsLeft = questionsLeft,
            motivesRevealed = motivesRevealed,
            favour = favour,
            heirNameRes = setup.claimants[setup.heirId].nameRes,
            accusedNameRes = setup.claimants[accusedId.coerceIn(0, setup.claimants.size - 1)].nameRes,
            exposedMotives = exposed
        )
    }
}
