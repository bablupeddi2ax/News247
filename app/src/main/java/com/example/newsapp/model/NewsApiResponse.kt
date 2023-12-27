package com.example.newsapp.model

import com.example.newsapp.model.Article

data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)






