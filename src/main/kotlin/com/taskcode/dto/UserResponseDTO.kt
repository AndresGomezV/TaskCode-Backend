package com.taskcode.dto

import com.taskcode.model.Role

data class UserResponseDTO (
    val username: String,
    val role: Role,
)
