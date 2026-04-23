package com.example.carparkingsystem.ui.theme.screens.car

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.carparkingsystem.data.CarViewModel
import com.example.carparkingsystem.models.CarModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarScreen(
    navController: NavController,
    carId: String
) {
    val carViewModel: CarViewModel = viewModel()
    val context = LocalContext.current

    // ── Form State ────────────────────────────────────────────────────────────
    var driverName       by remember { mutableStateOf("") }
    var plateNumber      by remember { mutableStateOf("") }
    var vehicleType      by remember { mutableStateOf("") }
    var phoneNumber      by remember { mutableStateOf("") }
    var vehicleColor     by remember { mutableStateOf("") }
    var entryTime        by remember { mutableStateOf("") }
    var existingImageUrl by remember { mutableStateOf("") }
    var imageUri         by remember { mutableStateOf<Uri?>(null) }
    var isFetchingData   by remember { mutableStateOf(true) }
    var isUpdating       by remember { mutableStateOf(false) }

    val isPreview = LocalInspectionMode.current

    // ── Fetch Existing Car Data from Firebase ─────────────────────────────────
    LaunchedEffect(carId) {
        if (isPreview) return@LaunchedEffect
        try {
            val ref = FirebaseDatabase.getInstance().getReference("Cars").child(carId)
            val snapshot = ref.get().await()
            val car = snapshot.getValue(CarModel::class.java)
            car?.let {
                driverName       = it.driverName    ?: ""
                plateNumber      = it.plateNumber   ?: ""
                vehicleType      = it.vehicleType   ?: ""
                phoneNumber      = it.phoneNumber   ?: ""
                vehicleColor     = it.vehicleColor  ?: ""
                entryTime        = it.entryTime     ?: ""
                existingImageUrl = it.imageUrl      ?: ""
            }
        } finally {
            isFetchingData = false
        }
    }

    // ── Image Picker ──────────────────────────────────────────────────────────
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    // ── Theme Colours ─────────────────────────────────────────────────────────
    val redDark  = Color(0xFFB71C1C)
    val redVivid = Color(0xFFE53935)
    val redLight = Color(0xFFEF9A9A)
    val bgDark   = Color(0xFF1A0000)
    val bgMid    = Color(0xFF2C0000)
    val cardBg   = Color(0xFF3A0000)

    // ── Loading State ─────────────────────────────────────────────────────────
    if (isFetchingData) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgDark),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = redVivid)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Loading car details...", color = redLight.copy(alpha = 0.6f), fontSize = 13.sp)
            }
        }
        return
    }

    // ── Main Screen ───────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Update Car Details",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark)
            )
        },
        containerColor = bgDark
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(bgDark, bgMid, bgDark)))
        ) {
            // Decorative glow blobs
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .offset(x = 130.dp, y = 40.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(redVivid.copy(alpha = 0.12f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (-50).dp, y = 480.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(redDark.copy(alpha = 0.10f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Image Picker ──────────────────────────────────────────────
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBg)
                        .border(2.dp, redVivid.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable { imagePickerLauncher.launch("image/*") }
                ) {
                    val displayImage: Any? = imageUri
                        ?: if (existingImageUrl.isNotEmpty()) existingImageUrl else null

                    if (displayImage != null) {
                        AsyncImage(
                            model = displayImage,
                            contentDescription = "Car Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp))
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = redLight.copy(alpha = 0.5f),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tap to change", fontSize = 11.sp, color = redLight.copy(alpha = 0.4f))
                        }
                    }

                    // Edit badge
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(28.dp)
                            .background(redVivid, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Tap image to update photo", fontSize = 12.sp, color = redLight.copy(alpha = 0.35f))
                Spacer(modifier = Modifier.height(24.dp))

                // ── Form Card ─────────────────────────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        UpdateTextField(
                            value = driverName,
                            onValueChange = { driverName = it },
                            label = "Driver Name",
                            icon = Icons.Default.Person,
                            redVivid = redVivid,
                            redLight = redLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        UpdateTextField(
                            value = plateNumber,
                            onValueChange = { plateNumber = it },
                            label = "Plate Number",
                            icon = Icons.Default.DirectionsCar,
                            redVivid = redVivid,
                            redLight = redLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        UpdateTextField(
                            value = vehicleType,
                            onValueChange = { vehicleType = it },
                            label = "Vehicle Type",
                            icon = Icons.Default.CarRental,
                            redVivid = redVivid,
                            redLight = redLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        UpdateTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            icon = Icons.Default.Phone,
                            keyboardType = KeyboardType.Phone,
                            redVivid = redVivid,
                            redLight = redLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        UpdateTextField(
                            value = vehicleColor,
                            onValueChange = { vehicleColor = it },
                            label = "Vehicle Color",
                            icon = Icons.Default.ColorLens,
                            redVivid = redVivid,
                            redLight = redLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        UpdateTextField(
                            value = entryTime,
                            onValueChange = { entryTime = it },
                            label = "Entry Time",
                            icon = Icons.Default.AccessTime,
                            redVivid = redVivid,
                            redLight = redLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // ── Update Button ─────────────────────────────────────────────
                Button(
                    onClick = {
                        isUpdating = true
                        carViewModel.updateCar(
                            carId         = carId,
                            imageUri      = imageUri,
                            driverName    = driverName,
                            plateNumber   = plateNumber,
                            vehicleType   = vehicleType,
                            phoneNumber   = phoneNumber,
                            vehicleColor  = vehicleColor,
                            entryTime     = entryTime,
                            context       = context,
                            navController = navController,
                            currentImageUrl = existingImageUrl
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    enabled = !isUpdating
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(listOf(redDark, redVivid)),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUpdating) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Save,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "UPDATE DETAILS",
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cancel button
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, redVivid.copy(alpha = 0.5f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = redLight)
                ) {
                    Text(
                        "CANCEL",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ── Reusable themed text field ────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    redVivid: Color,
    redLight: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = redLight.copy(alpha = 0.7f), fontSize = 13.sp) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = redVivid,
                modifier = Modifier.size(20.dp)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = redVivid,
            unfocusedBorderColor    = redLight.copy(alpha = 0.3f),
            focusedTextColor        = Color.White,
            unfocusedTextColor      = Color.White,
            cursorColor             = redVivid,
            focusedContainerColor   = Color(0xFF4A0000),
            unfocusedContainerColor = Color(0xFF3A0000)
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UpdateCarScreenPreview() {
    UpdateCarScreen(
        navController = rememberNavController(),
        carId = "preview_id"
    )
}