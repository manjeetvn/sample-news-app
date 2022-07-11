package com.example.app.data.model.network

import com.google.gson.annotations.SerializedName

/**
 * Data class that captures user information for logged in users retrieved from api
 */
data class LogInModel(
        @SerializedName("token") val token: String,
        @SerializedName("full_name") val full_name: String,
)