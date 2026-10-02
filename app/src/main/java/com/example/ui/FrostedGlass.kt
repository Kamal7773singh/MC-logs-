package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

/**
 * Modern Apple Frosted Glass (backdrop-filter equivalent) Utility for Jetpack Compose.
 *
 * Implements the core physics of Apple VisionOS & iOS Materials:
 * 1. Layered Translucent Substrate: Filters background content and cosmological ambient gradients.
 * 2. Specular Top-Light Highlight: Directional refraction gradient mimicking polished physical glass.
 * 3. Precision Rim Border: Dual-toned specular border catching ambient room and interface light.
 * 4. Ambient Diffusion Glow: Soft elevation shadow for realistic spatial float.
 */

/**
 * Modifier extension to apply a high-fidelity Apple frosted glass effect to any composable.
 */
fun Modifier.frostedGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = FrostedGlassSurface,
    borderBrush: Brush = GlassBorderGradient,
    borderWidth: Dp = 1.dp,
    hasSpecularHighlight: Boolean = true,
    specularAlpha: Float = 0.12f,
    shadowElevation: Dp = 0.dp
): Modifier = this
    .then(
        if (shadowElevation > 0.dp) {
            Modifier.shadow(
                elevation = shadowElevation,
                shape = shape,
                ambientColor = Color(0x33000000),
                spotColor = Color(0x2A00E5FF)
            )
        } else Modifier
    )
    .clip(shape)
    .background(backgroundColor)
    .then(
        if (hasSpecularHighlight) {
            Modifier.drawBehind {
                // Subtle directional specular sheen along the top-left edge
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = specularAlpha),
                            Color.White.copy(alpha = specularAlpha * 0.3f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = size.height * 0.45f
                    ),
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height * 0.45f)
                )
            }
        } else Modifier
    )
    .border(borderWidth, borderBrush, shape)

/**
 * Standard Apple Frosted Glass Card Container
 */
@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = FrostedGlassSurface,
    borderBrush: Brush = GlassBorderGradient,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    hasSpecularHighlight: Boolean = true,
    specularAlpha: Float = 0.12f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .frostedGlass(
                shape = shape,
                backgroundColor = backgroundColor,
                borderBrush = borderBrush,
                borderWidth = borderWidth,
                hasSpecularHighlight = hasSpecularHighlight,
                specularAlpha = specularAlpha,
                shadowElevation = elevation
            ),
        content = content
    )
}

/**
 * Frosted Glass Pill Capsule for Eyebrow Tags, Status HUDs, and Category Badges
 */
@Composable
fun FrostedGlassPill(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = FrostedGlassPill,
    borderColor: Color = VisionCyan.copy(alpha = 0.35f),
    borderBrush: Brush? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = size.height
                    )
                )
            }
            .then(
                if (borderBrush != null) {
                    Modifier.border(1.dp, borderBrush, shape)
                } else {
                    Modifier.border(1.dp, borderColor, shape)
                }
            ),
        content = content
    )
}
