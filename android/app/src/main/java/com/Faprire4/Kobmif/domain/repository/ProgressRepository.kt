package com.Faprire4.Kobmif.domain.repository

import com.Faprire4.Kobmif.domain.model.CaseResult
import com.Faprire4.Kobmif.domain.model.Progress

interface ProgressRepository {
    fun read(): Progress
    fun record(result: CaseResult)
}
