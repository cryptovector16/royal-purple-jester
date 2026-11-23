package com.Faprire4.Kobmif.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CornerFlourish
import com.Faprire4.Kobmif.core.ui.CourtBackdrop

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val model: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val ready by model.finished.collectAsStateWithLifecycle()
    val motion by model.motion.collectAsStateWithLifecycle()

    LaunchedEffect(ready) {
        if (ready) onFinished()
    }

    SplashContent(motion = motion)
}

@Composable
private fun SplashContent(motion: Boolean) {
    val transition = rememberInfiniteTransition(label = "splash")
    val filigree by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing)),
        label = "filigree"
    )
    val candle by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "candle"
    )
    val breath by transition.animateFloat(
        initialValue = 0.975f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )
    val glowAlpha = if (motion) candle else 0.78f
    val crestScale = if (motion) breath else 1f
    val arcAngle = if (motion) filigree else 24f

    CourtBackdrop(
        overlay = listOf(
            Color(0xF007070A),
            Color(0xE61C0E14),
            Color(0xF507070A)
        ),
        glow = Candlelight.copy(alpha = 0.12f)
    ) {
        CornerFlourish(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 56.dp, start = 14.dp)
        )
        CornerFlourish(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 56.dp, end = 14.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(248.dp)
                        .alpha(glowAlpha)
                        .background(
                            Brush.radialGradient(
                                listOf(Candlelight.copy(alpha = 0.22f), Color.Transparent)
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .border(1.dp, AntiqueGold.copy(alpha = 0.42f), CircleShape)
                )
                Canvas(
                    modifier = Modifier
                        .size(198.dp)
                        .rotate(arcAngle)
                        .alpha(0.2f)
                ) {
                    drawArc(
                        color = AntiqueGold,
                        startAngle = 0f,
                        sweepAngle = 254f,
                        useCenter = false,
                        style = Stroke(width = 3f)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(168.dp)
                        .scale(crestScale)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall,
                color = AntiqueGold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(38.dp))
            LinearProgressIndicator(
                color = AntiqueGold,
                trackColor = SurfaceRaised,
                modifier = Modifier
                    .width(176.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
