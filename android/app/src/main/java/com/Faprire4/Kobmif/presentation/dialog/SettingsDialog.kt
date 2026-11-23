package com.Faprire4.Kobmif.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.Crimson
import com.Faprire4.Kobmif.core.theme.NeutralSeal
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.VelvetDeep
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.domain.model.CourtSettings

@Composable
fun SettingsDialog(
    settings: CourtSettings,
    onToggleConfirm: () -> Unit,
    onToggleMotion: () -> Unit,
    onToggleMasks: () -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    CourtDialog(title = stringResource(R.string.settings_title), onDismiss = onDismiss) {
        SettingRow(
            label = stringResource(R.string.settings_confirm),
            hint = stringResource(R.string.settings_confirm_hint),
            checked = settings.confirmAccusation,
            onToggle = onToggleConfirm
        )
        SettingRow(
            label = stringResource(R.string.settings_motion),
            hint = stringResource(R.string.settings_motion_hint),
            checked = settings.candleMotion,
            onToggle = onToggleMotion
        )
        SettingRow(
            label = stringResource(R.string.settings_masks),
            hint = stringResource(R.string.settings_masks_hint),
            checked = settings.maskColours,
            onToggle = onToggleMasks
        )
        SecondaryButton(
            text = stringResource(R.string.settings_reset),
            onClick = onReset,
            accent = Crimson,
            modifier = Modifier.fillMaxWidth()
        )
        SecondaryButton(
            text = stringResource(R.string.cta_close),
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SettingRow(
    label: String,
    hint: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(SurfaceRaised.copy(alpha = 0.85f), shape)
            .border(1.dp, AntiqueGold.copy(alpha = 0.26f), shape)
            .semantics { stateDescription = if (checked) onLabel else offLabel }
            .clickable(onClick = onToggle)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
        Box(
            modifier = Modifier
                .width(52.dp)
                .heightIn(min = 28.dp)
                .size(width = 52.dp, height = 28.dp)
                .background(
                    if (checked) AntiqueGold.copy(alpha = 0.35f) else VelvetDeep,
                    CircleShape
                )
                .border(1.dp, if (checked) Candlelight else NeutralSeal, CircleShape),
            contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(22.dp)
                    .background(if (checked) Candlelight else NeutralSeal, CircleShape)
            )
        }
    }
}
