package com.example.app.data.api

import com.example.app.data.model.network.*
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface ApiService {

    /********************************
     * User Login
     */
    @GET("0774724810730d4ee184")
    suspend fun login(): Response<LogInModel>

    /********************************
     * All News List
     */
    @GET("7c27fa874f0a4d46e4d4")
    suspend fun allArticles(): Response<ArticleResponse>
}