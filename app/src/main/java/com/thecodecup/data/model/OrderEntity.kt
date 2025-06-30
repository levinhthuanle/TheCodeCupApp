package com.thecodecup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageResId: Int,
    val description: String,
    val quantity: Int,
    val price: Double,
    val status: String = "ongoing",
    val timestamp: Long,
    val address: String = ""
)
