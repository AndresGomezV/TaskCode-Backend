package com.taskcode.service

import com.taskcode.dto.TaskDTO
import com.taskcode.mapper.TaskMapper
import com.taskcode.model.Role
import com.taskcode.model.Task
import com.taskcode.model.TaskStatus
import com.taskcode.model.User
import com.taskcode.repository.TaskRepository
import com.taskcode.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository, private val taskMapper: TaskMapper ) {

    fun getTasks(userId: Long?, currentUser: User, status: TaskStatus?): List<TaskDTO> {

        if (userId != null && currentUser.role != Role.ADMIN && currentUser.id != userId) {
            throw IllegalAccessException("You do not have permission to view these tasks")
        }
        return when {
            userId != null && status != null -> taskRepository.findByUserIdAndStatus(userId, status)
            userId != null -> taskRepository.findByUserId(userId)
            status != null -> taskRepository.findByStatus(status)
            currentUser.role == Role.ADMIN -> taskRepository.findAll()
            else -> taskRepository.findByUserId(currentUser.id!!)
            }.map { taskMapper.toResponseDTO(it) }
    }

    fun getTaskById(id: Long) : TaskDTO {
        val task = taskRepository.findById(id).orElseThrow {
            EntityNotFoundException("Task Id '$id' not found")
        }
        return taskMapper.toResponseDTO(task)
    }

    fun saveTask(task: Task): Task {

        return taskRepository.save(task)
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

    fun updateTaskStatus(taskId: Long, newStatus: TaskStatus, currentUser: User): TaskDTO {

        val task = taskRepository.findById(taskId).orElseThrow {
            EntityNotFoundException("Task Id '$taskId' not found")
        }

        if (currentUser.role != Role.ADMIN) {
            throw IllegalAccessException("Only admins can change tasks status")
        }

        task.status = newStatus
        val updatedTask = taskRepository.save(task)
        return taskMapper.toResponseDTO(updatedTask)

    }
}
