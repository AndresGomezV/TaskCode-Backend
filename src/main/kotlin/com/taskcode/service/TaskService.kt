package com.taskcode.service

import com.taskcode.model.Task
import com.taskcode.repository.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getTaskById(id: Long) : Task? {
        return taskRepository.findById(id).orElse(null)
    }

    fun saveTask(task: Task): Task {
        return taskRepository.save(task)
    }

    fun deleteTask(id: Long) {
        return taskRepository.deleteById(id)
    }
}