package com.example.sampletaskapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ImageDescriptionScreen(onBack: () -> Unit) {

    var hasRecorded by remember { mutableStateOf(false) }
    var recordDuration by remember { mutableStateOf(12) }
    var errorMsg by remember { mutableStateOf("") }

    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        .format(Date())

    Scaffold(
        topBar = { TopBar("Sample Task", onBack) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                "Describe what you see in your language.",
                fontSize = 18.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sample static image from app drawable
            Image(
                painter = painterResource(id = R.drawable.sample_image),
                contentDescription = "Sample Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!hasRecorded) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            hasRecorded = true
                            recordDuration = 12
                        },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Press and hold to record", modifier = Modifier.align(Alignment.CenterHorizontally))

            } else {

                Text("Your Recording:")
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFFE4EBF5))
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red)
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (recordDuration < 10)
                            errorMsg = "Recording too short (min 10s)"
                        else if (recordDuration > 20)
                            errorMsg = "Recording too long (max 20s)"
                        else {
                            TaskRepository.addTask(
                                Task(
                                    id = TaskRepository.nextId(),
                                    taskType = "image_description",
                                    textOrImageUrl = "sample_image_url_here",
                                    audioPath = "/local/path/image_desc.mp3",
                                    durationSec = recordDuration,
                                    timestamp = timestamp
                                )
                            )
                            onBack()
                        }
                    }
                ) {
                    Text("Submit")
                }
            }
        }
    }
}
