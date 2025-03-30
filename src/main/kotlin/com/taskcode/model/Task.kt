package com.taskcode.model

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    val status: taskStatus = taskStatus.PENDING
    )

enum class taskStatus {
    PENDING, ACCEPTED, REJECTED
}