package com.example.carparkingsystem.ui.theme.screens.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.data.AuthEvent
import com.example.carparkingsystem.data.AuthViewModel
import com.example.carparkingsystem.navigation.ROUTE_DASHBOARD
import com.example.carparkingsystem.navigation.ROUTE_LOGIN
import com.example.carparkingsystem.navigation.ROUTE_REGISTER

// ── Palette (matches Dashboard & Register) ────────────────────────────────────
private val BgDeep   = Color(0xFF0A0A0A)
private val BgPanel  = Color(0xFF111111)
private val BgCard   = Color(0xFF181818)
private val BgField  = Color(0xFF1E1E1E)
private val RedCore  = Color(0xFFE53935)
private val RedDim   = Color(0xFF7B1010)
private val RedFaint = Color(0xFF1A0808)
private val Steel    = Color(0xFF9E9E9E)
private val Divider  = Color(0xFF2A2A2A)
private val WhitePri = Color(0xFFF5F5F5)
private val ErrorRed = Color(0xFFEF5350)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    // Pulsing live dot — same as Dashboard & Register
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by pulseAnim.animateFloat(
        initialValue = 0.3f, targetValue = 1f, label = "pulseAlpha",
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse)
    )

    LaunchedEffect(Unit) {
        authViewModel.authEvent.collect { event ->
            when (event) {
                is AuthEvent.NavigateToDashboard -> {
                    navController.navigate(ROUTE_DASHBOARD) { popUpTo(0) { inclusive = true } }
                }
                is AuthEvent.NavigateToLogin -> {
                    navController.navigate(ROUTE_LOGIN) { popUpTo(0) { inclusive = true } }
                }
                is AuthEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val isFormValid = Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor          = WhitePri,
        unfocusedTextColor        = WhitePri,
        focusedContainerColor     = BgField,
        unfocusedContainerColor   = BgField,
        cursorColor               = RedCore,
        focusedBorderColor        = RedCore,
        unfocusedBorderColor      = Divider,
        focusedLabelColor         = RedCore,
        unfocusedLabelColor       = Steel,
        focusedLeadingIconColor   = RedCore,
        unfocusedLeadingIconColor = Steel,
        errorBorderColor          = ErrorRed,
        errorLabelColor           = ErrorRed,
        errorLeadingIconColor     = ErrorRed
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // Background glow blobs
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = (-80).dp, y = (-100).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(RedCore.copy(alpha = 0.07f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = 140.dp, y = 480.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(RedDim.copy(alpha = 0.09f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo badge ────────────────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF1A0808), RedDim))
                    )
                    .border(1.dp, RedCore.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(8.dp)
                        .background(RedCore.copy(alpha = pulseAlpha), CircleShape)
                )
                Text(
                    text = "SP",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = WhitePri,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Header ────────────────────────────────────────────────────────
            Text(
                text = "WELCOME BACK",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = WhitePri,
                letterSpacing = 3.sp
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(2.dp)
                        .background(RedCore, RoundedCornerShape(1.dp))
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "SMARTPARK OPERATIONS",
                    fontSize = 9.sp,
                    color = RedCore,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(2.dp)
                        .background(RedCore, RoundedCornerShape(1.dp))
                )
            }

            Spacer(Modifier.height(36.dp))

            // ── Form card ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(BgCard)
                    .border(1.dp, Divider, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, null, modifier = Modifier.size(18.dp))
                        },
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, null, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    null,
                                    tint = Steel,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Forgot password
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(
                            onClick = { navController.navigate("forgot_password") },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Forgot Password?",
                                color = RedCore.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Login button ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isFormValid)
                            Brush.horizontalGradient(listOf(Color(0xFF7B1010), RedCore))
                        else
                            Brush.horizontalGradient(listOf(BgCard, BgPanel))
                    )
                    .border(
                        1.dp,
                        if (isFormValid) RedCore.copy(alpha = 0.5f) else Divider,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick  = { authViewModel.login(email = email, password = password) },
                    enabled  = isFormValid,
                    modifier = Modifier.fillMaxSize(),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    shape     = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
                ) {
                    Text(
                        text          = "SIGN IN",
                        fontSize      = 13.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color         = if (isFormValid) WhitePri else Steel
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── OR divider ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(1.dp).background(Divider))
                Text("  OR  ", fontSize = 11.sp, color = Steel, letterSpacing = 1.sp)
                Box(modifier = Modifier.weight(1f).height(1.dp).background(Divider))
            }

            Spacer(Modifier.height(20.dp))

            // ── Register link ─────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?", color = Steel, fontSize = 13.sp)
                TextButton(onClick = { navController.navigate(ROUTE_REGISTER) }) {
                    Text(
                        text           = "Register",
                        color          = RedCore,
                        fontWeight     = FontWeight.Bold,
                        fontSize       = 13.sp,
                        textDecoration = TextDecoration.Underline,
                        letterSpacing  = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Footer ────────────────────────────────────────────────────────
            Text(
                "SmartPark © 2025",
                fontSize = 10.sp,
                color = Steel.copy(alpha = 0.35f),
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}