package com.dylan.dylanmeszaros_comp304_finaltest_f24

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.dylan.dylanmeszaros_comp304_finaltest_f24.data.AppDatabase
import com.dylan.dylanmeszaros_comp304_finaltest_f24.data.StockInfo
import com.dylan.dylanmeszaros_comp304_finaltest_f24.di.appModules
import com.dylan.dylanmeszaros_comp304_finaltest_f24.ui.theme.CoreTheme
import com.dylan.dylanmeszaros_comp304_finaltest_f24.viewmodel.StockViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin


var onStartup = false;

class MainActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (onStartup == false){
            onStartup = true;
            startKoin {
                androidContext(applicationContext);
                modules(appModules);
            }
        }
        enableEdgeToEdge()

        setContentView(R.layout.activity_main);

        //val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database").build();
        //val stockDao = database.stockDao();

        setContent {
            val stockViewModel: StockViewModel = koinViewModel();

            CoreTheme {
                Scaffold(
                    content =  {
                        StockInsertScreen(onInsert = { stockInfo ->
                            //stockDao.insert(stockInfo);
                            stockViewModel.insert(stockInfo);
                        });
                        //Spacer(modifier = Modifier.padding(10.dp));
                        StockListerScreen(onDisplay = { stockInfo ->
                            startActivity(Intent(this@MainActivity, DisplayActivity::class.java).apply {
                                putExtra("stockSymbol", stockInfo.stockSymbol)
                            });
                        }, this);
                    }
                )
            }
        }
    }
}

class DisplayActivity : AppCompatActivity() {

    //val database = Room.databaseBuilder(applicationContext, RoomDatabase::class.java, "stock-database") .build();
    //val stockDao = database.stockDao();
    //val stocks: List<StockInfo> = stockDao.getAll();

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main);

        setContent {
            val stockViewModel: StockViewModel = koinViewModel();
            val stockInfo = intent.getStringExtra("stockSymbol")?.let { stockViewModel.queryBySymbol(it) };
            if (stockInfo == null) {  // Go back
                startActivity(Intent(this@DisplayActivity, MainActivity::class.java))
            }
            else{
                CoreTheme {
                    Scaffold(
                        content = {
                            StockDisplayScreen(onBack = {
                                startActivity(Intent(this@DisplayActivity, MainActivity::class.java));
                            }, stockInfo)
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockInsertScreen(onInsert: (StockInfo) -> Unit){
    var stockSymbol by remember { mutableStateOf(TextFieldValue("")) };
    var companyName by remember { mutableStateOf(TextFieldValue("")) };
    var stockQuote by remember { mutableStateOf(TextFieldValue("")) };

    Column(
        modifier = Modifier
            .padding(16.dp)
            .absolutePadding(top = 10.dp)
    ){
        // Title for Insert Stocks
        Text(
            text = "Insert Stocks",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        CustomTextField(
            title = "Stock Symbol",
            value = stockSymbol ,
            onValueChange = { stockSymbol = it }
        );
        Box(
            modifier = Modifier
                .offset(
                    y = -55.dp
                )
        ){
            CustomTextField(
                title = "Company Name",
                value = companyName,
                onValueChange = { companyName = it }
            );
        }
        Box(
            modifier = Modifier
                .offset(
                    y = -109.dp
                )
        ) {
            CustomTextField(
                title = "Stock Quote",
                value = stockQuote,
                onValueChange = { stockQuote = it }
            );
        }
        Box(
            modifier = Modifier
                .offset(
                    y = -165.dp
                )
        ){
            Button(
                onClick = {
                    if (stockSymbol.text.isNotBlank() && stockQuote.text.toDoubleOrNull() != null && companyName.text.isNotBlank()) {
                        onInsert(
                            StockInfo(
                                stockSymbol = stockSymbol.text,
                                stockQuote = stockQuote.text.toDouble(),
                                companyName = companyName.text
                            )
                        );
                        stockSymbol = TextFieldValue("");
                        companyName = TextFieldValue("");
                        stockQuote = TextFieldValue("");
                    }
                },
                modifier = Modifier
                    .width(130.dp)
                    .padding(vertical = 16.dp)
            ) {
                Text("Insert Stock")
            }
        }
    }
}

@Composable
fun CustomTextField(title: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .offset(
            y = -30.dp
        )
    ) {

        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                color = Color.Black,
            ),
            modifier = Modifier
                .padding(top = 38.dp) // Push the text down to the bottom
                .width(280.dp)
                .height(60.dp)
        );
        Text(
            text = title,
            style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .offset(
                    x = 16.dp,
                    y = -60.dp
                )
        );

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StockListerScreen(onDisplay: (StockInfo) -> Unit, context: Context){
    //val database = Room.databaseBuilder(context, RoomDatabase::class.java, "stock-database").build();
    //val stockDao = database.stockDao();
    //val stocks = stockDao.getAll();
    val stockViewModel: StockViewModel = koinViewModel();
    //val stocks = stockViewModel.stocks;
    val stocks by stockViewModel.stocks.collectAsState();

    var selectedStock by remember { mutableStateOf<StockInfo?>(null) };

    Column(
        modifier = Modifier
            .padding(16.dp)
            .absolutePadding(
                top = 290.dp
            )
    ){
        Text(
            text = "Display Stock Info",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .height(300.dp)
        ) {
            items(stocks) { stock ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedStock = stock }
                        .background(
                            if (selectedStock == stock) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else Color.Transparent
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = stock.stockSymbol,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    );
                }
            }
        }

        Button(
            onClick = {
                selectedStock?.let { onDisplay(it) }
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .width(180.dp)
        ) {
            Text("Display Stock Info");
        }
    }
}


@Composable
fun StockDisplayScreen(onBack: () -> Unit, stockInfo: StockInfo){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .absolutePadding(top = 30.dp)
    ) {
        Text(
            text = "Stock Symbol: ${stockInfo.stockSymbol}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
        Text(
            text = "Company Name: ${stockInfo.companyName}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
        Text(
            text = "Stock Quote: ${stockInfo.stockQuote}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )

        Button(
            onClick = { onBack() },
            modifier = Modifier
                .width(80.dp)
                .padding(vertical = 16.dp)
        ) {
            Text("Back")
        }
    }
}
























