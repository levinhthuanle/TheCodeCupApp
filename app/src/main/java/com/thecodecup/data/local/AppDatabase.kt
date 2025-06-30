package com.thecodecup.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thecodecup.data.model.CartItemEntity
import com.thecodecup.data.model.UserProfileEntity
import com.thecodecup.data.model.OrderEntity
import com.thecodecup.data.model.RewardHistoryEntity

@Database(
    entities = [
        CartItemEntity::class,
        UserProfileEntity::class,
        OrderEntity::class,
        RewardHistoryEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun orderDao(): OrderDao
    abstract fun rewardDao(): RewardDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "the_code_cup_db"
            )
                .fallbackToDestructiveMigration() // ⚠ Tự động xóa DB cũ khi schema thay đổi
                .build()
    }
}

