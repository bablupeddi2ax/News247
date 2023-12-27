package com.example.newsapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.NewsRepository
import com.example.newsapp.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private var currentPage = 1
    private val pageSize = 20
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _topHeadlines = MutableLiveData<List<Article>>()
    val topHeadlines: LiveData<List<Article>> get() = _topHeadlines

    private val _newsByCategory = MutableLiveData<List<Article>>()
    val newsByCategory:LiveData<List<Article>> get() = _newsByCategory

    private val _searchResults = MutableLiveData<List<Article>>()
    val searchResults:LiveData<List<Article>> get() = _searchResults

    fun fetchTopHeadlines() {
        _isLoading.postValue(true)

        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getTopHeadlines(currentPage, pageSize)
            response?.let { articles ->
                _topHeadlines.postValue(articles.articles)
                currentPage++
            }

            _isLoading.postValue(false)
        }
    }
    fun fetchNewsByCategory(cat: String) {
        currentPage=1
        _isLoading.postValue(true)

        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCategoryNews(cat, currentPage, pageSize)
            response?.let { articles ->
                _newsByCategory.postValue(articles.articles)
                currentPage++
            }

            _isLoading.postValue(false)
        }
    }
    fun searchNews(query:String){
        currentPage=1
        _isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.searchNews(query,currentPage,pageSize)
            response?.let { articles->_searchResults.postValue(articles.articles)
                Log.i("Asearcgresults", articles.articles.toString())}
        }
        _isLoading.postValue(false)
    }

}
