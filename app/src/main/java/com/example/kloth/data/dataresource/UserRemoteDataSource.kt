package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.UserDto

interface UserRemoteDataSource {
    suspend fun getAllUsers(): List<UserDto>
    suspend fun getUserById(id: String): UserDto
}
