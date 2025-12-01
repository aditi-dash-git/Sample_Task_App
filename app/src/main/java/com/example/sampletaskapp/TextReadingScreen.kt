package com.example.sampletaskapp

import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TextReadingScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    var recorder: MediaRecorder? by remember { mutableStateOf(null) }
    var audioFilePath by remember { mutableStateOf("") }

    var isRecording by remember { mutableStateOf(false) }
    var hasRecorded by remember { mutableStateOf(false) }
    var recordDuration by remember { mutableStateOf(0) }
    var errorMsg by remember { mutableStateOf("") }

    var check1 by remember { mutableStateOf(false) }
    var check2 by remember { mutableStateOf(false) }
    var check3 by remember { mutableStateOf(false) }

    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

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
                "INSTRUCTIONS DYNAMIC TEXT INSTRUCTIONS...",
                fontSize = 18.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (!hasRecorded) {
                // 🔵 Mic button
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    val buttonColor = if (isRecording) Color.Red else Color(0xFF1976D2)

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(buttonColor, CircleShape)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        errorMsg = ""
                                        recordDuration = 0
                                        hasRecorded = false

                                        val file = File(
                                            context.getExternalFilesDir(null),
                                            "audio_${System.currentTimeMillis()}.mp3"
                                        )
                                        audioFilePath = file.absolutePath

                                        recorder = MediaRecorder().apply {
                                            setAudioSource(MediaRecorder.AudioSource.MIC)
                                            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                            setOutputFile(audioFilePath)
                                            prepare()
                                            start()
                                        }

                                        isRecording = true

                                        // Wait until finger released
                                        try {
                                            tryAwaitRelease()
                                        } finally {
                                            isRecording = false
                                            hasRecorded = true
                                            try { recorder?.stop() } catch (_: Exception) {}
                                            recorder?.release()
                                            recorder = null
                                        }
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Press and hold to record",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                if (isRecording) {
                    Text("Recording: ${recordDuration}s", color = Color.Red)
                }
            }

            else {
                // 🎧 Playback Section
                val mediaPlayer = remember { MediaPlayer() }
                var isPlaying by remember { mutableStateOf(false) }
                var playProgress by remember { mutableStateOf(0f) }

                LaunchedEffect(isPlaying) {
                    if (isPlaying) {
                        while (isPlaying && mediaPlayer.isPlaying) {
                            playProgress =
                                mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration
                            delay(200)
                        }
                    }
                }

                Text("Your Recording", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE4EBF5)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                                if (!isPlaying) {
                                    try {
                                        mediaPlayer.reset()
                                        mediaPlayer.setDataSource(audioFilePath)
                                        mediaPlayer.prepare()
                                        mediaPlayer.start()
                                        isPlaying = true
                                    } catch (_: Exception) {
                                        errorMsg = "Playback error!"
                                    }
                                } else {
                                    mediaPlayer.pause()
                                    isPlaying = false
                                }
                            }
                        ) {
                            Icon(
                                if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = null,
                                tint = Color(0xFF1976D2)
                            )
                        }

                        LinearProgressIndicator(
                            progress = playProgress,
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text("Before Submitting check these:", fontSize = 16.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = check1, onCheckedChange = { check1 = it })
                    Text("This Audio has no background noise")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = check2, onCheckedChange = { check2 = it })
                    Text("Passage has no mistakes while reading")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = check3, onCheckedChange = { check3 = it })
                    Text("Beech me koi galti nahi")
                }

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = {
                        hasRecorded = false
                        check1 = false
                        check2 = false
                        check3 = false
                        isPlaying = false
                        mediaPlayer.reset()
                    }) {
                        Text("Record again", color = Color(0xFF1976D2))
                    }

                    Button(
                        enabled = check1 && check2 && check3,
                        onClick = {
                            if (recordDuration < 10) {
                                errorMsg = "Recording too short (min 10s)"
                            } else if (recordDuration > 20) {
                                errorMsg = "Recording too long (max 20s)"
                            } else {
                                TaskRepository.addTask(
                                    Task(
                                        id = TaskRepository.nextId(),
                                        taskType = "text_reading",
                                        textOrImageUrl = "Sample text",
                                        audioPath = audioFilePath,
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

    if (isRecording) {
        LaunchedEffect(Unit) {
            while (isRecording) {
                delay(1000)
                recordDuration++
            }
        }
    }
}
