package com.Faprire4.Kobmif.core.ui

import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.domain.model.Ending
import com.Faprire4.Kobmif.domain.model.Motive

object CourtLabels {

    private val names = intArrayOf(
        R.string.courtier_name_1,
        R.string.courtier_name_2,
        R.string.courtier_name_3,
        R.string.courtier_name_4,
        R.string.courtier_name_5
    )

    private val masks = intArrayOf(
        R.string.mask_name_1,
        R.string.mask_name_2,
        R.string.mask_name_3,
        R.string.mask_name_4,
        R.string.mask_name_5
    )

    private val sprites = intArrayOf(
        R.drawable.sprite_1,
        R.drawable.sprite_2,
        R.drawable.sprite_3,
        R.drawable.sprite_4,
        R.drawable.sprite_5
    )

    private val chapterNumerals = intArrayOf(
        R.string.chapter_numeral_1,
        R.string.chapter_numeral_2,
        R.string.chapter_numeral_3,
        R.string.chapter_numeral_4,
        R.string.chapter_numeral_5,
        R.string.chapter_numeral_6
    )

    private val chapterNames = intArrayOf(
        R.string.chapter_name_1,
        R.string.chapter_name_2,
        R.string.chapter_name_3,
        R.string.chapter_name_4,
        R.string.chapter_name_5,
        R.string.chapter_name_6
    )

    private val ruleTitles = intArrayOf(
        R.string.rule_title_1,
        R.string.rule_title_2,
        R.string.rule_title_3,
        R.string.rule_title_4,
        R.string.rule_title_5
    )

    private val ruleBodies = intArrayOf(
        R.string.rule_body_1,
        R.string.rule_body_2,
        R.string.rule_body_3,
        R.string.rule_body_4,
        R.string.rule_body_5
    )

    private val ruleIcons = intArrayOf(
        R.drawable.ic_quill,
        R.drawable.ic_wax_seal,
        R.drawable.ic_scales,
        R.drawable.ic_mask,
        R.drawable.ic_crown
    )

    fun nameOf(courtier: Int): Int = names[courtier.coerceIn(names.indices)]

    fun maskOf(courtier: Int): Int = masks[courtier.coerceIn(masks.indices)]

    fun spriteOf(courtier: Int): Int = sprites[courtier.coerceIn(sprites.indices)]

    fun chapterNumeral(index: Int): Int = chapterNumerals[index.coerceIn(chapterNumerals.indices)]

    fun chapterName(index: Int): Int = chapterNames[index.coerceIn(chapterNames.indices)]

    fun ruleTitle(index: Int): Int = ruleTitles[index.coerceIn(ruleTitles.indices)]

    fun ruleBody(index: Int): Int = ruleBodies[index.coerceIn(ruleBodies.indices)]

    fun ruleIcon(index: Int): Int = ruleIcons[index.coerceIn(ruleIcons.indices)]

    fun motiveLabel(motive: Motive): Int = when (motive) {
        Motive.CROWN -> R.string.motive_crown
        Motive.COIN -> R.string.motive_coin
        Motive.VENGEANCE -> R.string.motive_vengeance
        Motive.DEVOTION -> R.string.motive_devotion
        Motive.FEAR -> R.string.motive_fear
    }

    fun motiveBadge(motive: Motive): Int = when (motive) {
        Motive.CROWN -> R.string.motive_badge_crown
        Motive.COIN -> R.string.motive_badge_coin
        Motive.VENGEANCE -> R.string.motive_badge_vengeance
        Motive.DEVOTION -> R.string.motive_badge_devotion
        Motive.FEAR -> R.string.motive_badge_fear
    }

    fun endingTitle(ending: Ending): Int = when (ending) {
        Ending.TRUE_CROWN -> R.string.ending_true_crown
        Ending.QUIET_CORONATION -> R.string.ending_quiet_coronation
        Ending.DOUBTED_HEIR -> R.string.ending_doubted_heir
        Ending.SILENT_COUNCIL -> R.string.ending_silent_council
        Ending.USURPER_CROWNED -> R.string.ending_usurper_crowned
    }

    fun endingStory(ending: Ending): Int = when (ending) {
        Ending.TRUE_CROWN -> R.string.ending_story_true_crown
        Ending.QUIET_CORONATION -> R.string.ending_story_quiet_coronation
        Ending.DOUBTED_HEIR -> R.string.ending_story_doubted_heir
        Ending.SILENT_COUNCIL -> R.string.ending_story_silent_council
        Ending.USURPER_CROWNED -> R.string.ending_story_usurper_crowned
    }
}
