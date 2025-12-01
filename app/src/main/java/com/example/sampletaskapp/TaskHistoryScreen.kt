package com.example.sampletaskapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Bright blue used across the app
private val PrimaryBlue = Color(0xFF0D6EFD)
private val CardBorderColor = Color(0xFFE0E0E0)
private val TaskChipColor = Color(0xFFF5F6FA)

@Composable
fun TaskHistoryScreen(onBack: () -> Unit) {

    val tasks = TaskRepository.getTasks()
    val totalTasks = tasks.size
    val totalDurationSec = tasks.sumOf { it.durationSec }

    Scaffold(
        topBar = { TopBar("Task History", onBack) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            // Work Report title
            Text(
                text = "Work Report",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Two stat cards row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                StatCardHistory(
                    value = "$totalTasks",
                    labelTop = "Total",
                    labelBottom = "Tasks",
                    modifier = Modifier.weight(1f)
                )

                StatCardHistory(
                    value = formatDuration(totalDurationSec),
                    labelTop = "Duration",
                    labelBottom = "Recorded",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tasks",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // List of tasks
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TaskHistoryItem(task = task)
                }
            }

            // "See More" text at bottom
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "See More",
                fontSize = 14.sp,
                color = PrimaryBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun StatCardHistory(
    value: String,
    labelTop: String,
    labelBottom: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222F3E)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = labelTop,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = labelBottom,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun TaskHistoryItem(task: Task) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = TaskChipColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Task - ${task.id}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF222F3E)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Duration ${task.durationSec}sec   |   ${task.timestamp}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

fun formatDuration(sec: Int): String {
    val min = sec / 60
    val rem = sec % 60
    return "${min}m ${rem}s"
}
