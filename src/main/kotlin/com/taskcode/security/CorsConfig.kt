package com.taskcode.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("http://localhost:4200") // Permite Angular
        config.addAllowedMethod("*") // Permite todos los métodos (GET, POST, PUT, DELETE, etc.)
        config.addAllowedHeader("*") // Permite todos los headers
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}