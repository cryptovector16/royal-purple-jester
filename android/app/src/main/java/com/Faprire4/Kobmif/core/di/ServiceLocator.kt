package com.Faprire4.Kobmif.core.di

import android.content.Context
import com.Faprire4.Kobmif.data.local.JesterPreferences
import com.Faprire4.Kobmif.data.repository.CaseRepositoryImpl
import com.Faprire4.Kobmif.data.repository.ProgressRepositoryImpl
import com.Faprire4.Kobmif.data.repository.SettingsRepositoryImpl
import com.Faprire4.Kobmif.domain.repository.CaseRepository
import com.Faprire4.Kobmif.domain.repository.ProgressRepository
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import com.Faprire4.Kobmif.domain.usecase.ComputeInsightUseCase
import com.Faprire4.Kobmif.domain.usecase.CrossExamineUseCase
import com.Faprire4.Kobmif.domain.usecase.GenerateCaseUseCase
import com.Faprire4.Kobmif.domain.usecase.ResolveVerdictUseCase
import com.Faprire4.Kobmif.domain.usecase.ValidateCaseUseCase

object ServiceLocator {

    private var appContext: Context? = null

    fun init(context: Context) {
        if (appContext == null) {
            appContext = context.applicationContext
        }
    }

    private val preferences: JesterPreferences by lazy {
        JesterPreferences(requireNotNull(appContext))
    }

    private val progressRepository: ProgressRepository by lazy {
        ProgressRepositoryImpl(preferences)
    }

    private val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(preferences)
    }

    private val caseRepository: CaseRepository by lazy {
        CaseRepositoryImpl()
    }

    private val crossExamineUseCase: CrossExamineUseCase by lazy { CrossExamineUseCase() }

    private val computeInsightUseCase: ComputeInsightUseCase by lazy { ComputeInsightUseCase() }

    private val validateCaseUseCase: ValidateCaseUseCase by lazy {
        ValidateCaseUseCase(crossExamineUseCase)
    }

    private val generateCaseUseCase: GenerateCaseUseCase by lazy {
        GenerateCaseUseCase(caseRepository, validateCaseUseCase)
    }

    private val resolveVerdictUseCase: ResolveVerdictUseCase by lazy {
        ResolveVerdictUseCase(computeInsightUseCase)
    }

    val viewModelFactory: AppViewModelFactory by lazy {
        AppViewModelFactory(
            progressRepository = progressRepository,
            settingsRepository = settingsRepository,
            generateCase = generateCaseUseCase,
            crossExamine = crossExamineUseCase,
            resolveVerdict = resolveVerdictUseCase
        )
    }
}
