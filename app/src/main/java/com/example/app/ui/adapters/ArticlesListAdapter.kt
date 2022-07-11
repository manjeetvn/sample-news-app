package com.example.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.data.model.network.Articles
import com.example.app.databinding.ItemArticleListBinding
import com.example.app.utils.ext.formatText
import com.example.app.utils.ext.loadImageOrHide

class ArticlesListAdapter(private val items: MutableList<Articles> = mutableListOf()) : RecyclerView.Adapter<ArticlesListAdapter.ArticleViewHolder>() {

    private var mOnItemClickLister: OnItemClickListener? = null

    fun setItemClickListener(mOnItemClickLister: OnItemClickListener) {
        this.mOnItemClickLister = mOnItemClickLister
    }

    override fun getItemCount(): Int = this.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val lBinding = ItemArticleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(lBinding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun updateList(content: List<Articles>) {
        this.items.clear()
        this.items.addAll(content)

        notifyDataSetChanged()
    }

    // Inner Base CLass
    open inner class ArticleViewHolder(private val binding: ItemArticleListBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: Articles, position: Int) {

            // Title of News
            binding.tvTitle.formatText(item.title)

            // Description
            binding.tvDesc.formatText(item.description)

            // Image
            binding.ivImage.loadImageOrHide(item.urlToImage)

            // Navigate to Detail Screen on Click Handle
            binding.cardView.setOnClickListener {
                mOnItemClickLister?.onItemClicked(data = item, position = position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(data: Articles, position: Int)
    }
}