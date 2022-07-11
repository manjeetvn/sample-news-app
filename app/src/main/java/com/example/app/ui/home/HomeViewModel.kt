package com.example.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.Result
import com.example.app.data.model.network.Articles
import com.example.app.data.repository.LoginRepository
import com.example.app.data.state.ContentUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    // User Login Content State
    private val _articleResult = MutableSharedFlow<ContentUIState<List<Articles>>>(replay = 0)
    fun getArticleSharedFlow(): SharedFlow<ContentUIState<List<Articles>>> = _articleResult


    /*********************************************
     * User Login Api Call
     */
    fun getAllArticle() {
        val loginResultExceptionHandler = CoroutineExceptionHandler { _, _ ->
            // _articleResult.tryEmit(ContentUIState.ErrorUIState(exp.get()))
            _articleResult.tryEmit(ContentUIState.ErrorUIState("Error"))
        }

        viewModelScope.launch(Dispatchers.IO + loginResultExceptionHandler) {

            //Emit for Showing Loader
            _articleResult.emit(ContentUIState.LoadingUIState)

            val result = loginRepository.getAllArticles()

            if (result is Result.Success) {
                if (result.data.status.equals("ok", ignoreCase = true)) {

                    if (!result.data.articles.isNullOrEmpty()) {
                        _articleResult.emit(ContentUIState.SuccessUIState(result.data.articles!!))

                    } else {
                        _articleResult.emit(ContentUIState.EmptyUIState)
                    }
                    return@launch
                }
                _articleResult.emit(ContentUIState.ErrorUIState("Error 2"))
            } else {
                _articleResult.emit(ContentUIState.ErrorUIState("Error 3"))
            }
        }
    }
}