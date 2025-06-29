package com.thecodecup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1, // chỉ có 1 user
    val name: String,
    val phone: String,
    val email: String,
    val address: String
)
