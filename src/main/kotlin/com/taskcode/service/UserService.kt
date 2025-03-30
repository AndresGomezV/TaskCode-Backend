package com.taskcode.service

import com.taskcode.dto.UserAuthDTO
import com.taskcode.dto.UserResponseDTO
import com.taskcode.jwt.JwtUtil
import com.taskcode.mapper.UserMapper
import com.taskcode.model.Role
import com.taskcode.model.User
import com.taskcode.repository.UserRepository
import jakarta.persistence.EntityExistsException
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val userMapper: UserMapper, private val jwtUtil: JwtUtil) {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    fun getUserById(id: Long): UserResponseDTO {
        val user = userRepository.findById(id).orElseThrow {
            EntityNotFoundException("User Id '$id' not found")
        }
        return userMapper.toResponseDTO(user)
    }

    fun getUserByUsername(username: String): UserResponseDTO {
        val user = userRepository.findByUsername(username)
            ?: throw EntityNotFoundException("Username '$username' not found")
        return userMapper.toResponseDTO(user)
    }

    fun saveUser(userAuthDTO: UserAuthDTO): UserResponseDTO {
        if (userRepository.findByUsername(userAuthDTO.username) != null) {
            throw EntityExistsException("Username '${userAuthDTO.username}' already exists")
        }

        val user = User(
            username = userAuthDTO.username,
            password = passwordEncoder.encode(userAuthDTO.password),
            role = Role.USER
        )

        val savedUser = userRepository.save(user)

        return userMapper.toResponseDTO(savedUser)
    }

    fun deleteUserById(id: Long) {
        val user = userRepository.findById(id).orElseThrow {
            EntityNotFoundException("Id '$id' not found")
        }
        userRepository.delete(user)
    }

    fun authenticateUser(userAuthDTO: UserAuthDTO): String {
        val user = userRepository.findByUsername(userAuthDTO.username)
            ?: throw EntityNotFoundException("Username '${userAuthDTO.username}' not found")

        if (!passwordEncoder.matches(userAuthDTO.password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        return jwtUtil.generateToken(user.username)
    }
}