package com.example.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset

/**
 * Custom Spring-Physics and Apple Cubic-Bezier Animation Library.
 * Replaces standard linear and generic transitions with fluid physics curves:
 * - Apple Fluid Spring Cubic: cubic-bezier(0.2, 0.8, 0.2, 1)
 * - Calibrated Damped Spring Physics for sheets, modals, buttons, and navigation.
 */

// User-specified Apple fluid spring cubic curve: cubic-bezier(0.2, 0.8, 0.2, 1)
val AppleFluidSpringCubic = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1.0f)
val AppleStandardCubic = AppleFluidSpringCubic

// Apple standard interactive ease-in-out: cubic-bezier(0.25, 0.1, 0.25, 1.0)
val AppleEaseInOutCubic = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)
val AppleEaseInOut = AppleFluidSpringCubic

// Apple deceleration curve: cubic-bezier(0.215, 0.61, 0.355, 1.0)
val AppleEaseOut = CubicBezierEasing(0.215f, 0.61f, 0.355f, 1.0f)

// Apple acceleration curve: cubic-bezier(0.55, 0.055, 0.675, 0.19)
val AppleEaseIn = CubicBezierEasing(0.55f, 0.055f, 0.675f, 0.19f)

// Apple fluid momentum curve: cubic-bezier(0.16, 1.0, 0.3, 1.0)
val AppleFluidDecel = CubicBezierEasing(0.16f, 1.0f, 0.3f, 1.0f)

/**
 * Apple iOS Tuned Spring-Physics Specs
 */

// General UI interactive spring: balanced responsiveness and gentle settle
val AppleSpringSpec = spring<Float>(
    dampingRatio = 0.72f,
    stiffness = 380f
)

// Snappy micro-interaction spring (buttons, toggles, icon bouncing)
val AppleSpringSnappy = spring<Float>(
    dampingRatio = 0.62f,
    stiffness = 480f
)

// Ultra-tactile button press spring with noticeable elastic bounce
val AppleSpringButton = spring<Float>(
    dampingRatio = 0.54f,
    stiffness = 520f
)

// Fluid smooth spring for medium components (cards, chart bars, scale transitions)
val AppleSpringSmooth = spring<Float>(
    dampingRatio = 0.76f,
    stiffness = 320f
)

// High bounce spring (FAB appearance, badges, celebratory states)
val AppleSpringBouncy = spring<Float>(
    dampingRatio = 0.52f,
    stiffness = 360f
)

// Gentle slow settle spring (screen entry, background scale down)
val AppleSpringGentle = spring<Float>(
    dampingRatio = 0.84f,
    stiffness = 260f
)

// Modal sheet & card presentation spring physics
val AppleSpringModal = spring<Float>(
    dampingRatio = 0.76f,
    stiffness = 340f
)

// Spring specification for IntOffset (slide-in / slide-out)
val AppleSpringOffsetSpec = spring<IntOffset>(
    dampingRatio = 0.76f,
    stiffness = 340f
)

/**
 * Helper to produce a tween spec with cubic-bezier(0.2, 0.8, 0.2, 1)
 */
fun <T> appleCubicTween(
    durationMillis: Int = 300,
    easing: CubicBezierEasing = AppleFluidSpringCubic
): TweenSpec<T> {
    return tween(durationMillis = durationMillis, easing = easing)
}

/**
 * Apple Haptic Feedback System
 */
enum class AppleHapticType {
    SELECTION,
    IMPACT_LIGHT,
    IMPACT_MEDIUM,
    SUCCESS,
    ERROR
}

fun performAppleHaptic(context: Context, type: AppleHapticType = AppleHapticType.SELECTION) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vm.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val effect = when (type) {
            AppleHapticType.SELECTION -> VibrationEffect.createOneShot(14, 90)
            AppleHapticType.IMPACT_LIGHT -> VibrationEffect.createOneShot(22, 140)
            AppleHapticType.IMPACT_MEDIUM -> VibrationEffect.createOneShot(35, 200)
            AppleHapticType.SUCCESS -> VibrationEffect.createWaveform(longArrayOf(0, 15, 60, 25), intArrayOf(0, 100, 0, 200), -1)
            AppleHapticType.ERROR -> VibrationEffect.createWaveform(longArrayOf(0, 30, 40, 30), intArrayOf(0, 250, 0, 250), -1)
        }
        vibrator.vibrate(effect)
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(20)
    }
}

/**
 * Custom Apple-style touch press modifier.
 * Replaces standard instant/linear press states with spring-physics release
 * and cubic-bezier(0.2, 0.8, 0.2, 1) touch-down curve.
 */
fun Modifier.applePressEffect(
    pressedScale: Float = 0.95f,
    haptic: Boolean = true,
    hapticType: AppleHapticType = AppleHapticType.SELECTION,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = if (isPressed) {
            tween(durationMillis = 130, easing = AppleFluidSpringCubic)
        } else {
            AppleSpringSnappy
        },
        label = "applePressScale"
    )
    val context = LocalContext.current

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(onClick) {
            awaitEachGesture {
                awaitFirstDown()
                isPressed = true
                if (haptic) {
                    performAppleHaptic(context, hapticType)
                }
                val upOrCancel = waitForUpOrCancellation()
                isPressed = false
                if (upOrCancel != null && onClick != null) {
                    onClick()
                }
            }
        }
}

/**
 * Custom Apple Spring Button Modifier.
 * Higher bounce spring physics specifically tuned for primary and secondary action buttons.
 */
fun Modifier.appleSpringButton(
    pressedScale: Float = 0.93f,
    hapticType: AppleHapticType = AppleHapticType.IMPACT_MEDIUM,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = if (isPressed) {
            tween(durationMillis = 110, easing = AppleFluidSpringCubic)
        } else {
            AppleSpringButton
        },
        label = "appleSpringButtonScale"
    )
    val context = LocalContext.current

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(onClick) {
            awaitEachGesture {
                awaitFirstDown()
                isPressed = true
                performAppleHaptic(context, hapticType)
                val upOrCancel = waitForUpOrCancellation()
                isPressed = false
                if (upOrCancel != null && onClick != null) {
                    onClick()
                }
            }
        }
}

/**
 * Apple iOS Screen Navigation Transitions with cubic-bezier(0.2, 0.8, 0.2, 1) and spring scaling
 */
fun appleEnterTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(300, easing = AppleFluidSpringCubic)) +
            scaleIn(initialScale = 0.95f, animationSpec = AppleSpringGentle)
}

fun appleExitTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(220, easing = AppleFluidSpringCubic)) +
            scaleOut(targetScale = 1.03f, animationSpec = tween(220, easing = AppleFluidSpringCubic))
}

fun applePopEnterTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(300, easing = AppleFluidSpringCubic)) +
            scaleIn(initialScale = 1.03f, animationSpec = AppleSpringGentle)
}

fun applePopExitTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(220, easing = AppleFluidSpringCubic)) +
            scaleOut(targetScale = 0.95f, animationSpec = tween(220, easing = AppleFluidSpringCubic))
}
