package com.Faprire4.Kobmif.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.HeaderScrim

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    leadingRes: Int? = null,
    leadingDescription: String? = null,
    onLeading: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HeaderScrim)
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            if (leadingRes != null && onLeading != null) {
                IconButton(onClick = onLeading, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(leadingRes),
                        contentDescription = leadingDescription,
                        tint = AntiqueGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Candlelight,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        Box(
            modifier = Modifier.size(width = 72.dp, height = 48.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (trailing != null) {
                trailing()
            }
        }
    }
}
