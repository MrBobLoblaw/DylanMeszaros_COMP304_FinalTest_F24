package com.dylan.meszaros.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dylan.meszaros.data.StockDAO
import com.dylan.meszaros.data.StockInfo

@Database(entities = [StockInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDAO
}