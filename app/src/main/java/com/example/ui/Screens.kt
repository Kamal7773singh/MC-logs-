package com.example.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.data.MachineEntity
import com.example.data.RecordEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel,
    state: UiState,
    navController: NavHostController
) {
    var showAddSheet by remember { mutableStateOf(false) }
    var editMachine by remember { mutableStateOf<MachineEntity?>(null) }
    var showPinDialog by remember { mutableStateOf<(() -> Unit)?>(null) }
    
    val context = LocalContext.current
    
    fun performHaptic(pattern: LongArray) {
        if (!state.hapticEnabled) return
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }
    
    fun performSound() {
        if (!state.soundEnabled) return
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        audioManager.playSoundEffect(android.media.AudioManager.FX_KEY_CLICK)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCosmos)
            .drawBehind {
                // Soft brushed platinum silver ambient orb at top-left
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(SilverPlatinum.copy(alpha = 0.07f), Color.Transparent),
                        center = Offset(0f, 0f),
                        radius = size.width * 0.95f
                    ),
                    center = Offset(0f, 0f),
                    radius = size.width * 0.95f
                )
                // Radiant fine champagne gold ambient orb at mid-right
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GoldPrimary.copy(alpha = 0.09f), Color.Transparent),
                        center = Offset(size.width, size.height * 0.38f),
                        radius = size.width * 0.85f
                    ),
                    center = Offset(size.width, size.height * 0.38f),
                    radius = size.width * 0.85f
                )
            }
    ) {
        Scaffold(
            bottomBar = {
                BottomNavBar(navController)
            },
            floatingActionButton = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                androidx.compose.animation.AnimatedVisibility(
                    visible = currentRoute == "home",
                    enter = androidx.compose.animation.scaleIn(animationSpec = AppleSpringBouncy) + androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(220, easing = AppleFluidSpringCubic)),
                    exit = androidx.compose.animation.scaleOut(animationSpec = androidx.compose.animation.core.tween(180, easing = AppleFluidSpringCubic)) + androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(150, easing = AppleFluidSpringCubic))
                ) {
                    FloatingActionButton(
                        onClick = { 
                            performSound()
                            showAddSheet = true 
                        },
                        containerColor = GoldPrimary,
                        contentColor = Color.Black,
                        shape = CircleShape,
                        modifier = Modifier
                            .appleSpringButton(pressedScale = 0.90f, hapticType = AppleHapticType.IMPACT_LIGHT)
                            .border(1.5.dp, Brush.linearGradient(listOf(Color.White, GoldLight, GoldPrimary, SilverPlatinum)), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Machine", modifier = Modifier.size(26.dp))
                    }
                }
            },
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
        val isCardSheetOrModalOpen = showAddSheet || editMachine != null || showPinDialog != null
        val backgroundScale by animateFloatAsState(
            targetValue = if (isCardSheetOrModalOpen) 0.93f else 1.0f,
            animationSpec = AppleSpringSmooth,
            label = "bgScale"
        )
        val backgroundCornerRadius by animateDpAsState(
            targetValue = if (isCardSheetOrModalOpen) 24.dp else 0.dp,
            animationSpec = androidx.compose.animation.core.tween(durationMillis = 280, easing = AppleFluidSpringCubic),
            label = "bgCorners"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .graphicsLayer {
                    scaleX = backgroundScale
                    scaleY = backgroundScale
                    clip = backgroundCornerRadius > 0.dp
                    shape = RoundedCornerShape(backgroundCornerRadius)
                }
        ) {
            val weeklyTrend by viewModel.weeklyTrend.collectAsState()

            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize(),
                enterTransition = { appleEnterTransition() },
                exitTransition = { appleExitTransition() },
                popEnterTransition = { applePopEnterTransition() },
                popExitTransition = { applePopExitTransition() }
            ) {
                composable("home") {
                    HomeScreen(
                        state = state,
                        weeklyTrend = weeklyTrend,
                        onToggle = { 
                            viewModel.toggleMachine(it)
                            if (!it.isRunning) performHaptic(longArrayOf(100, 50, 100, 50, 80))
                            else performHaptic(longArrayOf(150, 100, 200))
                        },
                        onEdit = { editMachine = it },
                        onSelectTrendMachine = { viewModel.selectTrendMachine(it) },
                        onNavigateToTrends = {
                            navController.navigate("trends") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable("trends") {
                    TrendsScreen(
                        weeklyTrend = weeklyTrend,
                        machines = state.machines,
                        onSelectMachine = { viewModel.selectTrendMachine(it) }
                    )
                }
                composable("logs") {
                    LogsScreen(state = state)
                }
                composable("settings") {
                    SettingsScreen(
                        state = state,
                        onToggleSound = { viewModel.toggleSound(it) },
                        onToggleHaptic = { viewModel.toggleHaptic(it) },
                        onReset = { showPinDialog = { viewModel.factoryReset() } }
                    )
                }
            }
        }
        
        IosCardSheet(
            visible = showAddSheet || editMachine != null,
            machine = editMachine,
            onDismiss = {
                showAddSheet = false
                editMachine = null
            },
            onSave = { name, brand ->
                if (editMachine != null) {
                    viewModel.updateMachine(editMachine!!.id, name, brand)
                    editMachine = null
                } else {
                    viewModel.addMachine(name, brand)
                    showAddSheet = false
                }
            },
            onDelete = if (editMachine != null) {
                {
                    val toDeleteId = editMachine!!.id
                    editMachine = null
                    showPinDialog = {
                        viewModel.deleteMachine(toDeleteId)
                    }
                }
            } else null
        )

        IosAuthModal(
            visible = showPinDialog != null,
            onDismiss = { showPinDialog = null },
            onSuccess = {
                showPinDialog?.invoke()
                showPinDialog = null
                Toast.makeText(context, "Action Authorized", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Triple("home", Icons.Filled.Home, Icons.Outlined.Home),
        Triple("trends", Icons.Filled.BarChart, Icons.Outlined.BarChart),
        Triple("logs", Icons.Filled.List, Icons.Outlined.List),
        Triple("settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Floating Apple VisionOS Frosted Glass Dock
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .frostedGlass(
                    shape = RoundedCornerShape(32.dp),
                    backgroundColor = FrostedGlassDock,
                    borderBrush = FrostedDockBorderGradient,
                    borderWidth = 1.dp,
                    hasSpecularHighlight = true,
                    specularAlpha = 0.16f,
                    shadowElevation = 24.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                items.forEach { (route, selectedIcon, unselectedIcon) ->
                    val selected = currentRoute == route
                    val iconScale by animateFloatAsState(
                        targetValue = if (selected) 1.14f else 1.0f,
                        animationSpec = AppleSpringBouncy,
                        label = "navIconScale"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(if (selected) GoldPrimary.copy(alpha = 0.18f) else Color.Transparent)
                            .border(
                                1.dp,
                                if (selected) {
                                    Brush.linearGradient(listOf(GoldLight, GoldPrimary, Color.White.copy(alpha = 0.35f)))
                                } else {
                                    Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                },
                                RoundedCornerShape(22.dp)
                            )
                            .applePressEffect(pressedScale = 0.90f) {
                                if (currentRoute != route) {
                                    performAppleHaptic(context, AppleHapticType.SELECTION)
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                if (selected) selectedIcon else unselectedIcon,
                                contentDescription = route,
                                tint = if (selected) GoldPrimary else SilverTitanium,
                                modifier = Modifier
                                    .size(20.dp)
                                    .graphicsLayer {
                                        scaleX = iconScale
                                        scaleY = iconScale
                                    }
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                route.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (selected) GoldPrimary else SilverTitanium,
                                fontFamily = FontFamily.Default,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
