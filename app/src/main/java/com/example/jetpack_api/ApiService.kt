package com.example.jetpack_api

import retrofit2.http.GET

data class SmartWatch(
    val id : Int,
    val name : String,
    val price : Double,
    val battery_life : String,
    val in_stock : Boolean,
    val image_url : String
)

interface ApiService {
    @GET("/WatchAPI/readAll.php")
    fun getObjects(): retrofit2.Call<List<SmartWatch>>
}