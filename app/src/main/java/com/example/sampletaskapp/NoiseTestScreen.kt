package com.example.sampletaskapp

import android.Manifest
import android.media.MediaRecorder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.sin

@Composable
fun NoiseTestScreen(onTestPass: () -> Unit) {

    val context = LocalContext.current
    var noiseLevel by remember { mutableStateOf(0) }
    var result by remember { mutableStateOf("") }
    var isTesting by remember { mutableStateOf(false) }
    var recorder: MediaRecorder? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            recorder = null
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) result = "Microphone permission required"
    }

    Scaffold(
        topBar = { TopBar("Sample Task", onBack = {}) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Test Ambient Noise Level",
                fontSize = 22.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Before you can start the call we will have to check your ambient noise level.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(85.dp))

            NoiseGauge(noiseLevel)

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    result = ""
                    noiseLevel = 0
                    isTesting = true
                },
                enabled = !isTesting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text("Start Test")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (result.isNotEmpty()) {
                Text(
                    result,
                    fontSize = 18.sp,
                    color = if (result.contains("Good")) Color.Green else Color.Red
                )
            }

            if (result.contains("Good")) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onTestPass) {
                    Text("Continue")
                }
            }
        }
    }

    if (isTesting) {
        LaunchedEffect(Unit) {
            try {
                recorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                    val file = context.getExternalFilesDir(null)?.absolutePath + "/temp.3gp"
                    setOutputFile(file)

                    prepare()
                    start()
                }

                var total = 0
                repeat(30) {
                    val amp = recorder?.maxAmplitude ?: 0
                    val db = if (amp > 0) (20 * log10(amp.toDouble())).toInt() else 0
                    total += db
                    noiseLevel = db
                    delay(100)
                }

                recorder?.stop()
                recorder = null

                val avg = total / 30
                result = if (avg < 40) "Good to proceed 👍" else "Please move to a quieter place 🛑"

            } catch (e: Exception) {
                result = "Noise Test Failed"
            }
            isTesting = false
        }
    }
}


@Composable
fun NoiseGauge(noiseLevel: Int) {
    val minDb = 0
    val maxDb = 60
    val safeDbLimit = 45

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {

        val center = Offset(size.width / 2, size.height / 2 + 40.dp.toPx()) // SHIFT UP
        val radius = size.width / 3

        val db = noiseLevel.coerceIn(0, 60)
        val angle = db * 180f / 60f - 90f
        val rad = Math.toRadians(angle.toDouble())

        val strokeWidth = 28.dp.toPx()

        // 🔵 Blue arc (0–45 dB)
        // Blue arc 0–40 dB
        drawArc(
            color = Color(0xFF007AFF),
            startAngle = 180f,
            sweepAngle = 180f * (40f / 60f),
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = 55f, cap = StrokeCap.Round)
        )

// Red arc 40–60 dB
        drawArc(
            color = Color(0xFFFF3B30),
            startAngle = 180f + 180f * (40f / 60f),
            sweepAngle = 180f * (20f / 60f),
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = 55f, cap = StrokeCap.Round)
        )


        // ▶ Pointer
        val pointerRadius = radius - strokeWidth / 2
        val pointerX = center.x + pointerRadius * cos(rad).toFloat()
        val pointerY = center.y + pointerRadius * sin(rad).toFloat()

        drawLine(
            color = Color.Black,
            start = center,
            end = Offset(pointerX, pointerY),
            strokeWidth = 6.dp.toPx()
        )
    }


    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "$noiseLevel dB",
        fontSize = 28.sp,
        color = Color.Black
    )
}
