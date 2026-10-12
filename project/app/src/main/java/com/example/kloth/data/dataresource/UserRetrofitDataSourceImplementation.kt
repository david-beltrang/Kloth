package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.UserDto
import com.example.kloth.data.services.UserRetrofitService
import javax.inject.Inject

class UserRetrofitDataSourceImplementation @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getAllUsers(): List<UserDto> = service.getAllUsers()

    override suspend fun getUserById(id: String): UserDto = service.getUserById(id)
}
