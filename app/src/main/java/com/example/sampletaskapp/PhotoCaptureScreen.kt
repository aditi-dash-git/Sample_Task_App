package com.example.sampletaskapp

import android.Manifest
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PhotoCaptureScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var textDesc by remember { mutableStateOf("") }

    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        .format(Date())

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                "IMG_${System.currentTimeMillis()}",
                null
            )
            imageUri = Uri.parse(path)
        }
    }

    Scaffold(
        topBar = { TopBar("Sample Task", onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Capture an image and describe it:", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (imageUri == null) {
                Button(onClick = { cameraLauncher.launch(null) }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture Image")
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textDesc,
                onValueChange = { textDesc = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Describe the photo...") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                enabled = imageUri != null,
                onClick = {
                    TaskRepository.addTask(
                        Task(
                            id = TaskRepository.nextId(),
                            taskType = "photo_capture",
                            textOrImageUrl = textDesc,
                            imagePath = imageUri?.path,
                            audioPath = null,
                            durationSec = 0,
                            timestamp = timestamp
                        )
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}
