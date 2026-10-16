package com.example.kloth.data.datasource.implementation

import com.example.kloth.data.datasource.UserRemoteDataSource
import com.example.kloth.data.datasource.services.UserRetrofitService
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserDto> = service.getUsers()
    override suspend fun getUserById(id: String): UserDto = service.getUserById(id)
    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        TODO("falta por implementar epara retrofit")
    }
}
