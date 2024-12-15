package com.dylan.dylanmeszaros_comp304_finaltest_f24.data

interface StockRepository {
    fun getStocks(): MutableList<StockInfo>;
    fun getStock(symbol: String): StockInfo?;
    fun addStock(stock: StockInfo): Boolean;
    fun removeStock(stock: StockInfo): Boolean;
}