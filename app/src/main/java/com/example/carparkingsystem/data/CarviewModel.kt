package com.example.carparkingsystem.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.carparkingsystem.models.CarModel
import com.example.carparkingsystem.navigation.ROUTE_DASHBOARD
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

    // 🧠 TODOs (later)
    fun fetchCar() {}
    fun updateCar() {}
    fun deleteCar() {}
}