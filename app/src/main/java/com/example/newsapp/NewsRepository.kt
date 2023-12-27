package com.example.newsapp

import com.example.newsapp.model.NewsApiResponse
import com.example.newsapp.service.ApiService

class NewsRepository(private val apiService: ApiService) {


    suspend fun getTopHeadlines(page:Int,pageSize:Int): NewsApiResponse?{
        return try{
            apiService.getTopHeadlines(page, pageSize  )
        }catch (e:Exception){
            null
        }
    }
    suspend fun getCategoryNews(cat: String, page: Int, pageSize: Int): NewsApiResponse? {
        return try {
            apiService.getByCategory(cat, page, pageSize)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun searchNews(query:String, page:Int, pageSize:Int): NewsApiResponse?{
        return try{
            apiService.searchNew(query,page,pageSize)
        }catch (e:Exception){
            null
        }
    }
}
