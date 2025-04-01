package com.taskcode.controller

import com.taskcode.model.Task
import com.taskcode.model.TaskStatus
import com.taskcode.service.TaskService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import com.taskcode.repository.UserRepository
import org.apache.coyote.Response

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService, private val userRepository: UserRepository) {

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<Task> {
        return ResponseEntity.ok(taskService.getTaskById(id))
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getTasks(@RequestParam(required = false) userId: Long?,
                 @RequestParam(required = false) status: TaskStatus?,
                 authentication: Authentication
    ): ResponseEntity<List<Task>> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User not found")

        val tasks = taskService.getTasks(userId, currentUser, status)
        return ResponseEntity.ok(tasks)
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    fun createTask(@RequestBody task: Task, authentication: Authentication): ResponseEntity<Task> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User not found")
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.saveTask(task, currentUser))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteTaskById(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Void> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User not found")
        taskService.deleteTask(id, currentUser)
        return ResponseEntity.noContent().build()
    }
}

