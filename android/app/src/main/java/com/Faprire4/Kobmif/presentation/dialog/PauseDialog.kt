package com.Faprire4.Kobmif.presentation.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRules: () -> Unit,
    onMenu: () -> Unit
) {
    CourtDialog(title = stringResource(R.string.pause_title), onDismiss = onResume) {
        Text(
            text = stringResource(R.string.pause_body),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        PrimaryButton(
            text = stringResource(R.string.cta_resume),
            onClick = onResume,
            modifier = Modifier.fillMaxWidth()
        )
        SecondaryButton(
            text = stringResource(R.string.cta_rules),
            onClick = onRules,
            modifier = Modifier.fillMaxWidth()
        )
        SecondaryButton(
            text = stringResource(R.string.cta_menu),
            onClick = onMenu,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
