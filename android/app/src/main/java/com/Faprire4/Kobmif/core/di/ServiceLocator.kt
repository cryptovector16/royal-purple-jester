package com.Faprire4.Kobmif.core.di

import android.content.Context
import com.Faprire4.Kobmif.data.local.CourtPreferences
import com.Faprire4.Kobmif.data.repository.CaseProgressRepositoryImpl
import com.Faprire4.Kobmif.data.repository.CourtSessionRepositoryImpl
import com.Faprire4.Kobmif.data.repository.SettingsRepositoryImpl
import com.Faprire4.Kobmif.data.sample.SampleData
import com.Faprire4.Kobmif.domain.repository.CaseProgressRepository
import com.Faprire4.Kobmif.domain.repository.CourtSessionRepository
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import com.Faprire4.Kobmif.domain.usecase.DetectContradictionUseCase
import com.Faprire4.Kobmif.domain.usecase.GenerateCaseUseCase
import com.Faprire4.Kobmif.domain.usecase.ResolveEndingUseCase
import com.Faprire4.Kobmif.domain.usecase.ScoreVerdictUseCase
import com.Faprire4.Kobmif.domain.usecase.SolveCaseUseCase

object ServiceLocator {

    private var container: Container? = null

    class Container(context: Context) {
        private val preferences = CourtPreferences(context)
        private val solver = SolveCaseUseCase()

        val progressRepository: CaseProgressRepository = CaseProgressRepositoryImpl(preferences)
        val settingsRepository: SettingsRepository = SettingsRepositoryImpl(preferences)
        val sessionRepository: CourtSessionRepository = CourtSessionRepositoryImpl()
        val generateCase = GenerateCaseUseCase(solver) { chapter, attempt, seed ->
            SampleData.fallbackCase(chapter, attempt, seed)
        }
        val detectContradiction = DetectContradictionUseCase(solver)
        val scoreVerdict = ScoreVerdictUseCase()
        val resolveEnding = ResolveEndingUseCase()
        val viewModelFactory: AppViewModelFactory by lazy { AppViewModelFactory(this) }
    }

    fun install(context: Context) {
        if (container == null) container = Container(context.applicationContext)
    }

    private fun requireContainer(): Container =
        container ?: throw IllegalStateException(MISSING)

    val factory: AppViewModelFactory
        get() = requireContainer().viewModelFactory

    private const val MISSING = "ServiceLocator.install was not called"
}
