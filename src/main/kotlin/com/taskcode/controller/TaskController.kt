package com.taskcode.controller

import com.taskcode.dto.TaskDTO
import com.taskcode.dto.TaskRequest
import com.taskcode.dto.TaskUpdateDTO
import com.taskcode.mapper.TaskMapper
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

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService,
    private val userRepository: UserRepository,
    private val taskMapper: TaskMapper,

    ) {

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskDTO> {
        return ResponseEntity.ok(taskService.getTaskById(id))
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun getTasks(@RequestParam(required = false) userId: Long?,
                 @RequestParam(required = false) status: TaskStatus?,
                 authentication: Authentication
    ): ResponseEntity<List<TaskDTO>> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User  '${authentication.name}' not found")

        val tasks = taskService.getTasks(userId, currentUser, status)

        return ResponseEntity.ok(tasks)
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    fun createTask(@RequestBody taskDTO: TaskRequest, authentication: Authentication): ResponseEntity<TaskDTO> {
        val currentUser = userRepository.findByUsername(authentication.name)
            ?: throw EntityNotFoundException("User '${authentication.name}' not found")

        val task = Task(
            title = taskDTO.title,
            description = taskDTO.description,
            duration = taskDTO.duration,
            date = taskDTO.date,
            user = currentUser
        )

        val savedTask = taskService.saveTask(task)

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteTaskById(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Void> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User  '${authentication.name}' not found")
        taskService.deleteTask(id, currentUser)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun updateTaskStatus(@PathVariable id: Long, @RequestBody request: Map<String, String>, authentication: Authentication): ResponseEntity<TaskDTO> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User  '${authentication.name}' not found")

        val newStatus = TaskStatus.valueOf(request["status"] ?: throw IllegalArgumentException("Invalid status"))

        return ResponseEntity.ok(taskService.updateTaskStatus(id, newStatus, currentUser))

    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun updateTask(@PathVariable id: Long, @RequestBody newTask: TaskUpdateDTO , authentication: Authentication): ResponseEntity<TaskDTO> {
        val currentUser = userRepository.findByUsername(authentication.name) ?: throw EntityNotFoundException("User  '${authentication.name}' not found")

        return ResponseEntity.ok(taskService.updateTask(id, newTask, currentUser))
    }
}

