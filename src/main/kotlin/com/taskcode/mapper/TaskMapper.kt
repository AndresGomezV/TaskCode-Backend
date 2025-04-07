package com.taskcode.mapper

import com.taskcode.dto.TaskDTO
import com.taskcode.model.Task
import org.springframework.stereotype.Component

@Component
class TaskMapper {

    fun toResponseDTO(task: Task): TaskDTO {
        return TaskDTO(
            id = task.id,
            title = task.title,
            description = task.description,
            duration = task.duration,
            creationDate = task.creationDate,
            date = task.date,
            status = task.status,
            userId = task.user?.id
        )
    }
}