package com.example.app.data.model.network

import com.google.gson.annotations.SerializedName

data class ArticleResponse (
    @SerializedName("status") var status : String,
    @SerializedName("totalResults") var totalResults : Int,

    @SerializedName("articles") var articles : ArrayList<Articles>? = arrayListOf()
)