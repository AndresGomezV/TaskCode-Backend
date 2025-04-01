package com.taskcode.dto

import com.taskcode.model.Role

data class UserAuthDTO(
    var username: String,
    var password: String,
    var role: Role
)