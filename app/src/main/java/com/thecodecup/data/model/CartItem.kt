package com.thecodecup.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: Int,
    val name: String,
    val imageResId: Int,
    val shot: String,
    val type: String,
    val size: String,
    val ice: String,
    val quantity: Int,
    val price: Double
) : Parcelable
