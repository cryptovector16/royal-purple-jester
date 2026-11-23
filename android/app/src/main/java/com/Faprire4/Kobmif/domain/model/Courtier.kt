package com.Faprire4.Kobmif.domain.model

enum class CourtierRole { TRUTHFUL, MASKED }

enum class Motive { CROWN, COIN, VENGEANCE, DEVOTION, FEAR }

enum class Relation { KIN, RIVAL, DISTANT }

data class Courtier(
    val index: Int,
    val role: CourtierRole,
    val motive: Motive
)
