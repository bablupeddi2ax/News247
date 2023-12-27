package com.example.newsapp.service

import com.example.newsapp.utils.APIKEY
import com.example.newsapp.model.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines?country=in&apiKey=$APIKEY")
    suspend fun getTopHeadlines(@Query("page") page:Int, @Query("pageSize") pageSize:Int): NewsApiResponse

    @GET("top-headlines?country=in&apiKey=$APIKEY")
    suspend fun getByCategory(
        @Query("category") category: String = "",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsApiResponse

    @GET("everything?apiKey=$APIKEY")
    suspend fun  searchNew(
        @Query("q") query:String,
        @Query("page") page:Int,
        @Query("pageSize") pageSize:Int
    ): NewsApiResponse

}
