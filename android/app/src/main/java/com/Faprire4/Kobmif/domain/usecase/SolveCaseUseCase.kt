package com.Faprire4.Kobmif.domain.usecase

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.Claim
import com.Faprire4.Kobmif.domain.model.ClaimPredicate

data class CaseWorld(val heirIndex: Int, val maskedMask: Int) {
    fun isMasked(index: Int): Boolean = (maskedMask shr index) and 1 == 1
}

class SolveCaseUseCase {

    fun allWorlds(maskedCount: Int): List<CaseWorld> {
        val total = 1 shl GameConfig.COURTIER_COUNT
        val result = ArrayList<CaseWorld>(GameConfig.COURTIER_COUNT * 10)
        for (mask in 0 until total) {
            if (popCount(mask) != maskedCount) continue
            for (heir in 0 until GameConfig.COURTIER_COUNT) {
                result.add(CaseWorld(heir, mask))
            }
        }
        return result
    }

    fun truthOf(predicate: ClaimPredicate, world: CaseWorld): Boolean? = when (predicate) {
        is ClaimPredicate.IsHeir -> world.heirIndex == predicate.target
        is ClaimPredicate.NotHeir -> world.heirIndex != predicate.target
        is ClaimPredicate.IsMasked -> world.isMasked(predicate.target)
        is ClaimPredicate.IsTruthful -> !world.isMasked(predicate.target)
        else -> null
    }

    fun fits(claim: Claim, world: CaseWorld): Boolean {
        val literal = truthOf(claim.predicate, world) ?: return true
        return literal == !world.isMasked(claim.speaker)
    }

    fun fitsMark(claim: Claim, markedTrue: Boolean, world: CaseWorld): Boolean {
        val literal = truthOf(claim.predicate, world) ?: return false
        return literal == markedTrue && world.isMasked(claim.speaker) == !markedTrue
    }

    fun consistentWorlds(claims: List<Claim>, maskedCount: Int): List<CaseWorld> =
        allWorlds(maskedCount).filter { world -> claims.all { fits(it, world) } }

    fun heirIsUnique(claims: List<Claim>, maskedCount: Int): Boolean {
        val worlds = consistentWorlds(claims, maskedCount)
        if (worlds.isEmpty()) return false
        val first = worlds.first().heirIndex
        return worlds.all { it.heirIndex == first }
    }

    private fun popCount(value: Int): Int {
        var bits = value
        var count = 0
        while (bits != 0) {
            count += bits and 1
            bits = bits shr 1
        }
        return count
    }
}
