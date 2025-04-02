package com.taskcode.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = false)
    val duration: Int,

    @Column(nullable = false)
    val creationDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @Enumerated(EnumType.STRING)
    var status: TaskStatus = TaskStatus.PENDING
    )

enum class TaskStatus {
    PENDING, ACCEPTED, REJECTED
}