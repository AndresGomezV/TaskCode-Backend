package com.taskcode.dto

import java.io.Serializable


data class NotificationDTO(
    val userId: Long,
    val senderUsername: String,
    val taskTitle: String,
    val taskId: Long,
    val notificationType: NotificationType,
) : Serializable

enum class NotificationType {
    TASK_ACCEPTED, TASK_REJECTED, TASK_PENDING
}