package com.thecodecup.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thecodecup.data.model.PromoEntity

@Dao
interface PromoDao {
    @Query("SELECT * FROM promo WHERE code = :code")
    suspend fun getPromoByCode(code: String): PromoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(promo: PromoEntity)
}
