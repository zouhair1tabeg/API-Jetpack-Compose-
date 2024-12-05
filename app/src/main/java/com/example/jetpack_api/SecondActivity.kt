package com.example.jetpack_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jetpack_api.ui.theme.Jetpack_APITheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: ""
        val price = intent.getDoubleExtra("price", 0.0)
        val imageUrl = intent.getStringExtra("image_url") ?: ""
        val inStock = intent.getBooleanExtra("in_stock", false)

        setContent {
            Jetpack_APITheme {
                SmartWatchDetailScreen(
                    name = name,
                    price = price,
                    imageUrl = imageUrl,
                    inStock = inStock
                )
            }
        }
    }
}

@Composable
fun SmartWatchDetailScreen(
    name: String,
    price: Double,
    imageUrl: String,
    inStock: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = name)

        Text(
            text = "$price Dh",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        var isSwitchChecked by remember { mutableStateOf(inStock) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (isSwitchChecked) "En stock" else "Rupture de stock")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isSwitchChecked,
                onCheckedChange = { isSwitchChecked = it },
                enabled = false
            )
        }
    }
}