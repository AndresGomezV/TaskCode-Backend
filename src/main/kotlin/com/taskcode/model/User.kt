package com.taskcode.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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
    @JsonIgnore
    var tasks: MutableList<Task> = mutableListOf()
)
enum class Role : GrantedAuthority {
    USER,
    ADMIN;

    override fun getAuthority(): String {
        return "ROLE_$name"
    }
}