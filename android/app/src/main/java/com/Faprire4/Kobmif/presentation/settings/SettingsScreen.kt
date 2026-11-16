package com.Faprire4.Kobmif.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.GoldBorder
import com.Faprire4.Kobmif.core.theme.InertBorder
import com.Faprire4.Kobmif.core.theme.InkBlack
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import com.Faprire4.Kobmif.core.ui.EngravedCard
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.core.ui.SectionHeader

@Composable
fun SettingsScreen(
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)

    Box(modifier = modifier.fillMaxSize().background(InkBlack)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(InkBlack, SurfaceRaised, InkBlack))
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            SectionHeader(
                title = stringResource(R.string.settings),
                leadingRes = R.drawable.ic_back,
                leadingDescription = stringResource(R.string.cd_back),
                onLeading = onMenu
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                EngravedCard(modifier = Modifier.fillMaxWidth(), contentPadding = 14.dp) {
                    OptionHeading(
                        title = stringResource(R.string.setting_inquiry_length),
                        description = stringResource(R.string.setting_inquiry_desc)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SegmentChip(
                            label = stringResource(R.string.length_short),
                            selected = state.questionsPerCase == GameConfig.QUESTIONS_SHORT,
                            onClick = { settingsViewModel.onInquiryLength(GameConfig.QUESTIONS_SHORT) },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentChip(
                            label = stringResource(R.string.length_court),
                            selected = state.questionsPerCase == GameConfig.QUESTIONS_COURT,
                            onClick = { settingsViewModel.onInquiryLength(GameConfig.QUESTIONS_COURT) },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentChip(
                            label = stringResource(R.string.length_long),
                            selected = state.questionsPerCase == GameConfig.QUESTIONS_LONG,
                            onClick = { settingsViewModel.onInquiryLength(GameConfig.QUESTIONS_LONG) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                ToggleOption(
                    title = stringResource(R.string.setting_seal_hints),
                    description = stringResource(R.string.setting_seal_hints_desc),
                    checked = state.sealHints,
                    stateLabel = if (state.sealHints) onLabel else offLabel,
                    onCheckedChange = { settingsViewModel.onSealHints(it) }
                )
                ToggleOption(
                    title = stringResource(R.string.setting_steady),
                    description = stringResource(R.string.setting_steady_desc),
                    checked = state.steadyCandlelight,
                    stateLabel = if (state.steadyCandlelight) onLabel else offLabel,
                    onCheckedChange = { settingsViewModel.onSteadyCandlelight(it) }
                )
                ToggleOption(
                    title = stringResource(R.string.setting_auto_filter),
                    description = stringResource(R.string.setting_auto_filter_desc),
                    checked = state.autoFilterBoard,
                    stateLabel = if (state.autoFilterBoard) onLabel else offLabel,
                    onCheckedChange = { settingsViewModel.onAutoFilterBoard(it) }
                )
            }
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun OptionHeading(title: String, description: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        color = TextPrimary
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = description,
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted
    )
}

@Composable
private fun ToggleOption(
    title: String,
    description: String,
    checked: Boolean,
    stateLabel: String,
    onCheckedChange: (Boolean) -> Unit
) {
    EngravedCard(modifier = Modifier.fillMaxWidth(), contentPadding = 14.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OptionHeading(title = title, description = description)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = InkBlack,
                    checkedTrackColor = AntiqueGold,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = VelvetBurgundy,
                    uncheckedBorderColor = InertBorder
                ),
                modifier = Modifier.semantics { stateDescription = stateLabel }
            )
        }
    }
}

@Composable
private fun SegmentChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .background(if (selected) VelvetBurgundy else SurfaceRaised, shape)
            .border(1.dp, if (selected) GoldBorder else InertBorder, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Candlelight else TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
