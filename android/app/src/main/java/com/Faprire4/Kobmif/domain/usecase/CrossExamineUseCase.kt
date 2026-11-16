package com.Faprire4.Kobmif.domain.usecase

import com.Faprire4.Kobmif.domain.model.Statement

class CrossExamineUseCase {

    operator fun invoke(first: Statement, second: Statement): Boolean {
        if (first.id == second.id) {
            return false
        }
        return first.topicId == second.topicId && first.assertion != second.assertion
    }
}
