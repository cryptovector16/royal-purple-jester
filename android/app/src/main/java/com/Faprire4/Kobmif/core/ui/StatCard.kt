package com.Faprire4.Kobmif.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.core.theme.SurfaceCard
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted

data class StatEntry(
    val label: String,
    val value: String,
    val accent: Color,
    val hideWhenEmpty: Boolean = true
) {
    val visible: Boolean
        get() = !hideWhenEmpty || (value.isNotBlank() && value != "0")
}

@Composable
fun StatCard(entry: StatEntry, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(SurfaceRaised.copy(alpha = 0.94f), SurfaceCard.copy(alpha = 0.92f))
                ),
                shape = shape
            )
            .border(1.dp, entry.accent.copy(alpha = 0.32f), shape)
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(entry.accent, CircleShape)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = entry.value,
            style = MaterialTheme.typography.titleLarge,
            color = entry.accent,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = entry.label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StatRow(entries: List<StatEntry>, modifier: Modifier = Modifier) {
    val visible = entries.filter { it.visible }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visible.forEach { entry ->
            StatCard(entry = entry, modifier = Modifier.weight(1f))
        }
    }
}
