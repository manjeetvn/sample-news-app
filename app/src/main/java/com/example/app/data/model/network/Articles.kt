package com.example.app.data.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Articles (

  @SerializedName("url") var url : String? = null,
  @SerializedName("title") var title : String? = null,
  @SerializedName("author") var author : String? = null,
  @SerializedName("source") var source : Source? = Source(),
  @SerializedName("content") var content : String? = null,
  @SerializedName("urlToImage") var urlToImage : String? = null,
  @SerializedName("description") var description : String? = null,
  @SerializedName("publishedAt") var publishedAt : String? = null

): Parcelable