package com.Faprire4.Kobmif.domain.model

data class CourtRecord(
    val unlockedChapters: Int,
    val casesSolved: Int,
    val bestRanks: List<Int>
) {
    fun bestRankOf(chapterIndex: Int): Int = bestRanks.getOrElse(chapterIndex) { -1 }

    fun isUnlocked(chapterIndex: Int): Boolean = chapterIndex < unlockedChapters

    val bestEnding: Ending?
        get() = Ending.ofRank(bestRanks.maxOrNull() ?: -1)
}

data class CourtSettings(
    val confirmAccusation: Boolean,
    val candleMotion: Boolean,
    val maskColours: Boolean
)
