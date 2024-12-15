package com.dylan.meszaros.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StockRepositoryImpl(
    private val stockDAO: StockDAO
): StockRepository {

    var stockRepo: MutableList<StockInfo> = mutableListOf(
        StockInfo("APPL", "ZZY Institute", 10.01),
        StockInfo("IBM", "International Bank Manager", 198.2),
        StockInfo("MSFT", "Mok Son's Future Transformers", 77.13),
        StockInfo("Stock1", "Company1", 1.01),
    );

    override fun InitializeRoom(){
        CoroutineScope(Dispatchers.IO).launch {
            for (stock in stockRepo) {
                stockDAO.insert(stock)
                Log.d("StockDao", "Insert");
            }
        }
    }

    init {

    }


    override fun getStocks(): MutableList<StockInfo> {
        // Room
        /*var stocks: MutableList<StockInfo> = mutableListOf();
        Log.d("StockDao", "Testing Database")
        CoroutineScope(Dispatchers.IO).launch {
            stocks =  stockDAO.getAll();
        }
        if (stocks.isNotEmpty()){
            Log.d("StockDao", "Successfully got list from database")
        }
        return stocks;*/

        // No Room
        return stockRepo;
    }

    override fun getStock(symbol: String): StockInfo? {
        val foundStock = stockRepo.find { it.stockSymbol == symbol }

        if (foundStock != null) {
            Log.d("StockRepository", "Found stock: $foundStock");
            return foundStock;
        } else {
            Log.e("StockRepository", "Stock with ID [$symbol] not found.");
            return null;
        }
    }

    override fun addStock(stock: StockInfo): Boolean {
        val foundStock = stockRepo.find { it.stockSymbol == stock.stockSymbol }

        if (foundStock == null) {
            Log.d("StockRepository", "No ${stock.stockSymbol} found in repo. Adding now.");
            // No Model
            //stockRepo.add(stock);

            // Room
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
            // No Model
            //stockRepo.remove(stock);

            // Room
            //stockDao.delete(stock);
            return true;
        } else {
            Log.e("StockRepository", "${stock.stockSymbol} Doesn't Exist.");
            return false;
        }
    }
}