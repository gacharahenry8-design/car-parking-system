package com.example.carparkingsystem.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.compose.runtime.mutableStateListOf
import com.example.carparkingsystem.models.CarModel
import com.example.carparkingsystem.navigation.ROUTE_DASHBOARD
import com.example.carparkingsystem.navigation.ROUTE_VIEWCARS
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class CarViewModel : ViewModel() {

    // 🔥 Cloudinary config
    private val cloudinaryUrl =
        "https://api.cloudinary.com/v1_1/dn8beyx5a/image/upload"
    private val uploadPreset = "pic_folder"

    private val client = OkHttpClient()

    fun uploadCar(
        imageUri: Uri?,
        driverName: String,
        plateNumber: String,
        vehicleType: String,
        phoneNumber: String,
        context: Context,
        navController: NavController,
        vehicleColor: String, // These were already correctly in your parameters
        entryTime: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 📸 Upload image to Cloudinary first
                val imageUrl = imageUri?.let {
                    uploadToCloudinary(context, it)
                }

                // 🔥 Get Firebase Reference
                val ref = FirebaseDatabase.getInstance()
                    .getReference("Cars")
                    .push()

                // ✅ FIXED: Mapping the values to your Model
                val carData = CarModel(
                    id = ref.key,
                    plateNumber = plateNumber,
                    vehicleType = vehicleType,
                    vehicleColor = vehicleColor,
                    entryTime = entryTime,
                    driverName = driverName,
                    phoneNumber = phoneNumber,
                    imageUrl = imageUrl // This is the URL returned from Cloudinary
                )

                // Save to Firebase
                ref.setValue(carData).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Car saved successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARD)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // ☁️ Cloudinary upload (FIXED + INSIDE CLASS)
    private fun uploadToCloudinary(context: Context, uri: Uri): String {

        val inputStream: InputStream =
            context.contentResolver.openInputStream(uri)
                ?: throw Exception("Cannot open image")

        val bytes = inputStream.readBytes()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), bytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder()
            .url(cloudinaryUrl)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Upload failed: ${response.message}")
        }

        val body = response.body?.string()

        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(body ?: "")
            ?.groupValues?.get(1)

        return secureUrl ?: throw Exception("Image URL not found")
    }

    private val _cars = mutableStateListOf<CarModel>()
    val cars: List<CarModel> = _cars
    fun fetchCar(context: Context) {
        try {
            val ref = FirebaseDatabase.getInstance().getReference("Cars")
            ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    _cars.clear()
                    for (child in snapshot.children) {
                        val car = child.getValue(CarModel::class.java)
                        car?.let {
                            it.id = child.key
                            _cars.add(it)
                        }
                    }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Toast.makeText(context, "Failed to load cars: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
        } catch (e: Exception) {
            // Handle cases where Firebase is not initialized (like in Preview)
        }
    }
    fun updateCar(
        carId: String,
        imageUri: Uri?,
        driverName: String,
        plateNumber: String,
        vehicleType: String,
        phoneNumber: String,
        vehicleColor: String,
        entryTime: String,
        context: Context,
        navController: NavController,
        currentImageUrl: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Handle Image: If new image picked, upload it.
                // Otherwise, use the provided currentImageUrl.
                val imageUrl = if (imageUri != null) {
                    uploadToCloudinary(context, imageUri)
                } else {
                    currentImageUrl
                }

                // 2. Map the updated values
                val updatedCar = mapOf(
                    "id" to carId,
                    "plateNumber" to plateNumber,
                    "vehicleType" to vehicleType,
                    "driverName" to driverName,
                    "phoneNumber" to phoneNumber,
                    "imageUrl" to (imageUrl ?: ""),
                    "vehicleColor" to vehicleColor,
                    "entryTime" to entryTime
                )

                // 3. Update Firebase Realtime Database
                val ref = FirebaseDatabase.getInstance().getReference("Cars/$carId")
                ref.updateChildren(updatedCar).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Car Updated Successfully", Toast.LENGTH_LONG).show()
                    // Use popBackStack() to go back to the list screen
                    navController.popBackStack()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun deleteCar(carId: String, context: Context, navController: NavController) {
        val ref = FirebaseDatabase.getInstance().getReference("Cars/$carId")
        ref.removeValue().addOnSuccessListener {
            _cars.removeAll { it.id == carId }
            Toast.makeText(context, "Car deleted successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to delete car: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
}