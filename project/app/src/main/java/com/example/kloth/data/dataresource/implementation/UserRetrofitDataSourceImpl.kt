package com.example.kloth.data.dataresource.implementation

import com.example.kloth.data.dataresource.UserRemoteDataSource
import com.example.kloth.data.dataresource.services.UserRetrofitService
import com.example.kloth.data.dto.UserDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserDto> = service.getUsers()
    override suspend fun getUserById(id: String): UserDto = service.getUserById(id)
}
