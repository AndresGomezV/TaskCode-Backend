package com.taskcode.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.taskcode.dto.NotificationDTO
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service

@Service
class NotificationPublisher(private val jmsTemplate: JmsTemplate) {

    private val objectMapper = jacksonObjectMapper()

    fun sendNotification(notification: NotificationDTO) {
        val json = objectMapper.writeValueAsString(notification)
        println("Sending notification to ActiveMQ: $notification")
        jmsTemplate.convertAndSend("notification-queue", json)
    }
}