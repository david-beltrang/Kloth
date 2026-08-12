package com.example.kloth.data

data class Product(
    val id: String,
    val title: String,
    val brand: String,
    val price: String,
    val category: String,
    val colorName: String,
    val description: String,
    val imageUrl: String,
    val rating: Float,
    val reviewCount: Int
)