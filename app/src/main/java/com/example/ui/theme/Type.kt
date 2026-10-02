package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Modern Minimal Apple Design Language Typography.
 * Strictly uses Android system font (FontFamily.Default - Roboto / System Sans-serif)
 * with authentic Apple SF Pro weighting, tight tracking on headings, relaxed tracking
 * on small uppercase captions, and calibrated visual hierarchy.
 */

val AppleLargeTitle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 34.sp,
    lineHeight = 41.sp,
    letterSpacing = (-0.5).sp,
    color = TextPrimary
)

val AppleTitle1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 34.sp,
    letterSpacing = (-0.4).sp,
    color = TextPrimary
)

val AppleTitle2 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = (-0.3).sp,
    color = TextPrimary
)

val AppleTitle3 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    lineHeight = 25.sp,
    letterSpacing = (-0.2).sp,
    color = TextPrimary
)

val AppleHeadline = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.1).sp,
    color = TextPrimary
)

val AppleBody = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 23.sp,
    letterSpacing = 0.sp,
    color = TextSecondary
)

val AppleCallout = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 21.sp,
    letterSpacing = 0.sp,
    color = TextSecondary
)

val AppleSubheadline = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 19.sp,
    letterSpacing = 0.sp,
    color = TextSecondary
)

val AppleFootnote = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp,
    color = TextMuted
)

val AppleCaption1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp,
    color = TextSecondary
)

val AppleCaption2 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.5.sp,
    color = TextMuted
)

val AppleEyebrowPill = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 10.sp,
    lineHeight = 13.sp,
    letterSpacing = 0.8.sp,
    color = VisionCyan
)

val AppleTelemetryMetric = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 36.sp,
    letterSpacing = (-0.5).sp,
    color = TextPrimary
)

val AppleTelemetrySub = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.2.sp,
    color = TextMuted
)

// Complete Material3 Typography mapped to Apple System Font Hierarchy
val Typography = Typography(
    displayLarge = AppleLargeTitle,
    displayMedium = AppleTitle1,
    displaySmall = AppleTitle2,
    headlineLarge = AppleTitle3,
    headlineMedium = AppleHeadline,
    headlineSmall = AppleSubheadline,
    titleLarge = AppleTitle2,
    titleMedium = AppleHeadline,
    titleSmall = AppleSubheadline,
    bodyLarge = AppleBody,
    bodyMedium = AppleCallout,
    bodySmall = AppleFootnote,
    labelLarge = AppleCallout.copy(fontWeight = FontWeight.SemiBold),
    labelMedium = AppleCaption1,
    labelSmall = AppleCaption2
)
