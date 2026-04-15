package com.example.carparkingsystem.ui.theme.screens.car

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.carparkingsystem.data.CarViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCarScreen(navController: NavHostController) {
    // 1. State for Image and Text Fields
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var plateNumber by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("") }
    var driverName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // NEW STATES
    var vehicleColor by remember { mutableStateOf("") }
    var entryTime by remember {
        mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
    }

    val carViewModel: CarViewModel = viewModel()
    val context = LocalContext.current

    val colorOptions = listOf("Black", "White", "Silver", "Red", "Blue", "Green")
    var expanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Car Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Image Picker Section
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar, // Changed to car for relevance
                        contentDescription = "Default Icon",
                        modifier = Modifier.size(120.dp),
                        tint = Color.Gray
                    )
                }
            }

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Plate Number
            OutlinedTextField(
                value = plateNumber,
                onValueChange = { plateNumber = it },
                label = { Text("Plate Number") },
                leadingIcon = { Icon(imageVector = Icons.Default.Numbers, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Vehicle Type
            OutlinedTextField(
                value = vehicleType,
                onValueChange = { vehicleType = it },
                label = { Text("Vehicle Type") },
                leadingIcon = { Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // DROPDOWN: Vehicle Color
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = vehicleColor,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Vehicle Color") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Palette, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    colorOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                vehicleColor = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Driver Name
            OutlinedTextField(
                value = driverName,
                onValueChange = { driverName = it },
                label = { Text("Driver Name") },
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone Number
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                leadingIcon = { Icon(imageVector = Icons.Default.Call, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ENTRY TIME
            OutlinedTextField(
                value = entryTime,
                onValueChange = { entryTime = it },
                label = { Text("Entry Time") },
                leadingIcon = { Icon(imageVector = Icons.Default.Schedule, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("HH:mm") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    carViewModel.uploadCar(
                        imageUri = imageUri,
                        driverName = driverName,
                        plateNumber = plateNumber,
                        vehicleType = vehicleType,
                        vehicleColor = vehicleColor, // Added
                        entryTime = entryTime,       // Added
                        phoneNumber = phoneNumber,
                        context = context,
                        navController = navController
                    )
                }
            ) {
                Text("Save Car")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppCarScreenPreview() {
    AppCarScreen(rememberNavController())
}