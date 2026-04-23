package com.example.carparkingsystem.ui.theme.screens.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.navigation.ROUTE_ADDCAR
import com.example.carparkingsystem.navigation.ROUTE_LOGIN
import com.example.carparkingsystem.navigation.ROUTE_VIEWCARS
import com.example.carparkingsystem.ui.theme.CarParkingSystemTheme
import com.google.firebase.auth.FirebaseAuth

// ── Palette ───────────────────────────────────────────────────────────────────
private val BgDeep    = Color(0xFF0A0A0A)
private val BgPanel   = Color(0xFF111111)
private val BgCard    = Color(0xFF181818)
private val RedCore   = Color(0xFFE53935)
private val RedGlow   = Color(0xFFEF5350)
private val RedDim    = Color(0xFF7B1010)
private val RedFaint  = Color(0xFF1A0808)
private val Amber     = Color(0xFFFF8F00)
private val Green     = Color(0xFF00C853)
private val Steel     = Color(0xFF9E9E9E)
private val WhitePri  = Color(0xFFF5F5F5)
private val WhiteSec  = Color(0xFF9E9E9E)
private val Divider   = Color(0xFF2A2A2A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController) {
    val selectedItem      = remember { mutableIntStateOf(0) }
    var showLogoutDialog  by remember { mutableStateOf(false) }

    // Pulsing animation for live indicator
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by pulseAnim.animateFloat(
        initialValue = 0.3f, targetValue = 1f, label = "pulseAlpha",
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse)
    )

    // ── Logout dialog ─────────────────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor    = Color(0xFF1A0808),
            titleContentColor = WhitePri,
            textContentColor  = Steel,
            shape = RoundedCornerShape(20.dp),
            icon = {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = RedCore, modifier = Modifier.size(28.dp))
            },
            title = { Text("Sign Out", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text  = { Text("End your current session?", fontSize = 14.sp) },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    try { FirebaseAuth.getInstance().signOut() } catch (_: Exception) {}
                    navController.navigate(ROUTE_LOGIN) { popUpTo(0) { inclusive = true } }
                }) { Text("Sign Out", color = RedCore, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Steel)
                }
            }
        )
    }

    Scaffold(
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Live dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(RedCore.copy(alpha = pulseAlpha), androidx.compose.foundation.shape.CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                "SMARTPARK",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = WhitePri,
                                letterSpacing = 3.sp
                            )
                            Text(
                                "OPERATIONS CENTER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                color = RedCore,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgPanel,
                    actionIconContentColor = Steel
                ),
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Logout", tint = Steel)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = BgPanel,
                tonalElevation = 0.dp,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Divider,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
            ) {
                listOf(
                    Triple(Icons.Default.Home,     "Home",     0),
                    Triple(Icons.Default.Settings, "Settings", 1),
                    Triple(Icons.Default.Person,   "Profile",  2)
                ).forEach { (icon, label, idx) ->
                    NavigationBarItem(
                        selected = selectedItem.intValue == idx,
                        onClick  = { selectedItem.intValue = idx },
                        icon     = { Icon(icon, label, modifier = Modifier.size(20.dp)) },
                        label    = { Text(label, fontSize = 10.sp, letterSpacing = 0.5.sp) },
                        colors   = NavigationBarItemDefaults.colors(
                            selectedIconColor   = RedCore,
                            selectedTextColor   = RedCore,
                            indicatorColor      = RedFaint,
                            unselectedIconColor = Steel,
                            unselectedTextColor = Steel
                        )
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Section label ─────────────────────────────────────────────────
            SectionLabel("SLOT OVERVIEW")

            // ── Slot Overview Card ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF1A0808), Color(0xFF0F0F0F)),
                            start = Offset(0f, 0f),
                            end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .border(1.dp, RedDim.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SlotStatBlock(
                        value = "18",
                        label = "AVAILABLE",
                        color = Green
                    )
                    // Vertical divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(64.dp)
                            .background(Divider)
                    )
                    SlotStatBlock(
                        value = "32",
                        label = "OCCUPIED",
                        color = RedGlow
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(64.dp)
                            .background(Divider)
                    )
                    SlotStatBlock(
                        value = "50",
                        label = "TOTAL",
                        color = Steel
                    )
                }
            }

            // ── Weekly Traffic ────────────────────────────────────────────────
            SectionLabel("WEEKLY TRAFFIC")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BgCard)
                    .border(1.dp, Divider, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // Day labels
                    val days    = listOf("M", "T", "W", "T", "F", "S", "S")
                    val heights = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f, 0.3f, 0.8f)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        heights.forEachIndexed { i, h ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.55f)
                                        .fillMaxHeight(h)
                                        .background(
                                            if (i == heights.lastIndex)
                                                Brush.verticalGradient(listOf(RedGlow, RedDim))
                                            else
                                                Brush.verticalGradient(listOf(RedDim, Color(0xFF300000))),
                                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                        )
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        days.forEach { d ->
                            Text(d, fontSize = 10.sp, color = Steel, textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // ── Quick Actions ─────────────────────────────────────────────────
            SectionLabel("QUICK ACTIONS")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionCard(
                    title = "Add Car",
                    icon  = Icons.Default.Add,
                    color = RedCore,
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(ROUTE_ADDCAR) }
                )
                ActionCard(
                    title = "View Cars",
                    icon  = Icons.Default.DirectionsCar,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(ROUTE_VIEWCARS) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionCard(
                    title = "Payment",
                    icon  = Icons.Default.Payments,
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.weight(1f)
                )
                ActionCard(
                    title = "History",
                    icon  = Icons.Default.History,
                    color = Color(0xFF37474F),
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Recent Activity ───────────────────────────────────────────────
            SectionLabel("RECENT ACTIVITY")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BgCard)
                    .border(1.dp, Divider, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val activities = listOf(
                        Triple("KCB 123X", "Entered", Green),
                        Triple("KAA 456Y", "Exited",  RedGlow),
                        Triple("KCD 789Z", "Entered", Green)
                    )
                    activities.forEachIndexed { index, (plate, status, statusColor) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (status == "Entered") Color(0xFF001A00)
                                    else Color(0xFF1A0000)
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            statusColor.copy(alpha = 0.15f),
                                            RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (status == "Entered") Icons.Default.Login else Icons.Default.Logout,
                                        contentDescription = null,
                                        tint = statusColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(plate, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = WhitePri)
                                    Text("Just now", fontSize = 11.sp, color = Steel)
                                }
                            }
                            Text(
                                text = status.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor,
                                letterSpacing = 1.sp,
                                modifier = Modifier
                                    .background(statusColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        if (index < activities.lastIndex) {
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ── Reusable components ───────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(14.dp)
                .background(RedCore, RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Steel,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun SlotStatBlock(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            color = color,
            letterSpacing = (-1).sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Steel,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(color.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = title,
                color = WhitePri,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    CarParkingSystemTheme {
        Dashboard(navController = rememberNavController())
    }
}