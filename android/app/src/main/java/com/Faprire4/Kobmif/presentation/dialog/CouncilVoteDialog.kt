package com.Faprire4.Kobmif.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.GoldBorder
import com.Faprire4.Kobmif.core.theme.InertBorder
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton

data class VoteOption(
    val id: Int,
    val name: String,
    val markLabel: String,
    val motive: String?
)

@Composable
fun CouncilVoteDialog(
    options: List<VoteOption>,
    selectedId: Int?,
    onSelect: (Int) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = SurfaceRaised,
        titleContentColor = Candlelight,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.name_the_heir),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.vote_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))
                options.forEach { option ->
                    VoteRow(
                        option = option,
                        selected = option.id == selectedId,
                        onSelect = { onSelect(option.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(R.string.confirm),
                onClick = onConfirm,
                enabled = selectedId != null,
                height = 48.dp
            )
        },
        dismissButton = {
            SecondaryButton(
                text = stringResource(R.string.cancel),
                onClick = onCancel,
                height = 48.dp
            )
        }
    )
}

@Composable
private fun VoteRow(
    option: VoteOption,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .background(VelvetBurgundy, shape)
            .border(if (selected) 2.dp else 1.dp, if (selected) GoldBorder else InertBorder, shape)
            .clickable(onClick = onSelect)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .semantics { stateDescription = option.markLabel },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_2),
            contentDescription = stringResource(R.string.cd_mask_fmt, option.name),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.name,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) Candlelight else TextPrimary
            )
            if (option.motive != null) {
                Text(
                    text = option.motive,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    maxLines = 2
                )
            }
        }
    }
}
