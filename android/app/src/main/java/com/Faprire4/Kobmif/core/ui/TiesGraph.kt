package com.Faprire4.Kobmif.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.InertBorder
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class TieState {
    HIDDEN,
    SUSPECTED,
    CONFIRMED
}

data class TieNodeUi(
    val initial: String,
    val name: String,
    val unmasked: Boolean
)

data class TieEdgeUi(
    val fromIndex: Int,
    val toIndex: Int,
    val label: String,
    val state: TieState
)

@Composable
fun TiesGraph(
    nodes: List<TieNodeUi>,
    edges: List<TieEdgeUi>,
    steady: Boolean,
    modifier: Modifier = Modifier
) {
    val confirmedCount = edges.count { it.state == TieState.CONFIRMED }
    val reveal by animateFloatAsState(
        targetValue = if (confirmedCount > 0) 1f else 0f,
        animationSpec = tween(if (steady) 0 else 420),
        label = "tieReveal"
    )
    BoxWithConstraints(modifier = modifier) {
        val widthDp = maxWidth
        val heightDp = maxHeight
        val radius: Dp = (if (widthDp < heightDp) widthDp else heightDp) * 0.32f
        val centerX = widthDp / 2
        val centerY = heightDp / 2
        val nodeSize = 56.dp
        val positions = nodes.indices.map { index ->
            val angle = (-PI / 2) + (2 * PI * index / nodes.size.coerceAtLeast(1))
            val dx = radius * cos(angle).toFloat()
            val dy = radius * sin(angle).toFloat()
            Pair(centerX + dx, centerY + dy)
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            edges.forEach { edge ->
                if (edge.state == TieState.HIDDEN) {
                    return@forEach
                }
                val from = positions.getOrNull(edge.fromIndex) ?: return@forEach
                val to = positions.getOrNull(edge.toIndex) ?: return@forEach
                val start = Offset(from.first.toPx(), from.second.toPx())
                val end = Offset(to.first.toPx(), to.second.toPx())
                if (edge.state == TieState.CONFIRMED) {
                    drawLine(
                        color = AntiqueGold,
                        start = start,
                        end = Offset(
                            start.x + (end.x - start.x) * (0.3f + 0.7f * reveal),
                            start.y + (end.y - start.y) * (0.3f + 0.7f * reveal)
                        ),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                } else {
                    drawLine(
                        color = TextMuted,
                        start = start,
                        end = end,
                        strokeWidth = 2f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 12f), 0f)
                    )
                }
            }
        }
        nodes.forEachIndexed { index, node ->
            val position = positions.getOrNull(index) ?: return@forEachIndexed
            Box(
                modifier = Modifier
                    .offset(x = position.first - nodeSize / 2, y = position.second - nodeSize / 2)
                    .size(nodeSize)
                    .background(
                        Brush.verticalGradient(listOf(SurfaceRaised, VelvetBurgundy)),
                        CircleShape
                    )
                    .border(
                        width = if (node.unmasked) 2.dp else 1.dp,
                        color = if (node.unmasked) Candlelight else InertBorder,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = node.initial,
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (node.unmasked) Candlelight else TextSecondary
                )
            }
        }
        edges.forEach { edge ->
            if (edge.state != TieState.CONFIRMED) {
                return@forEach
            }
            val from = positions.getOrNull(edge.fromIndex) ?: return@forEach
            val to = positions.getOrNull(edge.toIndex) ?: return@forEach
            val midX = (from.first + to.first) / 2
            val midY = (from.second + to.second) / 2
            Text(
                text = edge.label,
                style = MaterialTheme.typography.labelSmall,
                color = AntiqueGold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.offset(x = midX - 30.dp, y = midY - 8.dp)
            )
        }
    }
}
