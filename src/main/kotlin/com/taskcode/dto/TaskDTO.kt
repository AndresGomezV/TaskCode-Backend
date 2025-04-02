package com.taskcode.dto

import com.taskcode.model.TaskStatus
import java.time.LocalDateTime

data class TaskDTO(
    val id: Long?,
    val title: String,
    val description: String,
    val duration: Int,
    val creationDate: LocalDateTime,
    val status: TaskStatus,
    val userId: Long?
    )
