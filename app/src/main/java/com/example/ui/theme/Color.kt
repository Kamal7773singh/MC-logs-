package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Deep Obsidian & Velvet Luxury Backgrounds
val BgBlack = Color(0xFF030406)
val BgCosmos = Color(0xFF07090E)
val BgObsidian = Color(0xFF0A0C12)

// Fine Gold Metallic Palette
val GoldPrimary = Color(0xFFD4AF37) // Royal Fine Gold
val GoldChampagne = Color(0xFFF5E6BE) // Delicate Champagne Silk Gold
val GoldLight = Color(0xFFF9E8B2) // Bright Lustrous Gold
val GoldDark = Color(0xFFA67C1E) // Burnished Deep Gold
val GoldBronze = Color(0xFF8C6418) // Rich Antique Bronze Gold
val GoldGlow = Color(0x38D4AF37) // Ambient Gold Glow
val GoldSubtle = Color(0x1AD4AF37)

// Fine Platinum & Brushed Silver Palette
val SilverPlatinum = Color(0xFFF1F5F9) // Polished Platinum Silver
val SilverChrome = Color(0xFFE2E8F0) // Bright Chrome
val SilverTitanium = Color(0xFF94A3B8) // Brushed Titanium Silver
val SilverDark = Color(0xFF475569) // Deep Gunmetal Silver
val SilverMuted = Color(0xFF64748B) // Subtle Silver Gray
val SilverSpecular = Color(0x66FFFFFF) // Specular Light Sheen

// Backward-compatible bindings
val Amber = GoldPrimary
val AmberLight = GoldLight
val AmberDark = GoldDark
val VisionCyan = SilverPlatinum
val VisionCyanGlow = Color(0x28CBD5E1)
val VisionAmberGlow = GoldGlow
val VisionEmerald = Color(0xFF10B981)
val VisionEmeraldGlow = Color(0x2810B981)
val VisionPurple = Color(0xFF9D4EDD)

// Frosted Luxury Glass Substrates
val SurfaceDark = Color(0xFF0C0E14)
val SurfaceLight = Color(0xFF141824)
val GlassSurface = Color(0x2E1B2234)
val GlassSurfaceElevated = Color(0x40252F47)
val GlassSurfaceSubtle = Color(0x1A141A28)

val FrostedGlassBackground = Color(0x3B0F131D)
val FrostedGlassSurface = Color(0x33141926)
val FrostedGlassElevated = Color(0x4D1B2234)
val FrostedGlassSubtle = Color(0x1F0D111A)
val FrostedGlassDock = Color(0xDD090B10) // Luxury Obsidian Titanium Floating Dock
val FrostedGlassSheet = Color(0xE80A0D14) // Luxury Presentation Sheet
val FrostedGlassPill = Color(0x33161C2A)
val FrostedGlassKeypad = Color(0x38192030)

// Apple Minimal Visual Hierarchy & Luxury Text Colors
val TextLight = Color(0xFFF8FAFC)
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8) // Platinum Silver Secondary
val TextTertiary = Color(0xFF64748B) // Gunmetal Silver Tertiary
val TextQuaternary = Color(0xFF475569)
val TextGold = Color(0xFFF5E6BE) // Champagne Gold Text Accent
val TextSilver = Color(0xFFE2E8F0) // Polished Silver Text Accent
val TextMuted = Color(0xFF94A3B8)
val TextCyan = SilverPlatinum

// Status & Alerts
val BorderAmber = Color(0x40D4AF37)
val BorderGlass = Color(0x26FFFFFF)
val BorderSpecular = Color(0x4DFFFFFF)
val DangerRed = Color(0xFFE11D48)
val OkGreen = Color(0xFF10B981)

// Luxury Metallic Gradients & Shaders
val GoldMetallicBrush = Brush.linearGradient(
    colors = listOf(
        GoldLight,
        GoldPrimary,
        GoldDark,
        GoldChampagne
    )
)

val SilverPlatinumBrush = Brush.linearGradient(
    colors = listOf(
        Color.White,
        SilverPlatinum,
        SilverTitanium,
        Color.White
    )
)

// Dual Bimetallic Border Gradient (Gold & Silver Luxury Rim)
val GlassBorderGradient = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.50f),
        GoldLight.copy(alpha = 0.40f),
        GoldPrimary.copy(alpha = 0.30f),
        SilverTitanium.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.08f)
    )
)

val ActiveMachineBorderGradient = Brush.linearGradient(
    colors = listOf(
        GoldLight,
        GoldPrimary,
        Color.White.copy(alpha = 0.9f),
        GoldDark,
        GoldPrimary
    )
)

val FrostedDockBorderGradient = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.55f),
        GoldChampagne.copy(alpha = 0.45f),
        SilverTitanium.copy(alpha = 0.30f),
        GoldPrimary.copy(alpha = 0.25f),
        Color.White.copy(alpha = 0.12f)
    )
)

val GlassCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0x3B181E2C),
        Color(0x1F0F131C)
    )
)

val HeroCardBrush = Brush.linearGradient(
    colors = listOf(
        Color(0x45222B3D),
        Color(0x281B202D),
        Color(0x1C131822)
    )
)

val FrostedSpecularSheen = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.15f),
        GoldLight.copy(alpha = 0.05f),
        Color.Transparent
    )
)
