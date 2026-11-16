package com.Faprire4.Kobmif.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.GoldHairline
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy

@Composable
fun EngravedCard(
    modifier: Modifier = Modifier,
    accent: Color = AntiqueGold,
    borderWidth: Dp = 1.dp,
    corner: Dp = 18.dp,
    elevation: Dp = 10.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    Box(
        modifier = modifier
            .shadow(elevation, shape, ambientColor = accent, spotColor = Color.Black)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(SurfaceRaised, VelvetBurgundy)))
            .border(borderWidth, accent.copy(alpha = 0.45f), shape)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val inset = 6.dp.toPx()
            if (size.width > inset * 2 && size.height > inset * 2) {
                drawRoundRect(
                    color = GoldHairline,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - inset * 2, size.height - inset * 2),
                    cornerRadius = CornerRadius(corner.toPx() * 0.6f, corner.toPx() * 0.6f),
                    style = Stroke(width = 1f)
                )
            }
        }
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
