package com.taskcode.dto

import java.time.LocalDate

data class TaskRequest(
    val title: String,
    val description: String,
    val duration: Int,
    val date: LocalDate
)