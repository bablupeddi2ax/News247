package com.example.newsapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.newsapp.R

// In DetailsActivity.kt

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Retrieve data from intent
        val author = intent.getStringExtra("author")
        val content = intent.getStringExtra("content")
        val description = intent.getStringExtra("description")
        val url = intent.getStringExtra("url")
        val urlToImage = intent.getStringExtra("urlToImage")

        // Populate UI elements with data
        // Example: Set text to TextView
        findViewById<TextView>(R.id.authorTextView).text = author
        findViewById<TextView>(R.id.contentTextView).text =modifiedContent(content)

        findViewById<TextView>(R.id.urlTextView).text="View more"
        findViewById<TextView>(R.id.descriptionTextView).text = description
        findViewById<TextView>(R.id.urlTextView).setOnClickListener {
            val url = intent.getStringExtra("url")
            openUrlInBrowser(url)
        }
        // Load image using Glide into ImageView
        val imageView: ImageView = findViewById(R.id.imageView)
        imageView.loadImage(urlToImage)



    }

    private fun modifiedContent(content: String?): String {
        if (content.isNullOrEmpty()) {
            return ""
        }

        val pattern = "\\[\\+\\d+ chars\\]".toRegex()

        // Replace the matched pattern with an empty string
        val modifiedContent = content.replace(pattern, "")

        return modifiedContent.trim()
    }


    fun openUrlInBrowser(url: String?) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        Log.i("insideopen","sldhf")
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background) // Optional: Placeholder image
            .error(com.google.android.material.R.drawable.mtrl_ic_error) // Optional: Error image
            .into(this)
    }
}
