package com.example.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MachineEntity
import com.example.ui.theme.*
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Modern Apple VisionOS Frosted Glass Operational Trends Card
 */
@Composable
fun WeeklyTrendsCard(
    weeklyTrend: WeeklyTrendData,
    machines: List<MachineEntity>,
    onSelectMachine: (String?) -> Unit,
    modifier: Modifier = Modifier,
    onExpandAnalytics: (() -> Unit)? = null
) {
    var selectedDayIndex by remember { mutableIntStateOf(6) } // Default to today (last index)
    val context = LocalContext.current

    // Trigger subtle haptic on day scrub
    LaunchedEffect(selectedDayIndex) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(18, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(18)
        }
    }

    val selectedDay = weeklyTrend.days.getOrNull(selectedDayIndex) ?: weeklyTrend.days.lastOrNull()
    val isAboveAvg = (selectedDay?.runtimeHours ?: 0f) >= weeklyTrend.historicalAverageHours
    val deltaVsBenchmark = (selectedDay?.runtimeHours ?: 0f) - weeklyTrend.historicalAverageHours

    FrostedGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = FrostedGlassSurface,
        borderBrush = GlassBorderGradient,
        elevation = 14.dp,
        hasSpecularHighlight = true
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            Column {
                // Eyebrow and header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FrostedGlassPill(
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = GoldPrimary.copy(alpha = 0.16f),
                        borderBrush = Brush.linearGradient(listOf(GoldLight, GoldPrimary, SilverPlatinum.copy(alpha = 0.4f)))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "✦ HAUTE HORLOGERIE // OPERATIONAL TRENDS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,
                                color = GoldChampagne,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    if (onExpandAnalytics != null) {
                        Surface(
                            onClick = onExpandAnalytics,
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0x33FFFFFF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Full Report",
                                    fontSize = 11.sp,
                                    color = AmberLight,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Default
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = "Expand",
                                    tint = AmberLight,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Daily Runtime vs Historical Benchmark",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    color = Color.White
                )

                Text(
                    "Track weekly tractor telemetry against operational quota.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                // Tractor filter chips (All Fleet, Machine 1, Machine 2...)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = weeklyTrend.selectedMachineId == null,
                        onClick = { onSelectMachine(null) },
                        label = { 
                            Text(
                                "All Fleet (${machines.size})",
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default
                            ) 
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Amber,
                            selectedLabelColor = Color.Black,
                            containerColor = FrostedGlassElevated,
                            labelColor = TextMuted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = weeklyTrend.selectedMachineId == null,
                            borderColor = if (weeklyTrend.selectedMachineId == null) Amber else Color.White.copy(alpha = 0.12f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.applePressEffect(pressedScale = 0.94f)
                    )

                    machines.forEach { machine ->
                        val isSelected = weeklyTrend.selectedMachineId == machine.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectMachine(machine.id) },
                            label = { 
                                Text(
                                    machine.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Default
                                ) 
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Amber,
                                selectedLabelColor = Color.Black,
                                containerColor = FrostedGlassElevated,
                                labelColor = TextMuted
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) Amber else Color.White.copy(alpha = 0.12f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.applePressEffect(pressedScale = 0.94f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Frosted Metrics summary banner (Apple Health Style)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .frostedGlass(
                            shape = RoundedCornerShape(18.dp),
                            backgroundColor = FrostedGlassElevated,
                            borderBrush = GlassBorderGradient,
                            borderWidth = 1.dp,
                            hasSpecularHighlight = true,
                            specularAlpha = 0.10f
                        )
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            if (selectedDay?.isToday == true) "TODAY RUNTIME" else "${selectedDay?.dayOfWeek ?: "DAY"} RUNTIME",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            color = TextSecondary,
                            letterSpacing = 0.6.sp
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                String.format(Locale.US, "%.1f", selectedDay?.runtimeHours ?: 0f),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                color = Color.White
                            )
                            Text(
                                " hrs",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.Default,
                                color = AmberLight,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "HISTORICAL AVG",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            color = TextSecondary,
                            letterSpacing = 0.6.sp
                        )
                        Text(
                            "${String.format(Locale.US, "%.1f", weeklyTrend.historicalAverageHours)} h/day",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            color = Amber,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // Delta badge
                    val deltaFormatted = String.format(Locale.US, "%+.1f", deltaVsBenchmark)
                    val badgeColor = if (isAboveAvg) OkGreen else Amber
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(badgeColor.copy(alpha = 0.15f))
                            .border(1.dp, badgeColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isAboveAvg) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = badgeColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                "$deltaFormatted hrs",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,
                                color = badgeColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // The Apple-Smooth Interactive Bar Chart
                AppleSmoothBarChart(
                    days = weeklyTrend.days,
                    historicalAverageHours = weeklyTrend.historicalAverageHours,
                    selectedIndex = selectedDayIndex,
                    onSelectIndex = { selectedDayIndex = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Inspection detail card for the selected day with spring physics transition
                AnimatedContent(
                    targetState = selectedDay,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220, easing = AppleFluidSpringCubic)) +
                                scaleIn(initialScale = 0.96f, animationSpec = AppleSpringSmooth))
                            .togetherWith(
                                fadeOut(animationSpec = tween(180, easing = AppleFluidSpringCubic)) +
                                        scaleOut(targetScale = 0.96f, animationSpec = tween(180, easing = AppleFluidSpringCubic))
                            )
                    },
                    label = "selectedDayDetail"
                ) { targetDay ->
                    if (targetDay != null) {
                        SelectedDayDetailCard(
                            day = targetDay,
                            benchmark = weeklyTrend.historicalAverageHours
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppleSmoothBarChart(
    days: List<DayRuntimeItem>,
    historicalAverageHours: Float,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (days.isEmpty()) return

    // Spring animation for smooth Apple-like entry and transitions
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(days) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = AppleSpringSmooth
        )
    }

    val maxHours = maxOf(
        days.maxOfOrNull { it.runtimeHours } ?: 0f,
        historicalAverageHours
    )
    val yAxisMax = (ceil((maxHours * 1.25f).coerceAtLeast(6f) / 2f) * 2f).coerceAtLeast(4f)

    Box(
        modifier = modifier
            .pointerInput(days.size) {
                detectTapGestures { offset ->
                    val padX = 24.dp.toPx()
                    val usableWidth = size.width - padX * 2
                    val step = usableWidth / days.size.toFloat()
                    val idx = ((offset.x - padX) / step).toInt().coerceIn(0, days.size - 1)
                    onSelectIndex(idx)
                }
            }
            .pointerInput(days.size) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val padX = 24.dp.toPx()
                        val usableWidth = size.width - padX * 2
                        val step = usableWidth / days.size.toFloat()
                        val idx = ((offset.x - padX) / step).toInt().coerceIn(0, days.size - 1)
                        onSelectIndex(idx)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val padX = 24.dp.toPx()
                        val usableWidth = size.width - padX * 2
                        val step = usableWidth / days.size.toFloat()
                        val idx = ((change.position.x - padX) / step).toInt().coerceIn(0, days.size - 1)
                        onSelectIndex(idx)
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val padX = 24.dp.toPx()
            val padTop = 18.dp.toPx()
            val padBottom = 28.dp.toPx()
            val usableWidth = size.width - padX * 2
            val usableHeight = size.height - padTop - padBottom
            val baseline = size.height - padBottom
            val step = usableWidth / days.size.toFloat()
            val barWidth = (step * 0.52f).coerceIn(18.dp.toPx(), 34.dp.toPx())

            // 1. Draw horizontal subtle grid lines at 25%, 50%, 75%, 100%
            val gridLevels = listOf(0.25f, 0.5f, 0.75f, 1f)
            gridLevels.forEach { lvl ->
                val gridY = baseline - lvl * usableHeight
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(padX, gridY),
                    end = Offset(size.width - padX, gridY),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // 2. Draw Historical Average Benchmark Dashed Line
            val benchmarkRatio = (historicalAverageHours / yAxisMax).coerceIn(0f, 1f)
            val benchmarkY = baseline - benchmarkRatio * usableHeight
            drawLine(
                color = AmberLight.copy(alpha = 0.85f),
                start = Offset(padX, benchmarkY),
                end = Offset(size.width - padX, benchmarkY),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
            )

            // Draw benchmark badge on right
            val badgeWidth = 46.dp.toPx()
            val badgeHeight = 16.dp.toPx()
            drawRoundRect(
                color = Amber.copy(alpha = 0.2f),
                topLeft = Offset(size.width - padX - badgeWidth, benchmarkY - badgeHeight / 2f),
                size = Size(badgeWidth, badgeHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // 3. Draw the 7 bars
            days.forEachIndexed { i, day ->
                val centerX = padX + (i + 0.5f) * step
                val targetHeight = (day.runtimeHours / yAxisMax).coerceIn(0f, 1f) * usableHeight
                val currentHeight = targetHeight * animatedProgress.value
                val barTop = baseline - currentHeight
                val isSelected = (i == selectedIndex)

                // Selected bar glow column background
                if (isSelected) {
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(Amber.copy(alpha = 0.16f), Color.Transparent),
                            startY = 0f,
                            endY = baseline
                        ),
                        topLeft = Offset(centerX - step * 0.46f, 0f),
                        size = Size(step * 0.92f, baseline),
                        cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                    )
                }

                if (currentHeight > 2.dp.toPx()) {
                    val barBrush = if (isSelected) {
                        Brush.verticalGradient(
                            listOf(GoldLight, GoldPrimary, GoldDark),
                            startY = barTop,
                            endY = baseline
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.75f), SilverPlatinum, SilverTitanium, SilverDark),
                            startY = barTop,
                            endY = baseline
                        )
                    }

                    // Bar body
                    drawRoundRect(
                        brush = barBrush,
                        topLeft = Offset(centerX - barWidth / 2f, barTop),
                        size = Size(barWidth, currentHeight),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    // Specular highlight at top of bar
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.4f), Color.Transparent),
                            startY = barTop,
                            endY = barTop + 10.dp.toPx()
                        ),
                        topLeft = Offset(centerX - barWidth / 2f, barTop),
                        size = Size(barWidth, minOf(currentHeight, 10.dp.toPx())),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                } else {
                    // Small pill dot for empty or 0h days
                    drawCircle(
                        color = Color.White.copy(alpha = 0.2f),
                        radius = 3.dp.toPx(),
                        center = Offset(centerX, baseline - 4.dp.toPx())
                    )
                }

                // If today, draw indicator dot below baseline
                if (day.isToday) {
                    drawCircle(
                        color = Amber,
                        radius = 3.5.dp.toPx(),
                        center = Offset(centerX, baseline + 18.dp.toPx())
                    )
                }
            }
        }

        // X-Axis Day Labels rendered as Compose text overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEachIndexed { i, day ->
                val isSelected = (i == selectedIndex)
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.dayOfWeek,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontFamily = FontFamily.Default,
                        color = if (isSelected) Amber else TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Frosted Glass Selected Day Detail Card
 */
@Composable
fun SelectedDayDetailCard(day: DayRuntimeItem, benchmark: Float) {
    val isAbove = day.runtimeHours >= benchmark
    val delta = day.runtimeHours - benchmark

    FrostedGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = FrostedGlassElevated,
        borderBrush = Brush.linearGradient(listOf(Amber.copy(alpha = 0.45f), Color.White.copy(alpha = 0.08f))),
        borderWidth = 1.dp,
        elevation = 10.dp,
        hasSpecularHighlight = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${day.dayOfWeek} • ${day.dateLabel}",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    if (day.isToday) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Amber)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "TODAY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                color = Color.Black
                            )
                        }
                    }
                }

                Text(
                    formatTime(day.runtimeSeconds),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = AmberLight
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Benchmark comparison info
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = if (isAbove) Icons.Default.CheckCircle else Icons.Default.Info
                val color = if (isAbove) OkGreen else Amber
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val desc = if (isAbove) {
                    "${String.format(Locale.US, "%.1f", delta)} hrs above historical benchmark (${String.format(Locale.US, "%.1f", benchmark)}h target)"
                } else {
                    "${String.format(Locale.US, "%.1f", -delta)} hrs below historical benchmark (${String.format(Locale.US, "%.1f", benchmark)}h target)"
                }
                Text(
                    desc,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Default,
                    color = color
                )
            }

            // Tractor session breakdown if any
            if (day.machineBreakdown.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "FLEET LOGS (${day.sessionCount} sessions)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default,
                    color = TextSecondary,
                    letterSpacing = 0.6.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                day.machineBreakdown.forEach { breakdown ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "🚜 ${breakdown.machineName} (${breakdown.brand})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Default,
                            color = Color.White
                        )
                        Text(
                            "${String.format(Locale.US, "%.1f", breakdown.hours)}h",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            color = Amber
                        )
                    }
                }
            }
        }
    }
}

/**
 * Full Trends Screen with Apple Frosted Glass Panels
 */
@Composable
fun TrendsScreen(
    weeklyTrend: WeeklyTrendData,
    machines: List<MachineEntity>,
    onSelectMachine: (String?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        "Fleet Analytics\n& Trends.",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Default,
                        color = Color.White,
                        lineHeight = 36.sp,
                        letterSpacing = (-0.4).sp
                    )
                    Text(
                        "Operational trends and efficiency telemetry",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Default,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                FrostedGlassPill(
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = Amber.copy(alpha = 0.16f),
                    borderColor = Amber.copy(alpha = 0.35f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(OkGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "TELEMETRY SYNCED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            color = Amber,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // The Primary Bar Chart Component
        item {
            WeeklyTrendsCard(
                weeklyTrend = weeklyTrend,
                machines = machines,
                onSelectMachine = onSelectMachine,
                onExpandAnalytics = null
            )
        }

        // Apple-style Performance Highlights Grid
        item {
            Text(
                "Performance Benchmarks",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                color = Color.White,
                letterSpacing = (-0.2).sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnalyticsHighlightCard(
                    title = "PEAK RUNTIME DAY",
                    value = weeklyTrend.peakDay?.dayOfWeek ?: "TUE",
                    sub = "${String.format(Locale.US, "%.1f", weeklyTrend.peakDay?.runtimeHours ?: 0f)} hrs logged",
                    icon = Icons.Default.Star,
                    modifier = Modifier.weight(1f)
                )

                AnalyticsHighlightCard(
                    title = "WEEKLY TOTAL",
                    value = "${String.format(Locale.US, "%.1f", weeklyTrend.totalWeekHours)}h",
                    sub = "Quota: ${String.format(Locale.US, "%.1f", weeklyTrend.historicalAverageHours * 7f)}h",
                    icon = Icons.Default.Speed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnalyticsHighlightCard(
                    title = "FLEET EFFICIENCY",
                    value = "${(100f + weeklyTrend.deltaPercentVsAverage).coerceIn(60f, 150f).roundToInt()}%",
                    sub = if (weeklyTrend.deltaPercentVsAverage >= 0) "Optimal operating window" else "Below historical baseline",
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )

                AnalyticsHighlightCard(
                    title = "ACTIVE ENGINES",
                    value = "${machines.count { it.isRunning }} / ${machines.size}",
                    sub = "Real-time live status",
                    icon = Icons.Default.ElectricBolt,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 7-Day Performance Log Table
        item {
            Text(
                "Weekly Operations Log",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default,
                color = Color.White,
                letterSpacing = (-0.2).sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(weeklyTrend.days.reversed()) { day ->
            DayPerformanceRow(day = day, benchmark = weeklyTrend.historicalAverageHours)
        }
    }
}

/**
 * Frosted Glass Highlight Card
 */
@Composable
fun AnalyticsHighlightCard(
    title: String,
    value: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    FrostedGlassCard(
        modifier = modifier.applePressEffect(pressedScale = 0.96f),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = FrostedGlassSurface,
        borderBrush = GlassBorderGradient,
        elevation = 8.dp,
        hasSpecularHighlight = true
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default,
                        color = TextSecondary,
                        letterSpacing = 0.6.sp
                    )
                    Icon(icon, contentDescription = null, tint = Amber, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    sub,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = TextMuted
                )
            }
        }
    }
}

/**
 * Frosted Glass Day Performance Row
 */
@Composable
fun DayPerformanceRow(day: DayRuntimeItem, benchmark: Float) {
    val progress = if (benchmark > 0f) (day.runtimeHours / benchmark).coerceIn(0f, 1.5f) else 0f
    val isAbove = day.runtimeHours >= benchmark

    FrostedGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .applePressEffect(pressedScale = 0.98f),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = FrostedGlassSurface,
        borderBrush = if (day.isToday) ActiveMachineBorderGradient else GlassBorderGradient,
        borderWidth = if (day.isToday) 1.5.dp else 1.dp,
        elevation = 6.dp,
        hasSpecularHighlight = true
    ) {
        Box(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            day.dayOfWeek,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        if (day.isToday) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Amber)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "TODAY",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Default,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                    Text(
                        day.dateLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Default,
                        color = TextMuted
                    )
                }

                // Progress bar vs benchmark
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${(progress * 100).roundToInt()}% benchmark",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Default,
                            color = TextSecondary
                        )
                        Text(
                            if (isAbove) "▲ Above" else "▼ Below",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            color = if (isAbove) OkGreen else Amber
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val animatedProgress by animateFloatAsState(
                        targetValue = (progress / 1.5f).coerceIn(0f, 1f),
                        animationSpec = tween(durationMillis = 650, easing = AppleFluidDecel),
                        label = "benchmarkProgress"
                    )
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isAbove) OkGreen else Amber,
                        trackColor = Color.White.copy(alpha = 0.08f)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "${String.format(Locale.US, "%.1f", day.runtimeHours)}h",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Default,
                        color = Color.White
                    )
                    Text(
                        formatTime(day.runtimeSeconds),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Default,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
