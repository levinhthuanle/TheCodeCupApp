package com.thecodecup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageResId: Int,
    val shot: String,
    val type: String,
    val size: String,
    val ice: String,
    val quantity: Int,
    val price: Double
)
