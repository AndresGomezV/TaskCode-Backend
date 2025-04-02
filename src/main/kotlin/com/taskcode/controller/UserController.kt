package com.taskcode.controller

import com.taskcode.dto.UserAuthDTO
import com.taskcode.dto.UserResponseDTO
import com.taskcode.model.Role
import com.taskcode.repository.UserRepository

import com.taskcode.service.UserService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getUserById(@PathVariable id:Long): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("isAuthenticated()")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(userService.getUserByUsername(username))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteUserById(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUserById(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody userAuthDTO: UserAuthDTO): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userAuthDTO))
    }

    @PostMapping("/login")
    fun authenticateUser(@RequestBody userAuthDTO: UserAuthDTO): ResponseEntity<Map<String, String>> {
    val token = userService.authenticateUser(userAuthDTO)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun updateUserRole(@PathVariable id: Long, @RequestBody body: Map<String, String>, authentication: Authentication): ResponseEntity<UserResponseDTO> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User not found")

        val roleEnum = Role.valueOf(body["role"]?.uppercase() ?: throw IllegalArgumentException("Invalid role"))

        if (currentUser.id == id && roleEnum == Role.USER) {
            throw IllegalArgumentException("Admins cannot change their own role to USER")
        }

        return ResponseEntity.ok(userService.updateUserRole(id, roleEnum, currentUser))
    }
}

