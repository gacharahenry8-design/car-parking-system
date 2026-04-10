package com.example.carparkingsystem.ui.theme.screens.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.R
import com.example.carparkingsystem.data.AuthViewModel
import com.example.carparkingsystem.navigation.ROUTE_REGISTER

// ── Simple design tokens (identical to RegisterScreen) ─────────────────────
private val Primary     = Color(0xFF1A73E8)
private val BgWhite     = Color(0xFFFFFFFF)
private val BgLight     = Color(0xFFF5F6FA)
private val TextDark    = Color(0xFF1A1A2E)
private val TextGrey    = Color(0xFF6B7280)
private val BorderGrey  = Color(0xFFD1D5DB)
private val ErrorRed    = Color(0xFFDC2626)

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    val isFormValid = Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.length >= 6

    // ── Field colors (identical to RegisterScreen) ─────────────────────────
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor          = TextDark,
        unfocusedTextColor        = TextDark,
        focusedContainerColor     = BgWhite,
        unfocusedContainerColor   = BgLight,
        cursorColor               = Primary,
        focusedBorderColor        = Primary,
        unfocusedBorderColor      = BorderGrey,
        focusedLabelColor         = Primary,
        unfocusedLabelColor       = TextGrey,
        focusedLeadingIconColor   = Primary,
        unfocusedLeadingIconColor = TextGrey,
        errorBorderColor          = ErrorRed,
        errorLabelColor           = ErrorRed,
        errorLeadingIconColor     = ErrorRed
    )

    // ── Root ───────────────────────────────────────────────────────────────
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgWhite)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // ── Logo ───────────────────────────────────────────────────────────
        Image(
            painter            = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = BorderGrey, shape = CircleShape)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Title ──────────────────────────────────────────────────────────
        Text(
            text       = "Welcome Back",
            fontSize   = 24.sp,
            fontWeight = FontWeight.Bold,
            color      = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text     = "Sign in to your account",
            fontSize = 13.sp,
            color    = TextGrey
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ── Email ──────────────────────────────────────────────────────────
        OutlinedTextField(
            value         = email,
            onValueChange = { email = it },
            label         = { Text("Email Address") },
            leadingIcon   = {
                Icon(
                    imageVector        = Icons.Default.Email,
                    contentDescription = null,
                    modifier           = Modifier.size(20.dp)
                )
            },
            colors   = fieldColors,
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Password ───────────────────────────────────────────────────────
        OutlinedTextField(
            value                = password,
            onValueChange        = { password = it },
            label                = { Text("Password") },
            leadingIcon          = {
                Icon(
                    imageVector        = Icons.Default.Lock,
                    contentDescription = null,
                    modifier           = Modifier.size(20.dp)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            colors               = fieldColors,
            modifier             = Modifier.fillMaxWidth(),
            shape                = RoundedCornerShape(10.dp)
        )

        // ── Forgot password link ───────────────────────────────────────────
        Box(
            modifier         = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = {
                navController.navigate("forgot_password")
            }) {
                Text(
                    text       = "Forgot Password?",
                    color      = Primary,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Login button ───────────────────────────────────────────────────
        Button(
            onClick  = {
                authViewModel.login(email = email,password = password,navController = navController,context = context)
            },
            enabled  = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor         = Primary,
                disabledContainerColor = BorderGrey
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text          = "LOGIN",
                fontSize      = 15.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color         = if (isFormValid) BgWhite else TextGrey
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Register link ──────────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text     = "Don't have an account?",
                color    = TextGrey,
                fontSize = 14.sp
            )
            TextButton(onClick = {
                navController.navigate(ROUTE_REGISTER)
            }) {
                Text(
                    text           = "Register",
                    color          = Primary,
                    fontWeight     = FontWeight.SemiBold,
                    fontSize       = 14.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}
