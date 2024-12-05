package com.example.jetpack_api

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.jetpack_api.ui.theme.Jetpack_APITheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Jetpack_APITheme {
                SmartWatchListScreen()
            }
        }
    }
}

fun navigateToDetailActivity(context: android.content.Context, smartWatch: SmartWatch) {
    val intent = Intent(context, SecondActivity::class.java).apply {
        putExtra("name", smartWatch.name)
        putExtra("price", smartWatch.price)
        putExtra("image_url", smartWatch.image_url)
        putExtra("in_stock", smartWatch.in_stock)
    }
    context.startActivity(intent)
}

@Composable
fun SmartWatchListScreen() {
    val apiService = Retrofit.Builder()
        .baseUrl("https://apiyes.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    var smartWatches by remember { mutableStateOf<List<SmartWatch>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        apiService.getObjects().enqueue(object : Callback<List<SmartWatch>> {
            override fun onResponse(
                call: Call<List<SmartWatch>>,
                response: Response<List<SmartWatch>>
            ) {
                if (response.isSuccessful) {
                    smartWatches = response.body() ?: emptyList()
                } else {
                    errorMessage = "Erreur : ${response.message()}"
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<SmartWatch>>, t: Throwable) {
                errorMessage = "Échec : ${t.localizedMessage}"
                isLoading = false
            }
        })
    }

    if (!errorMessage.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage ?: "Une erreur inconnue s'est produite.")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(smartWatches.size) { index ->
                SmartWatchCard(
                    smartWatch = smartWatches[index],
                    onItemClick = { selectedSmartWatch ->
                        navigateToDetailActivity(context, selectedSmartWatch)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SmartWatchCard(smartWatch: SmartWatch, onItemClick: (SmartWatch) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(smartWatch) }
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = smartWatch.image_url,
                contentDescription = smartWatch.name,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "${smartWatch.name}\n${smartWatch.price} DH",
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }
    }
}
