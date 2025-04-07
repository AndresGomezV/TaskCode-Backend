package com.taskcode.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String,

    @Column(nullable = false)
    var duration: Int,

    @Column(nullable = false)
    var creationDate: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var date: LocalDate = LocalDate.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Enumerated(EnumType.STRING)
    var status: TaskStatus = TaskStatus.PENDING
    )

enum class TaskStatus {
    PENDING, ACCEPTED, REJECTED
}