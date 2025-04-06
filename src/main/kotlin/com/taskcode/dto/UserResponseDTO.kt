package com.taskcode.dto

import com.taskcode.model.Role

data class UserResponseDTO (
    val id: Long,
    val username: String,
    val role: Role,

)
