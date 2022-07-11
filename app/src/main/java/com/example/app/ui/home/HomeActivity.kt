package com.example.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.common.IntentConstant
import com.example.app.data.model.network.Articles
import com.example.app.data.state.ContentUIState
import com.example.app.databinding.ActivityMainBinding
import com.example.app.ui.adapters.ArticlesListAdapter
import com.example.app.ui.detail.NewsDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val articleViewModel: HomeViewModel by viewModels()

    // Article Adapter
    private val articleAdapter: ArticlesListAdapter by lazy { ArticlesListAdapter(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()
        setupObservers()
        setSupportActionBar(binding.toolbar)

        // Api Call on Resume
        lifecycleScope.launchWhenResumed { articleViewModel.getAllArticle()  }
    }

    private fun initViews() {
        // Article Listing LayoutManager
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        intent.extras?.let {
            val username = it.getString(IntentConstant.USER_NAME) ?: ""
            binding.toolbar.title = "${getString(R.string.welcome)}, $username"
        }
    }

    // [All Api Collectors]
    private fun setupObservers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                //Collect for Global Config
                launch {
                    articleViewModel.getArticleSharedFlow().collect { uiState ->
                        setLoadingState(isGone = true)

                        when (uiState) {
                            is ContentUIState.EmptyUIState -> setEmptyState()
                            is ContentUIState.ErrorUIState -> setErrorState(uiState)
                            is ContentUIState.LoadingUIState -> setLoadingState(isGone = false)
                            is ContentUIState.SuccessUIState -> setListingContent(uiState.content)
                        }
                    }
                }
            }
        }
    }

    // [Set Visibility State Between Views]
    private fun setViewVisible(isContent: Boolean, isNoContent: Boolean, isError: Boolean) {

        binding.recyclerView.isVisible = isContent
        binding.textMessage.isVisible  = isNoContent || isError
    }

    // [Loaders]
    private fun setLoadingState(isGone: Boolean = true) {
        binding.clLoading.isGone = isGone
    }

    // [Set No Content Image According to Type]
    private fun setEmptyState() {
        setViewVisible(isContent = false, isNoContent = true, isError = false)
    }

    // [Listing Error State]
    private fun setErrorState(uiState: ContentUIState.ErrorUIState) {
        setViewVisible(isContent = false, isNoContent = false, isError = true)
        Toast.makeText(this, uiState.msg, Toast.LENGTH_SHORT).show()
    }

    // [Listing Content]
    private fun setListingContent(content: List<Articles>) {
        setViewVisible(isContent = true, isNoContent = false, isError = false)

        articleAdapter.setItemClickListener(object : ArticlesListAdapter.OnItemClickListener {
            override fun onItemClicked(data: Articles, position: Int) {
                openNewsDetailScreen(data)
            }
        })

        // Vessel Data Adapter Set
        binding.recyclerView.adapter = articleAdapter

        // Submit New List
        articleAdapter.updateList(content)
    }

    // [Navigate to Next Screen]
    private fun openNewsDetailScreen(data: Articles) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(IntentConstant.NEWS_DATA, data)

        startActivity(intent)
    }
}