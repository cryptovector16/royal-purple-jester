package com.Faprire4.Kobmif.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.domain.model.ClaimPredicate

@Composable
fun claimSentence(predicate: ClaimPredicate): String = when (predicate) {
    is ClaimPredicate.IsHeir -> stringResource(
        R.string.claim_is_heir,
        stringResource(CourtLabels.nameOf(predicate.target))
    )

    is ClaimPredicate.NotHeir -> stringResource(
        R.string.claim_not_heir,
        stringResource(CourtLabels.nameOf(predicate.target))
    )

    is ClaimPredicate.IsMasked -> stringResource(
        R.string.claim_masked,
        stringResource(CourtLabels.nameOf(predicate.target))
    )

    is ClaimPredicate.IsTruthful -> stringResource(
        R.string.claim_truthful,
        stringResource(CourtLabels.nameOf(predicate.target))
    )

    is ClaimPredicate.Kin -> stringResource(
        R.string.claim_kin,
        stringResource(CourtLabels.nameOf(predicate.first)),
        stringResource(CourtLabels.nameOf(predicate.second))
    )

    is ClaimPredicate.Rival -> stringResource(
        R.string.claim_rival,
        stringResource(CourtLabels.nameOf(predicate.first)),
        stringResource(CourtLabels.nameOf(predicate.second))
    )

    is ClaimPredicate.HasMotive -> stringResource(
        R.string.claim_motive,
        stringResource(CourtLabels.nameOf(predicate.target)),
        stringResource(CourtLabels.motiveLabel(predicate.motive))
    )
}
