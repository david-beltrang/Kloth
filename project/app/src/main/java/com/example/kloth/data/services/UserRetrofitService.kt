package com.example.kloth.data.services

import com.example.kloth.data.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRetrofitService {

    @GET("users")
    suspend fun getAllUsers(): List<UserDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto
}
