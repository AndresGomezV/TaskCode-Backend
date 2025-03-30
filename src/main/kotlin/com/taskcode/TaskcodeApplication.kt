package com.taskcode

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskcodeApplication

fun main(args: Array<String>) {
	runApplication<TaskcodeApplication>(*args)
}
