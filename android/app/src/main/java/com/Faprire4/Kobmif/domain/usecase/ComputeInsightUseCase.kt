package com.Faprire4.Kobmif.domain.usecase

class ComputeInsightUseCase {

    operator fun invoke(
        contradictions: Int,
        motivesRevealed: Int,
        questionsLeft: Int,
        favour: Int
    ): Int {
        return contradictions * 15 + motivesRevealed * 10 + questionsLeft * 5 + favour * 4
    }
}
