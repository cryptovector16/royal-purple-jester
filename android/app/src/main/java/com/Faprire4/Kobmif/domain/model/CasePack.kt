package com.Faprire4.Kobmif.domain.model

data class CasePack(
    val chapterTitles: List<Int>,
    val claimantNames: List<Int>,
    val motives: List<Int>,
    val topicTruths: List<Int>,
    val topicLies: List<Int>,
    val neutralLines: List<Int>,
    val tieLabels: List<Int>,
    val exclusionLine: Int
)
