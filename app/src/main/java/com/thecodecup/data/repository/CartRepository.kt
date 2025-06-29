package com.thecodecup.data.repository

import com.thecodecup.data.local.CartDao
import com.thecodecup.data.model.CartItemEntity

class CartRepository(private val dao: CartDao) {
    suspend fun getAll() = dao.getAll()
    suspend fun insert(item: CartItemEntity) = dao.insert(item)
    suspend fun delete(item: CartItemEntity) = dao.delete(item)
    suspend fun clear() = dao.clearAll()
}
