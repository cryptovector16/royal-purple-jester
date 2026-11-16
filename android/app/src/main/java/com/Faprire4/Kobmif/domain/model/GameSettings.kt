package com.Faprire4.Kobmif.domain.model

data class GameSettings(
    val questionsPerCase: Int,
    val sealHints: Boolean,
    val steadyCandlelight: Boolean,
    val autoFilterBoard: Boolean
)
