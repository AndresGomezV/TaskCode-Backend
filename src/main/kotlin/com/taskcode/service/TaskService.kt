package com.taskcode.service

import com.taskcode.model.Role
import com.taskcode.model.Task
import com.taskcode.model.User
import com.taskcode.repository.TaskRepository
import com.taskcode.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository, private val userRepository: UserRepository) {

    fun getTasksByUser(userId: Long, currentUser: User): List<Task> {

        if (currentUser.role != Role.ADMIN && currentUser.id != userId) {
            throw IllegalAccessException("You do not have permission to view these tasks")
        }

        return taskRepository.findByUserId(userId)
    }

    fun getTaskById(id: Long) : Task? {
        return taskRepository.findById(id).orElseThrow {
            EntityNotFoundException("Task Id '$id' not found")
        }
    }

    fun saveTask(task: Task, currentUser: User): Task {

        val newTask = Task(
            title = task.title,
            description = task.description,
            duration = task.duration,
            user = currentUser,
            status = task.status
        )
        return taskRepository.save(newTask)
    }

    fun deleteTask(id: Long,  currentUser: User) {
        val task = taskRepository.findById(id).orElseThrow {
            EntityNotFoundException("Task Id '$id' not found")
        }

        if (task.user != currentUser && currentUser.role != Role.ADMIN) {
            throw IllegalAccessException("You do not have permission to delete this task")
        }
        taskRepository.deleteById(id)
    }
}
