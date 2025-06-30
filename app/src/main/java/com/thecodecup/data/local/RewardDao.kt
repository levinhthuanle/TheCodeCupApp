package com.thecodecup.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thecodecup.data.model.RewardHistoryEntity

@Dao
interface RewardDao {
    @Insert
    suspend fun insert(reward: RewardHistoryEntity)

    @Query("SELECT * FROM reward_history ORDER BY redeemedAt DESC")
    suspend fun getAll(): List<RewardHistoryEntity>
}
