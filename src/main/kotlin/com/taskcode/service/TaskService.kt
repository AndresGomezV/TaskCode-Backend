package com.taskcode.service

import com.taskcode.model.Task
import com.taskcode.repository.TaskRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getTaskById(id: Long) : Task? {
        return taskRepository.findById(id).orElseThrow {
            EntityNotFoundException("Task Id '$id' not found")
        }
    }

    fun saveTask(task: Task): Task {
        return taskRepository.save(task)
    }

    fun deleteTask(id: Long) {
        if (!taskRepository.existsById(id)) {
            throw EntityNotFoundException("Task Id '$id' not found")
        }
        taskRepository.deleteById(id)
    }
}