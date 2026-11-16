package com.Faprire4.Kobmif.domain.model

data class Chapter(
    val index: Int,
    val titleRes: Int,
    val statementsPerClaimant: Int,
    val liars: Int,
    val unlocked: Boolean,
    val solved: Boolean,
    val ending: Ending?
)
