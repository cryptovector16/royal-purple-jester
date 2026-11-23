package com.Faprire4.Kobmif.domain.model

enum class Ending(val rank: Int, val victory: Boolean) {
    USURPER_CROWNED(0, false),
    SILENT_COUNCIL(1, false),
    DOUBTED_HEIR(2, true),
    QUIET_CORONATION(3, true),
    TRUE_CROWN(4, true);

    companion object {
        fun ofRank(rank: Int): Ending? = entries.firstOrNull { it.rank == rank }
    }
}

data class Verdict(
    val chapterIndex: Int,
    val seed: Int,
    val ending: Ending,
    val heirIndex: Int,
    val accusedIndex: Int,
    val confidence: Int,
    val locks: Int,
    val wrongLocks: Int,
    val questionsLeft: Int,
    val unmasked: Int
)
