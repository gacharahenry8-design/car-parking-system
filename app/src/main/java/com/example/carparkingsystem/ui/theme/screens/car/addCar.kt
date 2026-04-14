package com.example.carparkingsystem.ui.theme.screens.car

        import android.net.Uri
        import androidx.activity.compose.rememberLauncherForActivityResult
        import androidx.activity.result.contract.ActivityResultContracts
        import androidx.compose.foundation.Image
        import androidx.compose.foundation.layout.Box
        import androidx.compose.foundation.layout.Column
        import androidx.compose.foundation.layout.Spacer
        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.foundation.layout.fillMaxWidth
        import androidx.compose.foundation.layout.height
        import androidx.compose.foundation.layout.padding
        import androidx.compose.foundation.layout.size
        import androidx.compose.foundation.rememberScrollState
        import androidx.compose.foundation.verticalScroll
        import androidx.compose.material.icons.Icons
        import androidx.compose.material.icons.filled.Person
        import androidx.compose.material3.Button
        import androidx.compose.material3.ExperimentalMaterial3Api
        import androidx.compose.material3.Icon
        import androidx.compose.material3.OutlinedTextField
        import androidx.compose.material3.Scaffold
        import androidx.compose.material3.Text
        import androidx.compose.material3.TopAppBar
        import androidx.compose.material3.TopAppBarDefaults
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.getValue
        import androidx.compose.runtime.mutableStateOf
        import androidx.compose.runtime.remember
        import androidx.compose.runtime.setValue
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.layout.ContentScale
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.unit.dp
        import androidx.navigation.NavHostController
        import androidx.navigation.compose.rememberNavController
        import com.example.carparkingsystem.data.CarViewModel
        import androidx.compose.material.icons.filled.Call
        import androidx.compose.material.icons.filled.DirectionsCar
        import androidx.compose.material.icons.filled.Numbers
        import androidx.compose.material.icons.filled.PersonOutline
        import androidx.compose.ui.platform.LocalContext
        import androidx.lifecycle.viewmodel.compose.viewModel
        import coil.compose.rememberAsyncImagePainter
        import com.example.carparkingsystem.data.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun AppCarScreen(navController: NavHostController) {
            // 1. State for Image and Text Fields
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            var plateNumber by remember { mutableStateOf("") }
            var vehicleType by remember { mutableStateOf("") }
            var driverName by remember { mutableStateOf("") }
            var phoneNumber by remember { mutableStateOf("") }

            val carViewModel: CarViewModel = viewModel()
            val context = LocalContext.current

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
                        // 2. Added scroll so fields don't get cut off by keyboard
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
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Icon",
                                modifier = Modifier.size(120.dp),
                                tint = Color.Black
                            )
                        }
                    }

                    Button(onClick = { launcher.launch("image/*") }) {
                        Text("Select Image")
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    OutlinedTextField(
                        value = plateNumber,
                        onValueChange = { plateNumber = it },
                        label = { Text("Plate Number") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Numbers, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = vehicleType,
                        onValueChange = { vehicleType = it },
                        label = { Text("Vehicle Type") },
                        leadingIcon = { Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = driverName,
                        onValueChange = { driverName = it },
                        label = { Text("Driver Name") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Call, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))


                    Button(
                        onClick = {
                            carViewModel.uploadCar(
                                imageUri = imageUri,
                                driverName = driverName,
                                plateNumber = plateNumber,
                                vehicleType = vehicleType,
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