package com.Faprire4.Kobmif.presentation.caseselect

import com.Faprire4.Kobmif.domain.model.Chapter

data class CaseSelectUiState(
    val chapters: List<Chapter> = emptyList(),
    val seals: Int = 0,
    val totalChapters: Int = 0
)
