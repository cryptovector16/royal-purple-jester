package com.Faprire4.Kobmif.data.sample

import com.Faprire4.Kobmif.domain.model.Case
import com.Faprire4.Kobmif.domain.model.Chapter
import com.Faprire4.Kobmif.domain.model.Claim
import com.Faprire4.Kobmif.domain.model.ClaimPredicate
import com.Faprire4.Kobmif.domain.model.Courtier
import com.Faprire4.Kobmif.domain.model.CourtierRole
import com.Faprire4.Kobmif.domain.model.Motive
import com.Faprire4.Kobmif.domain.model.Relation

object SampleData {

    val chapters: List<Chapter> = listOf(
        Chapter(index = 0, coreClaims = 5, graphClaims = 2, questions = 5, patience = 3, maskedCount = 1),
        Chapter(index = 1, coreClaims = 6, graphClaims = 3, questions = 6, patience = 3, maskedCount = 2),
        Chapter(index = 2, coreClaims = 7, graphClaims = 3, questions = 6, patience = 3, maskedCount = 2),
        Chapter(index = 3, coreClaims = 8, graphClaims = 4, questions = 6, patience = 3, maskedCount = 2),
        Chapter(index = 4, coreClaims = 9, graphClaims = 4, questions = 5, patience = 3, maskedCount = 3),
        Chapter(index = 5, coreClaims = 10, graphClaims = 5, questions = 5, patience = 3, maskedCount = 3)
    )

    fun chapterOf(index: Int): Chapter = chapters[index.coerceIn(chapters.indices)]

    fun fallbackCase(chapter: Chapter, attempt: Int, seed: Int): Case {
        val maskedSet = setOf(2, 4)
        val motives = listOf(
            Motive.DEVOTION,
            Motive.CROWN,
            Motive.VENGEANCE,
            Motive.COIN,
            Motive.FEAR
        )
        val courtiers = (0..4).map { index ->
            Courtier(
                index = index,
                role = if (index in maskedSet) CourtierRole.MASKED else CourtierRole.TRUTHFUL,
                motive = motives[index]
            )
        }
        val claims = listOf(
            Claim(0, 0, ClaimPredicate.IsHeir(1)),
            Claim(1, 2, ClaimPredicate.IsHeir(3)),
            Claim(2, 3, ClaimPredicate.IsMasked(4)),
            Claim(3, 4, ClaimPredicate.IsTruthful(2)),
            Claim(4, 1, ClaimPredicate.NotHeir(0)),
            Claim(5, 3, ClaimPredicate.Kin(0, 1)),
            Claim(6, 4, ClaimPredicate.HasMotive(0, Motive.COIN))
        )
        val relations = mapOf(
            1 to Relation.KIN,
            2 to Relation.DISTANT,
            3 to Relation.RIVAL,
            4 to Relation.DISTANT,
            12 to Relation.RIVAL,
            13 to Relation.DISTANT,
            14 to Relation.KIN,
            23 to Relation.DISTANT,
            24 to Relation.RIVAL,
            34 to Relation.DISTANT
        )
        return Case(
            chapterIndex = chapter.index,
            attempt = attempt,
            seed = seed,
            heirIndex = 1,
            maskedCount = 2,
            courtiers = courtiers,
            claims = claims,
            relations = relations,
            graphRotationDeg = 18f,
            questions = chapter.questions,
            patience = chapter.patience
        )
    }
}
