package com.thecodecup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_history")
data class RewardHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rewardName: String,
    val redeemedAt: Long
)
