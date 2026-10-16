package com.example.kloth.data.datasource

import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDto

interface UserRemoteDataSource {
    suspend fun getAllUsers(): List<UserDto>
    suspend fun getUserById(id: String): UserDto
    suspend fun registerUser(registerUserDto: RegisterUserDto, userId : String): Unit
}
