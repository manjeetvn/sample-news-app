package com.example.app.data.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source (
  @SerializedName("id"   ) var id   : String? = null,
  @SerializedName("name" ) var name : String? = null

): Parcelable