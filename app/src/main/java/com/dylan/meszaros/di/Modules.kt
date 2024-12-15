package com.dylan.meszaros.di

import androidx.room.Room
import com.dylan.meszaros.data.AppDatabase
import com.dylan.meszaros.data.StockRepository
import com.dylan.meszaros.data.StockRepositoryImpl
import com.dylan.meszaros.viewmodel.StockViewModel
import org.koin.dsl.module

val appModules = module {

    single{
        Room.databaseBuilder(get(), AppDatabase::class.java, "stock-database")
            .build()
    }
    single { get<AppDatabase>().stockDao() }
    single<StockRepository> { StockRepositoryImpl(get()) }
    single { StockViewModel(get()) }
}