package com.thecodecup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promo")
data class PromoEntity(
    @PrimaryKey val code: String,
    val discountPercent: Int
)
