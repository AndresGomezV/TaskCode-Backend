package com.taskcode.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column (nullable = false, unique = true)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tasks: MutableList<Task> = mutableListOf()
)

enum class Role {
    USER, ADMIN
}