package com.example.app.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.app.common.IntentConstant
import com.example.app.data.model.network.Articles
import com.example.app.databinding.ActivityNewsDetailBinding
import com.example.app.utils.ext.*

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationBar()

        // Init Initial Data
        initNewsData()
    }

    private fun setNavigationBar() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed(); return true
    }

    // [Get News Data & Set]
    private fun initNewsData() {
        val article: Articles? = intent.extras?.getParcelable(IntentConstant.NEWS_DATA)

        // Finish & Return if Null
        if(article == null) {
            Log.e("NewsDetailActivity", "News Object is Null")
            finish(); return
        }

        // Image or Hide
        binding.ivImage.loadImageOrHide(article.urlToImage, relatedView = binding.cardViewImage)

        binding.tvTitle.formatText(article.title)
        binding.tvAuthor.formatTextOrHide(article.author)
        binding.tvDescription.formatHtmlOrHide(article.content)
    }
}