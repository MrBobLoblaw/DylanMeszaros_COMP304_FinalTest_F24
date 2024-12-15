package com.dylan.dylanmeszaros_comp304_finaltest_f24.di

import android.app.Application
import androidx.room.Room
import com.dylan.dylanmeszaros_comp304_finaltest_f24.MainActivity
import com.dylan.dylanmeszaros_comp304_finaltest_f24.data.AppDatabase
import com.dylan.dylanmeszaros_comp304_finaltest_f24.data.StockRepository
import com.dylan.dylanmeszaros_comp304_finaltest_f24.data.StockRepositoryImpl
import com.dylan.dylanmeszaros_comp304_finaltest_f24.viewmodel.StockViewModel
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