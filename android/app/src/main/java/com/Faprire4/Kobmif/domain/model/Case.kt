package com.Faprire4.Kobmif.domain.model

data class Chapter(
    val index: Int,
    val coreClaims: Int,
    val graphClaims: Int,
    val questions: Int,
    val patience: Int,
    val maskedCount: Int
)

data class Case(
    val chapterIndex: Int,
    val attempt: Int,
    val seed: Int,
    val heirIndex: Int,
    val maskedCount: Int,
    val courtiers: List<Courtier>,
    val claims: List<Claim>,
    val relations: Map<Int, Relation>,
    val graphRotationDeg: Float,
    val questions: Int,
    val patience: Int
) {
    fun relationKey(first: Int, second: Int): Int =
        if (first < second) first * 10 + second else second * 10 + first

    fun relationOf(first: Int, second: Int): Relation =
        relations[relationKey(first, second)] ?: Relation.DISTANT
}
