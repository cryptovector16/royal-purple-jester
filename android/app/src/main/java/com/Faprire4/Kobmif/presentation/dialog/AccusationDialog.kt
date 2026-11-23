package com.Faprire4.Kobmif.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.Crimson
import com.Faprire4.Kobmif.core.theme.MaskColours
import com.Faprire4.Kobmif.core.theme.NeutralSeal
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.CourtierMedallion
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton

@Composable
fun AccusationDialog(
    requireConfirm: Boolean,
    maskColours: Boolean,
    onName: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var chosen by rememberSaveable { mutableIntStateOf(0) }
    val selectedLabel = stringResource(R.string.state_selected)
    val unselectedLabel = stringResource(R.string.state_not_selected)

    CourtDialog(title = stringResource(R.string.accuse_title), onDismiss = onDismiss) {
        for (index in 0 until 5) {
            val active = index == chosen
            val shape = RoundedCornerShape(14.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .background(SurfaceRaised.copy(alpha = if (active) 0.95f else 0.7f), shape)
                    .border(
                        width = 1.dp,
                        color = if (active) AntiqueGold else NeutralSeal,
                        shape = shape
                    )
                    .semantics { stateDescription = if (active) selectedLabel else unselectedLabel }
                    .clickable {
                        if (requireConfirm) chosen = index else onName(index)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CourtierMedallion(
                    courtierIndex = index,
                    side = 40.dp,
                    rim = if (active) Candlelight else NeutralSeal,
                    tint = if (maskColours) MaskColours[index % MaskColours.size] else null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(CourtLabels.nameOf(index)),
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(CourtLabels.maskOf(index)),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrimaryButton(
                text = stringResource(R.string.cta_name_heir),
                onClick = { onName(chosen) },
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                text = stringResource(R.string.cta_withdraw),
                onClick = onDismiss,
                accent = Crimson,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
