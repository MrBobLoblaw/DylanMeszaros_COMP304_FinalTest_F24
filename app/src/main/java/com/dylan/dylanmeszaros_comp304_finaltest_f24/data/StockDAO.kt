package com.dylan.dylanmeszaros_comp304_finaltest_f24.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StockDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stockInfo: StockInfo)

    @Delete
    fun delete(stockInfo: StockInfo)

    @Update
    fun update(stockInfo: StockInfo)

    @Query("SELECT * FROM stock_info")
    fun getAll(): MutableList<StockInfo>

    @Query("SELECT * FROM stock_info WHERE stockSymbol = :symbol LIMIT 1")
    fun getStockBySymbol(symbol: String): StockInfo?
}