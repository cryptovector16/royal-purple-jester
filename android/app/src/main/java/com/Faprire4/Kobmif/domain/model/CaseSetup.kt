package com.Faprire4.Kobmif.domain.model

data class CaseSetup(
    val chapterIndex: Int,
    val chapterTitleRes: Int,
    val seed: Long,
    val claimants: List<Claimant>,
    val queues: Map<Int, List<Statement>>,
    val ties: List<Tie>,
    val heirId: Int,
    val liarIds: Set<Int>,
    val questionBudget: Int,
    val fallback: Boolean
)
