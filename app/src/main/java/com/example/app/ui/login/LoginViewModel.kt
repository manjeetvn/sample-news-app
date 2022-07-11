package com.example.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.R
import com.example.app.common.Constant
import com.example.app.data.repository.LoginRepository
import com.example.app.data.Result
import com.example.app.ui.login.model.LoggedInUserView
import com.example.app.ui.login.model.LoginFormState
import com.example.app.ui.login.model.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    // User Login Content State
    private val _loginResult = MutableSharedFlow<LoginResult>(replay = 0)
    fun getLoginSharedFlow(): SharedFlow<LoginResult> = _loginResult

    /*********************************************
     * User Login Api Call
     */
    fun login(username: String, password: String) {
        val loginResultExceptionHandler = CoroutineExceptionHandler { _, _ ->
            _loginResult.tryEmit(LoginResult(error = R.string.login_failed))
        }

        viewModelScope.launch(Dispatchers.IO + loginResultExceptionHandler) {
            try {
                val result = loginRepository.login(username, password)

                if (result is Result.Success) {
                    _loginResult.emit(LoginResult(success = LoggedInUserView(displayName = result.data.full_name)))

                } else {
                    _loginResult.emit(LoginResult(error = R.string.login_failed))
                }
            } catch (e: Exception) {
                _loginResult.emit(LoginResult(error = R.string.login_failed))
            }
        }
    }

    /**************************************************
     * User Input Data Validation
     */
    fun validate(username: String, password: String) : Flow<LoginFormState> = flow {
        // Both Empty No Error
        if(username.isEmpty() && password.isEmpty()) {
            emit(LoginFormState(isDataValid = false))

        } else if (!isUserNameValid(username)) {
            emit(LoginFormState(usernameError = R.string.invalid_username))

        } else if(password.isEmpty()) {
            emit(LoginFormState(isDataValid = false))

        } else if (!isPasswordValid(password)) {
            emit(LoginFormState(passwordError = R.string.invalid_password))

        } else {
            emit(LoginFormState(isDataValid = true))
        }
    }.flowOn(Dispatchers.IO)

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return (username.isNotEmpty() && username.matches(Constant.USER_REGEX.toRegex()))
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.matches(Constant.PASSWORD_REGEX.toRegex())
    }
}