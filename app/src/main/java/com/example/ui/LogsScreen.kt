package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modern Apple VisionOS Frosted Glass Telemetry Session Logs
 */
@Composable
fun LogsScreen(state: UiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp, 24.dp, 18.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 6.dp)) {
                // Frosted Glass Luxury Eyebrow Pill
                FrostedGlassPill(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = GoldPrimary.copy(alpha = 0.14f),
                    borderBrush = Brush.linearGradient(listOf(GoldLight, GoldPrimary, SilverPlatinum.copy(alpha = 0.4f)))
                ) {
                    Text(
                        "✦ CHRONO ARCHIVE // FLIGHT RECORDER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default,
                        color = GoldChampagne,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            "Session Telemetry.",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            "Encrypted operational logs and tractor engine sessions",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Default,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .frostedGlass(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = Amber.copy(alpha = 0.16f),
                                borderBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                                    listOf(Amber.copy(alpha = 0.45f), Amber.copy(alpha = 0.15f))
                                ),
                                borderWidth = 1.dp,
                                hasSpecularHighlight = false
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            "${state.records.size} LOGGED",
                            fontSize = 10.sp,
                            color = Amber,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            letterSpacing = 0.6.sp
                        )
                    }
                }
            }
        }
        
        if (state.records.isEmpty()) {
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = FrostedGlassSurface,
                    borderBrush = GlassBorderGradient,
                    elevation = 10.dp,
                    hasSpecularHighlight = true
                ) {
                    Box(
                        modifier = Modifier.padding(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🛰", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "No Telemetry Recorded Yet",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,
                                color = Color.White
                            )
                            Text(
                                "Start tractor sessions from the Fleet Grid. Detailed encrypted records will populate here automatically upon engine termination.",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.Default,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            }
        } else {
            items(state.records) { record ->
                FrostedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .applePressEffect(pressedScale = 0.98f, hapticType = AppleHapticType.SELECTION),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = FrostedGlassSurface,
                    borderBrush = GlassBorderGradient,
                    elevation = 6.dp,
                    hasSpecularHighlight = true
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .frostedGlass(
                                        shape = RoundedCornerShape(14.dp),
                                        backgroundColor = Amber.copy(alpha = 0.16f),
                                        borderBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                                            listOf(Amber.copy(alpha = 0.45f), Amber.copy(alpha = 0.15f))
                                        ),
                                        borderWidth = 1.dp,
                                        hasSpecularHighlight = true,
                                        specularAlpha = 0.12f
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🚜", fontSize = 20.sp)
                            }
                            
                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    record.machineName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Default,
                                    color = Color.White
                                )
                                Text(
                                    "${record.action} • Runtime: ${formatTime(record.duration)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily.Default,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            
                            val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(record.time))
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    timeStr,
                                    fontSize = 13.sp,
                                    color = Amber,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Default,
                                    letterSpacing = 0.2.sp
                                )
                                Text(
                                    "VERIFIED",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Default,
                                    color = OkGreen,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.4.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
