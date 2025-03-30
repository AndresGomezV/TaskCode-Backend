package com.taskcode.controller

import com.taskcode.model.Task
import com.taskcode.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<Task> {
        return ResponseEntity.ok(taskService.getTaskById(id))
    }

    @PostMapping
    fun createTask(@RequestBody task: Task): ResponseEntity<Task> {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.saveTask(task))
    }

    @DeleteMapping("/{id}")
    fun deleteTaskById(@PathVariable id: Long): ResponseEntity<Void> {
        taskService.deleteTask(id)
        return ResponseEntity.noContent().build()
    }
}

