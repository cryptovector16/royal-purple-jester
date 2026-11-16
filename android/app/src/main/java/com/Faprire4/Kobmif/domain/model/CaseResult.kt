package com.Faprire4.Kobmif.domain.model

data class CaseResult(
    val chapterIndex: Int,
    val chapterTitleRes: Int,
    val correct: Boolean,
    val ending: Ending,
    val insight: Int,
    val contradictions: Int,
    val questionsLeft: Int,
    val motivesRevealed: Int,
    val favour: Int,
    val heirNameRes: Int,
    val accusedNameRes: Int,
    val exposedMotives: List<Int>
)
