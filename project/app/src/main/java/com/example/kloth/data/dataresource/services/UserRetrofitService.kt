package com.example.kloth.data.dataresource.services

import com.example.kloth.data.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

// Mismas rutas que user.routes.js del backend
interface UserRetrofitService {
    @GET("/users")
    suspend fun getUsers(): List<UserDto>

    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto
}
