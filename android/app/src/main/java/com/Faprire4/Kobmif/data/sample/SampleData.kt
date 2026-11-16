package com.Faprire4.Kobmif.data.sample

import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.domain.model.CasePack

object SampleData {

    val chapterTitles = listOf(
        R.string.chapter_1,
        R.string.chapter_2,
        R.string.chapter_3,
        R.string.chapter_4,
        R.string.chapter_5,
        R.string.chapter_6
    )

    private val claimantNames = listOf(
        R.string.claimant_duchess,
        R.string.claimant_armourer,
        R.string.claimant_scribe,
        R.string.claimant_foundling,
        R.string.claimant_chamberlain,
        R.string.claimant_envoy,
        R.string.claimant_physician,
        R.string.claimant_revels
    )

    private val motives = listOf(
        R.string.motive_1,
        R.string.motive_2,
        R.string.motive_3,
        R.string.motive_4,
        R.string.motive_5,
        R.string.motive_6,
        R.string.motive_7,
        R.string.motive_8
    )

    private val topicTruths = listOf(
        R.string.topic_truth_1,
        R.string.topic_truth_2,
        R.string.topic_truth_3,
        R.string.topic_truth_4,
        R.string.topic_truth_5,
        R.string.topic_truth_6,
        R.string.topic_truth_7,
        R.string.topic_truth_8
    )

    private val topicLies = listOf(
        R.string.topic_lie_1,
        R.string.topic_lie_2,
        R.string.topic_lie_3,
        R.string.topic_lie_4,
        R.string.topic_lie_5,
        R.string.topic_lie_6,
        R.string.topic_lie_7,
        R.string.topic_lie_8
    )

    private val neutralLines = listOf(
        R.string.neutral_1,
        R.string.neutral_2,
        R.string.neutral_3,
        R.string.neutral_4,
        R.string.neutral_5,
        R.string.neutral_6,
        R.string.neutral_7,
        R.string.neutral_8
    )

    private val tieLabels = listOf(
        R.string.tie_debtor,
        R.string.tie_rival,
        R.string.tie_kin,
        R.string.tie_patron
    )

    val casePack = CasePack(
        chapterTitles = chapterTitles,
        claimantNames = claimantNames,
        motives = motives,
        topicTruths = topicTruths,
        topicLies = topicLies,
        neutralLines = neutralLines,
        tieLabels = tieLabels,
        exclusionLine = R.string.statement_exclusion_fmt
    )
}
