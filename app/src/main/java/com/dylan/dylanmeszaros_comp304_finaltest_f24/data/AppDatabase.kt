package com.dylan.dylanmeszaros_comp304_finaltest_f24.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StockInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDAO
}