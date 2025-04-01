package com.taskcode.repository

import com.taskcode.model.TaskStatus
import com.taskcode.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findByUserId(userId: Long): List<Task>
    fun findByStatus(status: TaskStatus): List<Task>
    fun findByUserIdAndStatus(userId: Long, status: TaskStatus): List<Task>
}