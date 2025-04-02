package com.taskcode.mapper

import com.taskcode.dto.UserAuthDTO
import com.taskcode.dto.UserResponseDTO
import com.taskcode.model.Role
import com.taskcode.model.User
import org.springframework.stereotype.Component


@Component
class UserMapper {

    fun toResponseDTO(user: User): UserResponseDTO {
        return UserResponseDTO(
            username = user.username,
            role = user.role
        )
    }

    fun toEntity(userAuthDTO: UserAuthDTO): User {
        return User(
            id = null, //null porque id es generado automáticamente por la bd
            username = userAuthDTO.username,
            password = userAuthDTO.password,
            role = userAuthDTO.role ?: Role.USER
        )
    }
}