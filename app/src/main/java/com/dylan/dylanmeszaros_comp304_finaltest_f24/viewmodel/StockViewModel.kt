package com.dylan.dylanmeszaros_comp304_finaltest_f24.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.dylanmeszaros_comp304_finaltest_f24.data.StockInfo
import com.dylan.dylanmeszaros_comp304_finaltest_f24.data.StockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StockViewModel (
    private var stockRepository: StockRepository
    // No Repository yet
): ViewModel() {

    //private val _stocks = mutableStateListOf<StockInfo>();
    //val stocks: List<StockInfo> get() = _stocks;
    private val _stocks = MutableStateFlow<List<StockInfo>>(emptyList());
    val stocks: StateFlow<List<StockInfo>> = _stocks;

    init {
        _stocks.value = stockRepository.getStocks();
        //_stocks.addAll(stockRepository.getStocks());
        //updateStocks();

        /*CoroutineScope(Dispatchers.IO).launch {
            stockRepository.InitializeRoom()
        }*/

        //fetchStocks();
    }

    private fun fetchStocks(){ // Room
        viewModelScope.launch {
            try {
                _stocks.value = stockRepository.getStocks() // Suspended function
            } catch (e: Exception) {
                Log.e("StockViewModel", "${e}");
            }
        }
    }

    fun insert(stock: StockInfo){
        val successful = stockRepository.addStock(stock);
        //updateStocks();

        if (successful){
            // Room
            //viewModelScope.launch { _stocks.add(stock); }

            // No Room
            viewModelScope.launch { _stocks.emit(_stocks.value + stock); }
        }
    }
    fun delete(stock: StockInfo){
        val successful = stockRepository.removeStock(stock);
        //updateStocks();

        if (successful){
            // Room
            //viewModelScope.launch { _stocks.add(stock); }

            // No Room
            viewModelScope.launch { _stocks.emit(_stocks.value - stock); }
        }
    }
    fun queryAll(): MutableList<StockInfo> {
        // Room
        //updateStocks();

        // No Room
        return stockRepository.getStocks();
    }
    fun queryBySymbol(symbol: String): StockInfo?{
        // Room
        return _stocks.value.find { it.stockSymbol == symbol }

        // No Room
        //return stockRepository.getStock(symbol);
    }

    private fun updateStocks(){
        // Room
        /*viewModelScope.launch {
            try{
                _stocks.value = stockRepository.getStocks();
            } catch (e: Exception){
                Log.e("StockViewModel", "${e}");
            }
        }*/

        // No Room
        _stocks.value = stockRepository.getStocks();
    }
}