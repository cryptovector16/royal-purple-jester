package com.Faprire4.Kobmif.domain.model

data class Statement(
    val id: Int,
    val speakerId: Int,
    val textRes: Int,
    val targetNameRes: Int,
    val targetId: Int,
    val topicId: Int,
    val assertion: Boolean,
    val truthful: Boolean,
    val kind: StatementKind
)
