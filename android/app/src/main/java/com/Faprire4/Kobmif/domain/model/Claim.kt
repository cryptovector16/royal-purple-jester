package com.Faprire4.Kobmif.domain.model

enum class SealState { UNMARKED, MARKED_TRUE, MARKED_FALSE }

sealed interface ClaimPredicate {
    data class IsHeir(val target: Int) : ClaimPredicate
    data class NotHeir(val target: Int) : ClaimPredicate
    data class IsMasked(val target: Int) : ClaimPredicate
    data class IsTruthful(val target: Int) : ClaimPredicate
    data class Kin(val first: Int, val second: Int) : ClaimPredicate
    data class Rival(val first: Int, val second: Int) : ClaimPredicate
    data class HasMotive(val target: Int, val motive: Motive) : ClaimPredicate
}

data class Claim(
    val id: Int,
    val speaker: Int,
    val predicate: ClaimPredicate
) {
    val core: Boolean
        get() = predicate is ClaimPredicate.IsHeir ||
            predicate is ClaimPredicate.NotHeir ||
            predicate is ClaimPredicate.IsMasked ||
            predicate is ClaimPredicate.IsTruthful
}
