package com.Faprire4.Kobmif.domain.usecase

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.CaseSetup
import com.Faprire4.Kobmif.domain.model.StatementKind

class ValidateCaseUseCase(private val crossExamine: CrossExamineUseCase) {

    operator fun invoke(setup: CaseSetup): Boolean {
        val statements = setup.queues.values.flatten()
        if (statements.isEmpty()) {
            return false
        }
        val excluded = statements
            .filter { it.kind == StatementKind.EXCLUSION && !setup.liarIds.contains(it.speakerId) }
            .map { it.targetId }
            .toSet()
        val survivors = (0 until GameConfig.CLAIMANTS).filterNot { excluded.contains(it) }
        if (survivors.size != 1 || survivors.first() != setup.heirId) {
            return false
        }
        var pairs = 0
        for (i in statements.indices) {
            for (j in i + 1 until statements.size) {
                if (crossExamine(statements[i], statements[j])) {
                    pairs++
                }
            }
        }
        return pairs >= GameConfig.REQUIRED_CONTRADICTIONS
    }
}
