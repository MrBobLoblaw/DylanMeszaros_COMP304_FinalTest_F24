package com.dylan.dylanmeszaros_comp304_finaltest_f24.data

import android.util.Log


class StockRepositoryImpl(
    private val stockDAO: StockDAO
): StockRepository {

    var stockRepo: MutableList<StockInfo> = mutableListOf(
        StockInfo("APPL", "ZZY Institute", 10.01),
        StockInfo("IBM", "International Bank Manager", 198.2),
        StockInfo("MSFT", "Mok Son's Future Transformers", 77.13),
        StockInfo("Stock1", "Company1", 1.01),
    );


    override fun getStocks(): MutableList<StockInfo> {
        return stockRepo;
        //return stockDao.getAllStocks();
    }

    override fun getStock(symbol: String): StockInfo? {
        val foundStock = stockRepo.find { it.stockSymbol == symbol }

        if (foundStock != null) {
            Log.d("StockRepository", "Found stock: $foundStock");
            return foundStock;
        } else {
            Log.e("StockRepository", "Stock with ID $symbol not found.");
            return null;
        }
    }

    override fun addStock(stock: StockInfo): Boolean {
        val foundStock = stockRepo.find { it.stockSymbol == stock.stockSymbol }

        if (foundStock == null) {
            Log.d("StockRepository", "No ${stock.stockSymbol} found in repo. Adding now.");
            //stockRepo.add(stock);
            //stockDao.insert(stock);
            return true;
        } else {
            Log.e("StockRepository", "${foundStock.stockSymbol} Already exists.");
            return false;
        }
    }

    override fun removeStock(stock: StockInfo): Boolean {
        val foundStock = getStock(stock.stockSymbol);

        if (foundStock != null) {
            Log.d("StockRepository", "${stock.stockSymbol} found in repo. Removing now.");
            //stockRepo.remove(stock);
            //stockDao.delete(stock);
            return true;
        } else {
            Log.e("StockRepository", "${stock.stockSymbol} Doesn't Exist.");
            return false;
        }
    }
}