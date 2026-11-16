package com.Faprire4.Kobmif.data.repository

import com.Faprire4.Kobmif.data.sample.SampleData
import com.Faprire4.Kobmif.domain.model.CasePack
import com.Faprire4.Kobmif.domain.repository.CaseRepository

class CaseRepositoryImpl : CaseRepository {

    override fun pack(): CasePack = SampleData.casePack

    override fun statementsPerClaimant(chapterIndex: Int): Int =
        if (chapterIndex >= 3) 4 else 3

    override fun liarsForChapter(chapterIndex: Int): Int =
        if (chapterIndex >= 3) 2 else 1
}
