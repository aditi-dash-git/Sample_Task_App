package com.example.sampletaskapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskSelectionScreen(
    onTextReadingClick: () -> Unit,
    onImageDescriptionClick: () -> Unit,
    onPhotoCaptureClick: () -> Unit,
    onViewHistoryClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(title = "Task Selection", onBack = { }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onTextReadingClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Text Reading Task", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onImageDescriptionClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Image Description Task", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onPhotoCaptureClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Photo Capture Task", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onViewHistoryClick) {
                    Text(
                        text = "View Task History",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
