package com.taskcode.controller

import com.taskcode.dto.UserAuthDTO
import com.taskcode.dto.UserResponseDTO

import com.taskcode.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id:Long): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @GetMapping("/username/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(userService.getUserByUsername(username))
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id:Long): ResponseEntity<Void> {
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

}