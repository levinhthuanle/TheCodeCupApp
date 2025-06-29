package com.thecodecup.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thecodecup.data.model.OrderEntity

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE status = 'ongoing'")
    suspend fun getOngoingOrders(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE status = 'history'")
    suspend fun getHistoryOrders(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE status = :status")
    suspend fun getOrdersByStatus(status: String): List<OrderEntity>

    @Query("UPDATE orders SET status = :newStatus WHERE id = :id")
    suspend fun updateStatus(id: Int, newStatus: String)

}
