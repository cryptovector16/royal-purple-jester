package com.Faprire4.Kobmif.presentation.rules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CourtBackdrop
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.EngravedPlate
import com.Faprire4.Kobmif.core.ui.HeaderUnderline
import com.Faprire4.Kobmif.core.ui.ScreenHeader
import com.Faprire4.Kobmif.core.ui.SecondaryButton

@Composable
fun RulesScreen(onMenu: () -> Unit) {
    CourtBackdrop(
        overlay = listOf(
            Color(0xDC07070A),
            Color(0xD11C0E14),
            Color(0xE607070A)
        ),
        glow = Candlelight.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.rules_title),
                onMenu = onMenu
            )
            HeaderUnderline()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (index in 0 until 5) {
                    RuleStep(index = index)
                }
                EngravedPlate(modifier = Modifier.fillMaxWidth(), radius = 14.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.rules_footer),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.disclaimer),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun RuleStep(index: Int) {
    EngravedPlate(modifier = Modifier.fillMaxWidth(), radius = 14.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(CourtLabels.ruleIcon(index)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(CourtLabels.ruleTitle(index)),
                    style = MaterialTheme.typography.titleSmall,
                    color = AntiqueGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(CourtLabels.ruleBody(index)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }
        }
    }
}
