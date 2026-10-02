package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MachineEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modern Luxury Apple VisionOS-style Home Dashboard
 * Incorporates fine gold & platinum silver metallic materials, frosted glass substrates,
 * and high-jewelry luxury elements.
 */
@Composable
fun HomeScreen(
    state: UiState,
    weeklyTrend: WeeklyTrendData,
    onToggle: (MachineEntity) -> Unit,
    onEdit: (MachineEntity) -> Unit,
    onSelectTrendMachine: (String?) -> Unit,
    onNavigateToTrends: () -> Unit
) {
    val totalTime = state.machines.sumOf { 
        it.totalTime + if (it.isRunning && it.startAt != null) (state.currentTime - it.startAt) / 1000 else 0 
    }
    val activeCount = state.machines.count { it.isRunning }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp, 16.dp, 18.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Luxury Spatial Header
        item {
            Column(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                // Gold & Silver Luxury Eyebrow Pill
                FrostedGlassPill(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = if (activeCount > 0) GoldPrimary.copy(alpha = 0.16f) else FrostedGlassPill,
                    borderColor = if (activeCount > 0) GoldLight.copy(alpha = 0.50f) else SilverTitanium.copy(alpha = 0.35f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        val infiniteTransition = rememberInfiniteTransition(label = "hudPulse")
                        val pulseAlpha by infiniteTransition.animateFloat(
                            initialValue = 0.5f,
                            targetValue = 1.0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(900, easing = AppleFluidSpringCubic),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "hudPulseAlpha"
                        )
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (activeCount > 0) GoldLight.copy(alpha = pulseAlpha) else SilverPlatinum.copy(alpha = pulseAlpha))
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            if (activeCount > 0) "✦ GOLD EDITION // ACTIVE HARVEST [LIVE]" else "✦ GOLD & PLATINUM // FLEET GRID [READY]",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            color = if (activeCount > 0) GoldChampagne else SilverPlatinum,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Field Command.",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    "Bespoke autonomous tractor telemetry, fine engine quotas & precision field operation.",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        
        // Luxury Frosted Glass Dashboard Card
        item {
            FuturisticDashboardCard(state = state, totalTime = totalTime)
        }

        // Luxury Frosted Glass Weekly Trends Card Preview
        item {
            WeeklyTrendsCard(
                weeklyTrend = weeklyTrend,
                machines = state.machines,
                onSelectMachine = onSelectTrendMachine,
                onExpandAnalytics = onNavigateToTrends
            )
        }
        
        // Fleet Header Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Fleet Units",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default,
                        color = Color.White,
                        letterSpacing = (-0.2).sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .frostedGlass(
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = GoldPrimary.copy(alpha = 0.16f),
                                borderBrush = Brush.linearGradient(listOf(GoldLight, GoldPrimary, SilverPlatinum.copy(alpha = 0.3f))),
                                borderWidth = 1.dp,
                                hasSpecularHighlight = false
                            )
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "${state.machines.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = GoldLight
                        )
                    }
                }

                Text(
                    "✦ PLATINUM & GOLD TELEMETRY",
                    fontSize = 10.sp,
                    color = GoldChampagne,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    letterSpacing = 0.8.sp
                )
            }
        }
        
        // Empty State or List of Luxury Machine Cards
        if (state.machines.isEmpty()) {
            item {
                FuturisticEmptyFleetCard()
            }
        } else {
            items(state.machines, key = { it.id }) { machine ->
                FuturisticMachineCard(
                    machine = machine,
                    currentTime = state.currentTime,
                    onToggle = onToggle,
                    onEdit = { onEdit(machine) }
                )
            }
        }
    }
}

/**
 * Luxury Frosted Glass Dashboard Card with Fine Gold & Silver Metallics
 */
@Composable
fun FuturisticDashboardCard(state: UiState, totalTime: Long) {
    val activeCount = state.machines.count { it.isRunning }
    val totalCount = state.machines.size
    val activeRatio = if (totalCount > 0) activeCount.toFloat() / totalCount.toFloat() else 0f
    val todayDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()).uppercase()
    
    // Dynamic circular capacity arc animation
    val animatedRatio by animateFloatAsState(
        targetValue = activeRatio,
        animationSpec = AppleSpringSmooth,
        label = "activeRatio"
    )

    FrostedGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .applePressEffect(pressedScale = 0.985f, hapticType = AppleHapticType.IMPACT_LIGHT),
        shape = RoundedCornerShape(28.dp),
        backgroundColor = FrostedGlassSurface,
        borderBrush = GlassBorderGradient,
        elevation = 16.dp,
        hasSpecularHighlight = true
    ) {
        Box(modifier = Modifier.padding(22.dp)) {
            Column {
                // Top Meta Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .frostedGlass(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = GoldPrimary.copy(alpha = 0.16f),
                                borderBrush = Brush.linearGradient(listOf(GoldLight, GoldPrimary, SilverPlatinum.copy(alpha = 0.4f))),
                                borderWidth = 1.dp,
                                hasSpecularHighlight = true,
                                specularAlpha = 0.10f
                            )
                            .padding(horizontal = 11.dp, vertical = 5.dp)
                    ) {
                        Text(
                            "✦ $todayDate • HAUTE TELEMETRY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            color = GoldChampagne,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Sensors,
                            contentDescription = null,
                            tint = if (activeCount > 0) GoldLight else SilverTitanium,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            if (activeCount > 0) "CHRONO SYNCED" else "STATIONARY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Default,
                            color = if (activeCount > 0) GoldLight else SilverTitanium,
                            letterSpacing = 0.4.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Core Telemetry Display: Runtime Odometer + Gold & Silver Circular Gauge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "EXECUTIVE FLEET RUNTIME",
                            fontSize = 10.sp,
                            color = SilverTitanium,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            letterSpacing = 0.8.sp
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                formatTime(totalTime),
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                color = Color.White,
                                letterSpacing = (-0.5).sp
                            )
                        }
                        Text(
                            "Precision telemetry across royal sectors",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Default,
                            color = TextMuted
                        )
                    }

                    // Gold & Platinum Circular Gauge
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val infiniteTransition = rememberInfiniteTransition(label = "gaugeRotation")
                        val rotation by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(12000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "gaugeRotationAngle"
                        )

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 6.5.dp.toPx()
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val radius = (size.minDimension - strokeWidth) / 2f

                            // Brushed platinum background track
                            drawCircle(
                                color = Color.White.copy(alpha = 0.09f),
                                radius = radius,
                                center = center,
                                style = Stroke(width = strokeWidth)
                            )

                            // Active Gold & Platinum metallic sweep
                            val sweepAngle = (animatedRatio.coerceIn(0.04f, 1f)) * 360f
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(GoldLight, GoldPrimary, SilverPlatinum, GoldDark, GoldLight),
                                    center = center
                                ),
                                startAngle = -90f + (if (activeCount > 0) rotation * 0.1f else 0f),
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (totalCount > 0) "${(animatedRatio * 100).toInt()}%" else "0%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                color = if (activeCount > 0) GoldLight else Color.White
                            )
                            Text(
                                "QUOTA",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,
                                color = SilverTitanium,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3 Luxury Stat Capsules in Gold & Silver
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FuturisticStatCapsule(
                        value = totalCount.toString(),
                        label = "UNITS",
                        sub = "Fleet Grid",
                        accentColor = SilverPlatinum,
                        modifier = Modifier.weight(1f)
                    )
                    FuturisticStatCapsule(
                        value = activeCount.toString(),
                        label = "ONLINE",
                        sub = "Harvesting",
                        accentColor = GoldPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    FuturisticStatCapsule(
                        value = state.records.size.toString(),
                        label = "CHRONO",
                        sub = "Sessions",
                        accentColor = GoldChampagne,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Luxury Frosted Glass Stat Capsule with Gold/Silver accents
 */
@Composable
fun FuturisticStatCapsule(
    value: String,
    label: String,
    sub: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .frostedGlass(
                shape = RoundedCornerShape(20.dp),
                backgroundColor = FrostedGlassElevated,
                borderBrush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.35f),
                        accentColor.copy(alpha = 0.40f),
                        Color.White.copy(alpha = 0.06f)
                    )
                ),
                borderWidth = 1.dp,
                hasSpecularHighlight = true,
                specularAlpha = 0.12f
            )
            .applePressEffect(pressedScale = 0.95f, hapticType = AppleHapticType.SELECTION)
            .padding(12.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    label,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Default,
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.7.sp
                )
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                color = TextLight
            )
            Text(
                sub,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                color = TextMuted
            )
        }
    }
}

/**
 * Luxury Cockpit-Grade Machine Card with Gold & Silver Fine Trim
 */
@Composable
fun FuturisticMachineCard(
    machine: MachineEntity,
    currentTime: Long,
    onToggle: (MachineEntity) -> Unit,
    onEdit: () -> Unit
) {
    val isRunning = machine.isRunning
    val displayTime = machine.totalTime + if (isRunning && machine.startAt != null) (currentTime - machine.startAt) / 1000 else 0
    
    val borderGradient = if (isRunning) ActiveMachineBorderGradient else GlassBorderGradient
    val cardBackground = if (isRunning) {
        Color(0x3D2E2108) // Warm amber-gold glow
    } else {
        FrostedGlassSurface
    }

    // Gold radar pulsing animation for running tractor
    val infiniteTransition = rememberInfiniteTransition(label = "tractorRadar")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = AppleFluidSpringCubic),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarPulse"
    )
    val radarAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = AppleFluidSpringCubic),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarAlpha"
    )

    FrostedGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .applePressEffect(pressedScale = 0.985f, hapticType = AppleHapticType.SELECTION),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = cardBackground,
        borderBrush = borderGradient,
        borderWidth = if (isRunning) 1.5.dp else 1.dp,
        elevation = if (isRunning) 18.dp else 8.dp,
        hasSpecularHighlight = true,
        specularAlpha = if (isRunning) 0.18f else 0.12f
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            Column {
                // Header with Status Radar & Edit Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Gold Beacon
                            Box(
                                modifier = Modifier.size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isRunning) {
                                    // Outer gold radar pulse wave
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp * radarPulse)
                                            .clip(CircleShape)
                                            .background(GoldPrimary.copy(alpha = radarAlpha))
                                    )
                                    // Inner fine gold core
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(GoldPrimary)
                                            .border(2.dp, GoldLight, CircleShape)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(SilverTitanium.copy(alpha = 0.40f))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                machine.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,
                                color = Color.White
                            )
                        }

                        // Luxury Model & Telemetry Tag
                        Text(
                            "✦ ${machine.brand.ifBlank { "BESPOKE" }.uppercase()} • NODE_${machine.id.takeLast(4).uppercase()}",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.4.sp,
                            color = if (isRunning) GoldChampagne else SilverTitanium,
                            modifier = Modifier.padding(start = 32.dp, top = 2.dp)
                        )
                    }

                    // Platinum & Glass Circular Action Button
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(36.dp)
                            .frostedGlass(
                                shape = CircleShape,
                                backgroundColor = Color(0x28FFFFFF),
                                borderBrush = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.45f), SilverTitanium.copy(alpha = 0.20f))),
                                borderWidth = 1.dp,
                                hasSpecularHighlight = false
                            )
                            .applePressEffect(pressedScale = 0.88f)
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Edit Machine",
                            tint = SilverPlatinum,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Telemetry Readout & Luxury Laser Ignition Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Bespoke Tractor Avatar Pod with Gold/Silver Rim
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .frostedGlass(
                                shape = RoundedCornerShape(18.dp),
                                backgroundColor = if (isRunning) GoldPrimary.copy(alpha = 0.18f) else FrostedGlassElevated,
                                borderBrush = if (isRunning) {
                                    Brush.linearGradient(listOf(GoldLight, GoldPrimary, SilverPlatinum.copy(alpha = 0.4f)))
                                } else {
                                    Brush.linearGradient(listOf(SilverPlatinum.copy(alpha = 0.35f), Color.White.copy(alpha = 0.08f)))
                                },
                                borderWidth = 1.dp,
                                hasSpecularHighlight = true,
                                specularAlpha = 0.16f
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚜", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (isRunning) "✦ LIVE GOLD ENGINE" else "RECORDED CHRONO",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isRunning) GoldLight else SilverTitanium,
                            letterSpacing = 0.6.sp
                        )
                        Text(
                            formatTime(displayTime),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = if (isRunning) GoldLight else Color.White,
                            letterSpacing = (-0.3).sp
                        )
                    }

                    // Luxury Vehicle Engine Ignition Button
                    Button(
                        onClick = { onToggle(machine) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRunning) DangerRed else GoldPrimary,
                            contentColor = if (isRunning) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .border(
                                1.dp,
                                if (isRunning) DangerRed.copy(alpha = 0.8f) else GoldLight,
                                RoundedCornerShape(16.dp)
                            )
                            .appleSpringButton(
                                pressedScale = 0.91f,
                                hapticType = if (isRunning) AppleHapticType.ERROR else AppleHapticType.SUCCESS
                            )
                    ) {
                        Text(
                            if (isRunning) "■ STOP" else "▶ IGNITE",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Default,
                            letterSpacing = 0.6.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Luxury Frosted Glass Empty Fleet State
 */
@Composable
fun FuturisticEmptyFleetCard() {
    FrostedGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = FrostedGlassSurface,
        borderBrush = GlassBorderGradient,
        elevation = 10.dp,
        hasSpecularHighlight = true
    ) {
        Box(
            modifier = Modifier.padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .frostedGlass(
                            shape = CircleShape,
                            backgroundColor = GoldPrimary.copy(alpha = 0.14f),
                            borderBrush = Brush.linearGradient(listOf(GoldLight, SilverPlatinum.copy(alpha = 0.5f))),
                            borderWidth = 1.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Speed,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    "Fleet Grid Unoccupied",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    color = Color.White
                )

                Text(
                    "No autonomous tractor nodes detected on the local frequency. Tap the floating gold action orb below to initialize your first bespoke unit.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

/**
 * Time formatting utility
 */
fun formatTime(seconds: Long): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format(Locale.US, "%02d:%02d:%02d", hrs, mins, secs)
}
