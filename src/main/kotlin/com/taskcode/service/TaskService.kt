package com.taskcode.service

import com.taskcode.dto.NotificationDTO
import com.taskcode.dto.NotificationType
import com.taskcode.dto.TaskDTO
import com.taskcode.dto.TaskUpdateDTO
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
class TaskService(
    private val taskRepository: TaskRepository,
    private val taskMapper: TaskMapper,
    private val notificationPublisher: NotificationPublisher,
    private val userRepository: UserRepository
) {

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

    fun getTaskById(id: Long): TaskDTO {
        val task = taskRepository.findById(id).orElseThrow {
            EntityNotFoundException("Task Id '$id' not found")
        }
        return taskMapper.toResponseDTO(task)
    }

    fun saveTask(task: Task): TaskDTO {
        val savedTask = taskRepository.save(task)

        val adminUsers = userRepository.findAllByRole(Role.ADMIN)

        adminUsers.forEach { admin ->
            val notification = NotificationDTO(
                userId = admin.id!!,
                senderUsername = savedTask.user?.username!!,
                taskId = savedTask.id!!,
                taskTitle = savedTask.title,
                notificationType = NotificationType.TASK_PENDING
            )
        notificationPublisher.sendNotification(notification)
        }
        return taskMapper.toResponseDTO(task)
    }

    fun deleteTask(id: Long, currentUser: User) {
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

        notificationPublisher.sendNotification(NotificationDTO(
            userId = task.user?.id!!,
            senderUsername = currentUser.username,
            taskId = task.id!!,
            taskTitle = task.title,
            notificationType = when (newStatus) {
                TaskStatus.ACCEPTED -> NotificationType.TASK_ACCEPTED
                TaskStatus.REJECTED -> NotificationType.TASK_REJECTED
                else -> NotificationType.TASK_PENDING
            }
        ))
        return taskMapper.toResponseDTO(updatedTask)

    }

    fun updateTask(taskId: Long, newTask: TaskUpdateDTO, currentUser: User): TaskDTO {
        val task = taskRepository.findById(taskId).orElseThrow {
            EntityNotFoundException("Task Id '$taskId' not found")
        }

        if (currentUser.role == Role.USER && task.user?.id != currentUser.id) {
            throw IllegalAccessException("You are not authorized to modify this task")
        }


        task.title = newTask.title
        task.description = newTask.description
        task.duration = newTask.duration

        taskRepository.save(task)
        return taskMapper.toResponseDTO(task)
    }
}
