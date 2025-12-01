package com.example.sampletaskapp

data class Task(
    val id: Int,
    val taskType: String,
    val textOrImageUrl: String?,
    val imagePath: String? = null,
    val audioPath: String?,
    val durationSec: Int,
    val timestamp: String
)

object TaskRepository {
    private val taskList = mutableListOf<Task>()

    fun addTask(task: Task) {
        taskList.add(task)
    }

    fun getTasks(): List<Task> = taskList // 👈 Renamed

    fun nextId(): Int = taskList.size + 1
}
