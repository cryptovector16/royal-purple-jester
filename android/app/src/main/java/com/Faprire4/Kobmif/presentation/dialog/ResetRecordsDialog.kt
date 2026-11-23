package com.Faprire4.Kobmif.presentation.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.Crimson
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.SecondaryButton

@Composable
fun ResetRecordsDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CourtDialog(title = stringResource(R.string.reset_title), onDismiss = onDismiss) {
        Text(
            text = stringResource(R.string.reset_body),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        SecondaryButton(
            text = stringResource(R.string.cta_burn_records),
            onClick = onConfirm,
            accent = Crimson,
            modifier = Modifier.fillMaxWidth()
        )
        SecondaryButton(
            text = stringResource(R.string.cta_keep_records),
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
