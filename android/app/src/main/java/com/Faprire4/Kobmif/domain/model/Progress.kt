package com.Faprire4.Kobmif.domain.model

data class Progress(
    val unlockedChapters: Int,
    val solvedChapters: Set<Int>,
    val endings: Map<Int, Ending>,
    val bestInsight: Int,
    val casesClosed: Int
)
