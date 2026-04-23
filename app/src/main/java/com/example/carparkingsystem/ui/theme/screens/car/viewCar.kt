package com.example.carparkingsystem.ui.theme.screens.car

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.carparkingsystem.data.CarViewModel
import com.example.carparkingsystem.models.CarModel
import com.example.carparkingsystem.navigation.buildUpdateCarRoute
import com.example.carparkingsystem.ui.theme.CarParkingSystemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(navController: NavController) {
    val isPreview = LocalInspectionMode.current
    val carViewModel: CarViewModel = viewModel()
    val cars = carViewModel.cars // Observes the mutableStateListOf in ViewModel
    val context = LocalContext.current

    // Refresh data whenever this screen is opened
    LaunchedEffect(Unit) {
        if (!isPreview) {
            carViewModel.fetchCar(context)
        }
    }

    val redDark  = Color(0xFFB71C1C)
    val bgDark   = Color(0xFF1A0000)
    val bgMid    = Color(0xFF2C0000)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registered Cars", fontWeight = FontWeight.ExtraBold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redDark),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        },
        containerColor = bgDark
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(bgDark, bgMid, bgDark)))
        ) {
            if (cars.isEmpty()) {
                EmptyStateView()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 12.dp,
                        bottom = 80.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cars) { car ->
                        CarCard(
                            car = car,
                            onDelete = {
                                carViewModel.deleteCar(car.id ?: "", context, navController)
                            },
                            onUpdate = {
                                navController.navigate(buildUpdateCarRoute(car.id ?: ""))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🚗", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "No cars registered yet.",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 15.sp
        )
    }
}

// ── CarCard ───────────────────────────────────────────────────────────────────
@Composable
fun CarCard(
    car: CarModel,
    onDelete: () -> Unit,
    onUpdate: () -> Unit
) {
    // Per-card delete confirmation dialog state
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ── Delete confirmation dialog ─────────────────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor   = Color(0xFF3A0000),
            titleContentColor = Color.White,
            textContentColor  = Color(0xFFEF9A9A),
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color(0xFFE53935)
                )
            },
            title = { Text("Delete Car", fontWeight = FontWeight.Bold) },
            text  = {
                Text("Remove ${car.plateNumber ?: "this car"} from the system? This cannot be undone.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) {
                    Text("Delete", color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color(0xFFEF9A9A))
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A0000)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Car image ──────────────────────────────────────────────────
            AsyncImage(
                model = car.imageUrl?.takeIf { it.isNotEmpty() }
                    ?: "https://via.placeholder.com/80?text=Car",
                contentDescription = "Car image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            // ── Car details ────────────────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = car.plateNumber ?: "No Plate",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = car.driverName ?: "Unknown Driver",
                    fontSize = 13.sp,
                    color = Color(0xFFEF9A9A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${car.vehicleType ?: "N/A"}  •  ${car.vehicleColor}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.45f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Entry time badge
                Text(
                    text = "⏱ ${car.entryTime}",
                    fontSize = 11.sp,
                    color = Color(0xFFEF9A9A).copy(alpha = 0.7f),
                    modifier = Modifier
                        .background(
                            Color(0xFF4A0000),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            // ── Action buttons ─────────────────────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Update button
                IconButton(
                    onClick = onUpdate,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFFF8F00), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Update Car",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Delete button
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFE53935), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Car",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ── Previews ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarListScreenPreview() {
    CarParkingSystemTheme {
        CarListScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun CarCardPreview() {
    CarParkingSystemTheme {
        CarCard(
            car = CarModel(
                id           = "1",
                plateNumber  = "KCB 123X",
                vehicleType  = "Sedan",
                driverName   = "John Doe",
                phoneNumber  = "0712345678",
                imageUrl     = "",
                vehicleColor = "Silver",
                entryTime    = "10:30 AM"
            ),
            onDelete = {},
            onUpdate = {}
        )
    }
}
