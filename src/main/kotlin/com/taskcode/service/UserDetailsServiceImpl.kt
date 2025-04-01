package com.taskcode.service

import com.taskcode.model.User
import com.taskcode.repository.UserRepository
import com.taskcode.service.UserDetailsImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")
        return UserDetailsImpl(user)
    }
}
