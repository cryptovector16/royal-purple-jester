package com.Faprire4.Kobmif.domain.usecase

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.Case
import com.Faprire4.Kobmif.domain.model.Chapter
import com.Faprire4.Kobmif.domain.model.Claim
import com.Faprire4.Kobmif.domain.model.ClaimPredicate
import com.Faprire4.Kobmif.domain.model.Courtier
import com.Faprire4.Kobmif.domain.model.CourtierRole
import com.Faprire4.Kobmif.domain.model.Motive
import com.Faprire4.Kobmif.domain.model.Relation
import kotlin.random.Random

class GenerateCaseUseCase(
    private val solve: SolveCaseUseCase,
    private val fallback: (Chapter, Int, Int) -> Case
) {

    operator fun invoke(chapter: Chapter, attempt: Int): Case {
        val seed = seedFor(chapter.index, attempt)
        var best: Case? = null
        for (pass in 0 until GameConfig.MAX_GENERATION_ATTEMPTS) {
            val candidate = build(chapter, attempt, seed, pass) ?: continue
            if (best == null) best = candidate
            if (isAcceptable(candidate)) return candidate
        }
        return best ?: fallback(chapter, attempt, seed)
    }

    fun seedFor(chapterIndex: Int, attempt: Int): Int =
        (chapterIndex + 1) * GameConfig.SEED_CHAPTER_FACTOR xor
            (attempt + 1) * GameConfig.SEED_ATTEMPT_FACTOR

    private fun isAcceptable(case: Case): Boolean {
        val core = case.claims.filter { it.core }
        if (core.size < 3) return false
        if (!solve.heirIsUnique(core, case.maskedCount)) return false
        return core.none { solve.heirIsUnique(listOf(it), case.maskedCount) }
    }

    private fun build(chapter: Chapter, attempt: Int, seed: Int, pass: Int): Case? {
        val count = GameConfig.COURTIER_COUNT
        val random = Random(seed + pass * 31)
        val heir = random.nextInt(count)
        val maskedCount = chapter.maskedCount.coerceIn(1, count - 1)
        val maskedSet = (0 until count).shuffled(random).take(maskedCount).toSet()
        val motiveOrder = Motive.entries.shuffled(random)
        val courtiers = (0 until count).map { index ->
            Courtier(
                index = index,
                role = if (index in maskedSet) CourtierRole.MASKED else CourtierRole.TRUTHFUL,
                motive = motiveOrder[index % motiveOrder.size]
            )
        }

        val relations = HashMap<Int, Relation>()
        for (first in 0 until count) {
            for (second in first + 1 until count) {
                val roll = random.nextInt(100)
                val relation = when {
                    roll < 26 -> Relation.KIN
                    roll < 56 -> Relation.RIVAL
                    else -> Relation.DISTANT
                }
                relations[first * 10 + second] = relation
            }
        }

        val claims = ArrayList<Claim>()
        var worlds = solve.allWorlds(maskedCount)
        val corePool = coreCandidates(count, heir, maskedSet).shuffled(random)
        for (candidate in corePool) {
            if (claims.size >= chapter.coreClaims) break
            val shaped = Claim(claims.size, candidate.speaker, candidate.predicate)
            val reduced = worlds.filter { solve.fits(shaped, it) }
            if (reduced.size < worlds.size) {
                claims.add(shaped)
                worlds = reduced
            }
        }
        if (claims.size < 3) return null

        val graphPool = graphCandidates(count, maskedSet, courtiers, relations)
            .shuffled(random)
            .distinctBy { it.predicate }
        for (candidate in graphPool) {
            if (claims.size >= chapter.coreClaims + chapter.graphClaims) break
            claims.add(Claim(claims.size, candidate.speaker, candidate.predicate))
        }

        val rotation = random.nextInt(GameConfig.GRAPH_ROTATION_STEPS) * GameConfig.GRAPH_ROTATION_STEP_DEG
        return Case(
            chapterIndex = chapter.index,
            attempt = attempt,
            seed = seed,
            heirIndex = heir,
            maskedCount = maskedCount,
            courtiers = courtiers,
            claims = claims.shuffled(random).mapIndexed { index, claim -> claim.copy(id = index) },
            relations = relations,
            graphRotationDeg = rotation,
            questions = chapter.questions,
            patience = chapter.patience
        )
    }

    private data class Candidate(val speaker: Int, val predicate: ClaimPredicate)

    private fun coreCandidates(count: Int, heir: Int, maskedSet: Set<Int>): List<Candidate> {
        val pool = ArrayList<Candidate>()
        for (speaker in 0 until count) {
            val speaksTruth = speaker !in maskedSet
            for (target in 0 until count) {
                if (target == speaker) continue
                if ((target == heir) == speaksTruth) {
                    pool.add(Candidate(speaker, ClaimPredicate.IsHeir(target)))
                }
                if ((target != heir) == speaksTruth) {
                    pool.add(Candidate(speaker, ClaimPredicate.NotHeir(target)))
                }
                if ((target in maskedSet) == speaksTruth) {
                    pool.add(Candidate(speaker, ClaimPredicate.IsMasked(target)))
                }
                if ((target !in maskedSet) == speaksTruth) {
                    pool.add(Candidate(speaker, ClaimPredicate.IsTruthful(target)))
                }
            }
        }
        return pool
    }

    private fun graphCandidates(
        count: Int,
        maskedSet: Set<Int>,
        courtiers: List<Courtier>,
        relations: Map<Int, Relation>
    ): List<Candidate> {
        val pool = ArrayList<Candidate>()
        for (speaker in 0 until count) {
            val speaksTruth = speaker !in maskedSet
            for (first in 0 until count) {
                for (second in first + 1 until count) {
                    val relation = relations[first * 10 + second] ?: Relation.DISTANT
                    if ((relation == Relation.KIN) == speaksTruth) {
                        pool.add(Candidate(speaker, ClaimPredicate.Kin(first, second)))
                    }
                    if ((relation == Relation.RIVAL) == speaksTruth) {
                        pool.add(Candidate(speaker, ClaimPredicate.Rival(first, second)))
                    }
                }
            }
            for (target in 0 until count) {
                for (motive in Motive.entries) {
                    if ((courtiers[target].motive == motive) == speaksTruth) {
                        pool.add(Candidate(speaker, ClaimPredicate.HasMotive(target, motive)))
                    }
                }
            }
        }
        return pool
    }
}
