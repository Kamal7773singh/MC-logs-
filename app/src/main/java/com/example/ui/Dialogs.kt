package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MachineEntity
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Modern Apple VisionOS Frosted Glass Card Presentation for Add/Edit Machine Sheet.
 * Features fluid slide-in from bottom and smooth backdrop fade with frosted glass backdrop materials.
 */
@Composable
fun IosCardSheet(
    visible: Boolean,
    machine: MachineEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, brand: String) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var name by remember(machine, visible) { mutableStateOf(machine?.name ?: "") }
    var brand by remember(machine, visible) { mutableStateOf(machine?.brand ?: "") }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(280, easing = AppleFluidSpringCubic)),
        exit = fadeOut(animationSpec = tween(220, easing = AppleFluidSpringCubic))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() }
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = AppleSpringOffsetSpec
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(240, easing = AppleFluidSpringCubic)
                ),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                // Frosted Glass Card Sheet Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* prevent backdrop click */ }
                        .frostedGlass(
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                            backgroundColor = FrostedGlassSheet,
                            borderBrush = GlassBorderGradient,
                            borderWidth = 1.dp,
                            hasSpecularHighlight = true,
                            specularAlpha = 0.16f,
                            shadowElevation = 24.dp
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                            .padding(bottom = 36.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // iOS-Style Top Grabber Pill
                            Box(
                                modifier = Modifier
                                    .size(width = 38.dp, height = 5.dp)
                                    .clip(RoundedCornerShape(2.5.dp))
                                    .background(Color.White.copy(alpha = 0.25f))
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Header with Title and iOS Close Circle Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        if (machine == null) "Nayi Machine Jodein" else "Machine Telemetry Update",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FontFamily.Default,
                                        color = Color.White,
                                        letterSpacing = (-0.2).sp
                                    )
                                    Text(
                                        "Field Command fleet unit configuration",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = FontFamily.Default,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .frostedGlass(
                                            shape = CircleShape,
                                            backgroundColor = Color(0x33FFFFFF),
                                            borderBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                                                listOf(Color.White.copy(alpha = 0.35f), Color.White.copy(alpha = 0.1f))
                                            ),
                                            borderWidth = 1.dp,
                                            hasSpecularHighlight = false
                                        )
                                        .applePressEffect(pressedScale = 0.88f) { onDismiss() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = TextLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(22.dp))

                            // Machine Name Input
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { 
                                    Text(
                                        "MACHINE / DRIVER KA NAAM",
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.4.sp
                                    ) 
                                },
                                placeholder = { 
                                    Text(
                                        "Udhaaran: Dunder Singh",
                                        fontFamily = FontFamily.Default,
                                        color = TextMuted
                                    ) 
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Amber,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                    focusedLabelColor = Amber,
                                    unfocusedLabelColor = TextMuted,
                                    focusedContainerColor = FrostedGlassElevated,
                                    unfocusedContainerColor = FrostedGlassSurface
                                ),
                                shape = RoundedCornerShape(16.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Brand Input
                            OutlinedTextField(
                                value = brand,
                                onValueChange = { brand = it },
                                label = { 
                                    Text(
                                        "BRAND / MODEL",
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.4.sp
                                    ) 
                                },
                                placeholder = { 
                                    Text(
                                        "Udhaaran: John Deere 5310",
                                        fontFamily = FontFamily.Default,
                                        color = TextMuted
                                    ) 
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Amber,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                    focusedLabelColor = Amber,
                                    unfocusedLabelColor = TextMuted,
                                    focusedContainerColor = FrostedGlassElevated,
                                    unfocusedContainerColor = FrostedGlassSurface
                                ),
                                shape = RoundedCornerShape(16.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(28.dp))

                            // Action Buttons Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                if (machine != null && onDelete != null) {
                                    OutlinedButton(
                                        onClick = onDelete,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(54.dp)
                                            .applePressEffect(pressedScale = 0.94f, hapticType = AppleHapticType.ERROR),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = DangerRed
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(1.5.dp, DangerRed.copy(alpha = 0.7f))
                                    ) {
                                        Text(
                                            "DELETE",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Default,
                                            letterSpacing = 0.6.sp
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (name.isNotBlank()) {
                                            onSave(name.trim(), brand.trim())
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(if (machine != null && onDelete != null) 1.5f else 1f)
                                        .height(54.dp)
                                        .appleSpringButton(
                                            pressedScale = 0.93f,
                                            hapticType = AppleHapticType.SUCCESS
                                        ),
                                    enabled = name.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Amber,
                                        contentColor = Color.Black,
                                        disabledContainerColor = SurfaceLight,
                                        disabledContentColor = TextMuted
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        if (machine == null) "MACHINE JODEIN" else "UPDATE KAREIN",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily.Default,
                                        letterSpacing = 0.6.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modern Apple VisionOS Frosted Glass Auth / PIN Modal with Tactile Circular Keypad.
 */
@Composable
fun IosAuthModal(
    visible: Boolean,
    requiredPin: String = "1234",
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var enteredPin by remember(visible) { mutableStateOf("") }
    var isError by remember(visible) { mutableStateOf(false) }
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(250, easing = AppleFluidSpringCubic)),
        exit = fadeOut(animationSpec = tween(200, easing = AppleFluidSpringCubic))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = AppleSpringOffsetSpec
                ) + scaleIn(
                    initialScale = 0.90f,
                    animationSpec = AppleSpringBouncy
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it / 5 },
                    animationSpec = tween(220, easing = AppleFluidSpringCubic)
                ) + scaleOut(
                    targetScale = 0.90f,
                    animationSpec = tween(220, easing = AppleFluidSpringCubic)
                )
            ) {
                // Auth Modal Frosted Glass Card Container
                FrostedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* prevent backdrop click */ },
                    shape = RoundedCornerShape(30.dp),
                    backgroundColor = FrostedGlassSheet,
                    borderBrush = GlassBorderGradient,
                    elevation = 24.dp,
                    hasSpecularHighlight = true
                ) {
                    Box(modifier = Modifier.padding(24.dp)) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Ambient Security Icon
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(if (isError) DangerRed.copy(alpha = 0.15f) else Amber.copy(alpha = 0.15f))
                                    .border(1.dp, if (isError) DangerRed.copy(alpha = 0.4f) else Amber.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isError) Icons.Default.Lock else Icons.Default.Security,
                                    contentDescription = "Security",
                                    tint = if (isError) DangerRed else Amber,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                "Security Verification",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default,
                                color = Color.White,
                                letterSpacing = (-0.2).sp
                            )

                            Text(
                                if (isError) "Galat PIN! Dubara koshish karein." else "Action authorize karne ke liye PIN darj karein",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.Default,
                                color = if (isError) DangerRed else TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                            )

                            // 4 Passcode Dots with horizontal iOS shake animation
                            Row(
                                modifier = Modifier
                                    .offset(x = shakeOffset.value.dp)
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (i in 0 until 4) {
                                    val isFilled = enteredPin.length > i
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isError -> DangerRed
                                                    isFilled -> Amber
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .border(
                                                2.dp,
                                                when {
                                                    isError -> DangerRed
                                                    isFilled -> Amber
                                                    else -> Color.White.copy(alpha = 0.3f)
                                                },
                                                CircleShape
                                            )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Native iOS-style Circular Keypad with Frosted Glass Buttons
                            val keys = listOf(
                                listOf("1", "2", "3"),
                                listOf("4", "5", "6"),
                                listOf("7", "8", "9"),
                                listOf("CANCEL", "0", "DEL")
                            )

                            keys.forEach { row ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    row.forEach { key ->
                                        when (key) {
                                            "CANCEL" -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .applePressEffect(pressedScale = 0.88f) { onDismiss() },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        "Cancel",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontFamily = FontFamily.Default,
                                                        color = TextSecondary
                                                    )
                                                }
                                            }
                                            "DEL" -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .frostedGlass(
                                                            shape = CircleShape,
                                                            backgroundColor = FrostedGlassKeypad,
                                                            borderBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                                                                listOf(Color.White.copy(alpha = 0.35f), SilverTitanium.copy(alpha = 0.25f), Color.White.copy(alpha = 0.08f))
                                                            ),
                                                            borderWidth = 1.dp,
                                                            hasSpecularHighlight = true,
                                                            specularAlpha = 0.12f
                                                        )
                                                        .applePressEffect(pressedScale = 0.88f) {
                                                            if (enteredPin.isNotEmpty()) {
                                                                enteredPin = enteredPin.dropLast(1)
                                                                isError = false
                                                            }
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Backspace,
                                                        contentDescription = "Delete",
                                                        tint = SilverPlatinum,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                            else -> {
                                                // Number button with Apple Frosted Glass
                                                Box(
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .frostedGlass(
                                                            shape = CircleShape,
                                                            backgroundColor = FrostedGlassKeypad,
                                                            borderBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                                                                listOf(
                                                                    Color.White.copy(alpha = 0.40f),
                                                                    GoldLight.copy(alpha = 0.35f),
                                                                    SilverTitanium.copy(alpha = 0.20f),
                                                                    Color.White.copy(alpha = 0.08f)
                                                                )
                                                            ),
                                                            borderWidth = 1.dp,
                                                            hasSpecularHighlight = true,
                                                            specularAlpha = 0.14f
                                                        )
                                                        .applePressEffect(pressedScale = 0.88f) {
                                                            if (enteredPin.length < 4) {
                                                                val newPin = enteredPin + key
                                                                enteredPin = newPin
                                                                isError = false

                                                                if (newPin.length == 4) {
                                                                    if (newPin == requiredPin) {
                                                                        performAppleHaptic(context, AppleHapticType.SUCCESS)
                                                                        coroutineScope.launch {
                                                                            delay(150)
                                                                            onSuccess()
                                                                        }
                                                                    } else {
                                                                        isError = true
                                                                        performAppleHaptic(context, AppleHapticType.ERROR)
                                                                        coroutineScope.launch {
                                                                            for (repeat in 0 until 3) {
                                                                                shakeOffset.animateTo(-22f, tween(45, easing = AppleFluidSpringCubic))
                                                                                shakeOffset.animateTo(22f, tween(45, easing = AppleFluidSpringCubic))
                                                                            }
                                                                            shakeOffset.animateTo(0f, AppleSpringSnappy)
                                                                            delay(200)
                                                                            enteredPin = ""
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        key,
                                                        fontSize = 24.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontFamily = FontFamily.Default,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Backwards-compatible aliases
@Composable
fun AddEditMachineSheet(
    machine: MachineEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, brand: String) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    IosCardSheet(
        visible = true,
        machine = machine,
        onDismiss = onDismiss,
        onSave = onSave,
        onDelete = onDelete
    )
}

@Composable
fun PinDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    IosAuthModal(
        visible = true,
        onDismiss = onDismiss,
        onSuccess = onSuccess
    )
}
