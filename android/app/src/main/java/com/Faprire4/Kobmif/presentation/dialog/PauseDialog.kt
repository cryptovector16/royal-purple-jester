package com.Faprire4.Kobmif.presentation.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.GoldBorder
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = SurfaceRaised,
        titleContentColor = Candlelight,
        textContentColor = TextSecondary,
        iconContentColor = GoldBorder,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_body),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(R.string.resume),
                onClick = onResume,
                height = 48.dp
            )
        },
        dismissButton = {
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                height = 48.dp
            )
        }
    )
}
