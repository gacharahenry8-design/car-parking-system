package com.example.carparkingsystem.ui.theme.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.navigation.ROUTE_DASHBOARD
import com.example.carparkingsystem.navigation.ROUTE_LOGIN
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController? = null) {
    val auth = FirebaseAuth.getInstance()

    // --- Animation States ---
    val iconScale     = remember { Animatable(0f) }
    val iconAlpha     = remember { Animatable(0f) }
    val titleAlpha    = remember { Animatable(0f) }
    val dividerWidth  = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val taglineAlpha  = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Icon bounces in
        iconScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        iconAlpha.animateTo(1f, animationSpec = tween(300))

        // Title fades in
        titleAlpha.animateTo(1f, animationSpec = tween(500))
        delay(80)

        // Accent divider expands
        dividerWidth.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))

        // Subtitle & tagline stagger
        subtitleAlpha.animateTo(1f, animationSpec = tween(450))
        delay(100)
        taglineAlpha.animateTo(1f, animationSpec = tween(450))

        // Hold then navigate
        delay(1800)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController?.navigate(ROUTE_DASHBOARD) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController?.navigate(ROUTE_LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // --- Root Background ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0000),
                        Color(0xFF2C0000),
                        Color(0xFF1A0000)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Decorative glowing blobs
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = 100.dp, y = (-200).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFD32F2F).copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-90).dp, y = 220.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF5252).copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        // --- Centered Content ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {

            // P icon badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(iconScale.value)
                    .alpha(iconAlpha.value)
                    .size(120.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFB71C1C),
                                Color(0xFFE53935)
                            )
                        )
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Text(
                        text = "P",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // App name
            Text(
                text = "SmartPark",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 0.5.sp,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Animated expanding accent line
            Box(
                modifier = Modifier
                    .fillMaxWidth(dividerWidth.value * 0.35f)
                    .height(3.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFE53935), Color(0xFFFF8A80))
                        ),
                        RoundedCornerShape(50)
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "CAR PARKING SYSTEM",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF9A9A),
                letterSpacing = 3.sp,
                modifier = Modifier.alpha(subtitleAlpha.value)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Tagline
            Text(
                text = "Smart slots. Real-time tracking.\nSeamless parking experience.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.45f),
                textAlign = TextAlign.Center,
                lineHeight = 21.sp,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }

        // Bottom brand footer
        Text(
            text = "SmartPark © 2025  •  All rights reserved",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.22f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .alpha(taglineAlpha.value)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}