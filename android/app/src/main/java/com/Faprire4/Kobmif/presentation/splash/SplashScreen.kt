package com.Faprire4.Kobmif.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.Faprire4.Kobmif.core.theme.GlassCourt
import com.Faprire4.Kobmif.core.theme.InkBlack
import com.Faprire4.Kobmif.core.theme.ScrimDeep
import com.Faprire4.Kobmif.core.theme.ScrimMid
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import com.Faprire4.Kobmif.core.theme.VelvetHighlight
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }

    val medallionScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.84f,
        animationSpec = tween(760, easing = FastOutSlowInEasing),
        label = "medallionScale"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(520),
        label = "splashAlpha"
    )

    val transition = rememberInfiniteTransition(label = "splashLoops")
    val flicker by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1700), RepeatMode.Reverse),
        label = "candleFlicker"
    )
    val ringSpin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(26000), RepeatMode.Restart),
        label = "pipRing"
    )
    val float by transition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(2400), RepeatMode.Reverse),
        label = "candleFloat"
    )

    Box(modifier = modifier.fillMaxSize().background(InkBlack)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(ScrimDeep, ScrimMid, ScrimDeep)))
        )
        Canvas(modifier = Modifier.fillMaxSize().alpha(flicker)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Candlelight.copy(alpha = 0.18f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height * 0.42f),
                    radius = 620f
                ),
                radius = 620f,
                center = Offset(size.width / 2f, size.height * 0.42f)
            )
        }
        Canvas(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.Center)
                .rotate(ringSpin)
                .alpha(0.5f * contentAlpha)
        ) {
            val radius = size.minDimension / 2f
            repeat(12) { index ->
                val angle = 2.0 * PI * index / 12.0
                drawCircle(
                    color = AntiqueGold.copy(alpha = 0.55f),
                    radius = 3.2f,
                    center = Offset(
                        size.width / 2f + radius * cos(angle).toFloat(),
                        size.height / 2f + radius * sin(angle).toFloat()
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = GlassCourt,
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(listOf(AntiqueGold, VelvetHighlight, AntiqueGold))
                ),
                modifier = Modifier
                    .size(188.dp)
                    .scale(medallionScale)
                    .alpha(contentAlpha)
                    .shadow(26.dp, RoundedCornerShape(28.dp), ambientColor = AntiqueGold)
                    .clip(RoundedCornerShape(28.dp))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(136.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall,
                color = Candlelight,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(contentAlpha)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(contentAlpha)
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                color = AntiqueGold,
                trackColor = VelvetBurgundy,
                modifier = Modifier
                    .width(184.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }

        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 46.dp)
                .size(96.dp)
                .alpha(flicker)
                .graphicsLayer { translationY = float }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter)
                .background(Brush.horizontalGradient(listOf(Color.Transparent, AntiqueGold, Color.Transparent)))
        )
    }
}