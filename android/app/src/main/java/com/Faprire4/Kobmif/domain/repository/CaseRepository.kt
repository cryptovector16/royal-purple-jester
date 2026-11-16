package com.Faprire4.Kobmif.domain.repository

import com.Faprire4.Kobmif.domain.model.CasePack

interface CaseRepository {
    fun pack(): CasePack
    fun statementsPerClaimant(chapterIndex: Int): Int
    fun liarsForChapter(chapterIndex: Int): Int
}
