package com.example.newsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide


// Import statements...
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.view.DetailsActivity

// Import statements...

class NewsAdapter(private val context: Context, private var articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    // ViewHolder class definition...
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]

        // Populate views with data
        holder.titleTextView.text =   modifyTitle(article.title)

        holder.authorTextView.text = article.author ?: "Anonymous author"
        holder.descriptionTextView.text = article.description ?: "Read More"
        holder.imageView.loadImage(article.urlToImage)

        // Set onClickListener to open DetailsActivity
        holder.itemView.setOnClickListener {
            Toast.makeText(context,"Opening ...", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("author", article.author)
            intent.putExtra("content", article.content)
            intent.putExtra("description", article.description)
            intent.putExtra("url", article.url)
            intent.putExtra("urlToImage", article.urlToImage)

            context.startActivity(intent)
        }
    }

    private fun modifyTitle(title: String): String {
        if (title.length <= 45) {
            return title
        }
        val words = title.split(" ")
        var modifiedTitle = ""
        var currentLength = 0
        for (word in words) {
            if (modifiedTitle.length + word.length + 1 <= 45) {
                modifiedTitle += "$word "
                currentLength += word.length + 1
            } else {
                break
            }
        }
        if(modifiedTitle.trim().endsWith(",") || modifiedTitle.trim().endsWith(".") || modifiedTitle.trim().endsWith("?")
            || modifiedTitle.trim().endsWith('"') || modifiedTitle.trim().endsWith("'") || modifiedTitle.trim().endsWith("-") || modifiedTitle.trim().endsWith(";")|| modifiedTitle.trim().endsWith(":")){
            modifiedTitle = modifiedTitle.substring(0,modifiedTitle.length-2)
        }
        return modifiedTitle.trim().plus("...")
    }

    override fun getItemCount(): Int {
        return articles.size
    }




    private fun ImageView.loadImage(url: String?) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background) // Optional: Placeholder image
            .error(com.google.android.material.R.drawable.mtrl_ic_error) // Optional: Error image
            .into(this)
    }

    // UpdateData method...
    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
