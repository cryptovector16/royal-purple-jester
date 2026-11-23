package com.Faprire4.Kobmif.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.InkSilhouette
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextSecondary

@Composable
fun ScreenHeader(
    title: String,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Int? = null,
    trailingDescription: String? = null,
    onTrailing: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(InkSilhouette.copy(alpha = 0.46f))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .background(SurfaceRaised.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                .border(1.dp, AntiqueGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable(onClick = onMenu)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_menu),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.cta_menu),
                style = MaterialTheme.typography.titleSmall,
                color = AntiqueGold,
                maxLines = 1
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        if (trailingIcon != null && onTrailing != null) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SurfaceRaised.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                    .border(1.dp, AntiqueGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .clickable(onClick = onTrailing),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(trailingIcon),
                    contentDescription = trailingDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
fun HeaderUnderline(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(AntiqueGold.copy(alpha = 0.24f))
    )
}
