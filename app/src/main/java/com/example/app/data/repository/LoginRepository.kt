package com.example.app.data.repository

import android.util.Log
import com.example.app.data.Result
import com.example.app.data.api.ApiService
import com.example.app.data.model.network.ArticleResponse
import com.example.app.data.model.network.LogInModel

/**
 * Class that requests data from api and return with some checks
 */
class LoginRepository(private val apiService: ApiService) {

    suspend fun login(username: String, password: String): Result<LogInModel> {
        Log.d("LoginRepository", "User Name : $username, Password : $password")
        try {
            // handle login
            val result = apiService.login()

            if(result.isSuccessful && result.body() != null) {
                return Result.Success(result.body()!!)
            }
        } catch (e : Exception) { }

        return Result.Error(error = "Login Failed..")
    }

    suspend fun getAllArticles(): Result<ArticleResponse> {
        try {
            // handle login
            val result = apiService.allArticles()

            if(result.isSuccessful && result.body() != null) {
                return Result.Success(result.body()!!)
            }
        } catch (e : Exception) { }

        return Result.Error(error = "News List Failed..")
    }
}