package com.dylan.meszaros.data

interface StockRepository {
    fun InitializeRoom()
    fun getStocks(): MutableList<StockInfo>;
    fun getStock(symbol: String): StockInfo?;
    fun addStock(stock: StockInfo): Boolean;
    fun removeStock(stock: StockInfo): Boolean;
}