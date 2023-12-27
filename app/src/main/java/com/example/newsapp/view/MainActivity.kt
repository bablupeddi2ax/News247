package com.example.newsapp.view
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.service.ApiService
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.NewsRepository
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.example.newsapp.R
import com.example.newsapp.utils.RetrofitInstance
import com.example.newsapp.viewmodel.NewsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var progressBar: ProgressBar

    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.articleListView)
        val searchButton = findViewById<ImageButton>(R.id.btnSearch)
        val searchEditText = findViewById<EditText>(R.id.edtSearch)
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                clearRecyclerView()
                viewModel.searchNews(query)
                viewModel.searchResults.observe(this,Observer { articles ->
                    if (articles.isNotEmpty() && !articles.isNullOrEmpty()) {
                        Log.i("SearchResults",articles.toString())
                        adapter.updateData(articles)

                    }

                })
            } else {
Toast.makeText(this@MainActivity,"Type something",Toast.LENGTH_SHORT ).show()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter(this@MainActivity, emptyList()) // Initially, pass an empty list
        recyclerView.adapter = adapter
        val btnEverything = findViewById<Button>(R.id.btnEverything)
        btnEverything.setOnClickListener {
            viewModel.topHeadlines.observe(this, Observer { articles ->
                // Handle the list of articles
                Log.d("NewsActivity", "Articles: $articles")

                // Update the adapter with new data
                adapter.updateData(articles)
            })
        }

        val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
        val repository = NewsRepository(apiService)
        viewModel = ViewModelProvider(this, NewsViewModelFactory(repository))
            .get(NewsViewModel::class.java)
        setupCategoryButtons()
        viewModel.isLoading.observe(this, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.topHeadlines.observe(this, Observer { articles ->
            // Handle the list of articles
            Log.d("NewsActivity", "Articles: $articles")

            // Update the adapter with new data
            adapter.updateData(articles)
        })

        viewModel.fetchTopHeadlines()
    }

    private fun setupCategoryButtons() {
        val categoryButtons = listOf(

            findViewById<Button>(R.id.btnBusiness),
            findViewById<Button>(R.id.btnEntertainment),
            findViewById<Button>(R.id.btnSports),
            findViewById<Button>(R.id.btnHealth),
            findViewById<Button>(R.id.btnTech)
        )

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isLoading = true
                        viewModel.fetchTopHeadlines()
                    }
                }
            }
        })
        categoryButtons.forEach { button ->
            button.setOnClickListener {
                val category = button.text.toString()
                viewModel.fetchNewsByCategory(category)
            }
        }
        viewModel.newsByCategory.observe(this) { articles ->
            articles?.let {
                adapter.updateData(articles)
            } ?: run {
                Log.e("NewsActivity", "Articles or Adapter is null")
            }
        }

    }

    private fun clearRecyclerView() {

        val adapter = recyclerView.adapter as NewsAdapter
        adapter.updateData(emptyList())
    }

}