package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Modern Apple VisionOS Frosted Glass System Settings & Subsystems
 */
@Composable
fun SettingsScreen(
    state: UiState,
    onToggleSound: (Boolean) -> Unit,
    onToggleHaptic: (Boolean) -> Unit,
    onReset: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp, 24.dp, 18.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                // Frosted Glass Luxury Eyebrow Pill
                FrostedGlassPill(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = GoldPrimary.copy(alpha = 0.14f),
                    borderBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                        listOf(GoldLight, GoldPrimary, SilverPlatinum.copy(alpha = 0.4f))
                    )
                ) {
                    Text(
                        "✦ EXECUTIVE SUBSYSTEMS // CONFIGURATION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default,
                        color = GoldChampagne,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "System Control.",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    "Calibrate haptic telemetry sensors, audio synthesis, and security parameters.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Subsystems Frosted Glass Card
        item {
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                backgroundColor = FrostedGlassSurface,
                borderBrush = GlassBorderGradient,
                elevation = 12.dp,
                hasSpecularHighlight = true
            ) {
                Box(modifier = Modifier.padding(20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        FuturisticSettingRow(
                            icon = Icons.Default.VolumeUp,
                            title = "Real Tractor Audio Engine",
                            subtitle = "Dynamic diesel piston chug-chug sound synthesis",
                            checked = state.soundEnabled,
                            onCheckedChange = onToggleSound
                        )

                        HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                        FuturisticSettingRow(
                            icon = Icons.Default.Vibration,
                            title = "Engine Haptic Telemetry",
                            subtitle = "Tactile mechanical vibration pulse on session triggers",
                            checked = state.hapticEnabled,
                            onCheckedChange = onToggleHaptic
                        )
                    }
                }
            }
        }

        // Factory Reset & Security Section
        item {
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                backgroundColor = Color(0x35280F16),
                borderBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(DangerRed.copy(alpha = 0.5f), DangerRed.copy(alpha = 0.2f))
                ),
                borderWidth = 1.dp,
                elevation = 10.dp,
                hasSpecularHighlight = true,
                specularAlpha = 0.08f
            ) {
                Box(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(DangerRed)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Factory Purge",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Default,
                                    color = DangerRed
                                )
                            }
                            Text(
                                "Permanently wipe all tractor telemetries and flight records",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.Default,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 4.dp, end = 12.dp)
                            )
                        }

                        Button(
                            onClick = onReset,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DangerRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.appleSpringButton(
                                pressedScale = 0.91f,
                                hapticType = AppleHapticType.ERROR
                            )
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "RESET",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Default,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // Spatial Firmware Telemetry Chip
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "FIELD COMMAND OS // SPATIAL BUILD 3.8",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Default,
                        color = TextMuted.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        "Apple VisionOS Spatial UI Architecture",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Default,
                        color = TextMuted.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FuturisticSettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .applePressEffect(pressedScale = 0.985f, hapticType = AppleHapticType.SELECTION) {
                onCheckedChange(!checked)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .frostedGlass(
                        shape = RoundedCornerShape(13.dp),
                        backgroundColor = if (checked) Amber.copy(alpha = 0.16f) else FrostedGlassElevated,
                        borderBrush = if (checked) {
                            androidx.compose.ui.graphics.Brush.linearGradient(listOf(Amber, AmberLight))
                        } else GlassBorderGradient,
                        borderWidth = 1.dp,
                        hasSpecularHighlight = true,
                        specularAlpha = 0.12f
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (checked) Amber else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    color = Color.White
                )
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Amber,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = SurfaceLight
            )
        )
    }
}
