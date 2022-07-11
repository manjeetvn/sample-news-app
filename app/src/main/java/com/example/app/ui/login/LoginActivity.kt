package com.example.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.app.R
import com.example.app.common.IntentConstant
import com.example.app.databinding.ActivityLoginBinding
import com.example.app.ui.home.HomeActivity
import com.example.app.ui.login.model.LoggedInUserView
import com.example.app.ui.login.model.LoginFormState
import com.example.app.ui.login.model.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initViews()
        setupObservers()
    }

    private fun initViews() {

        // User Name Field Change
        binding.username.doOnTextChanged { text, _, _, _ ->
            // Remove Error
            binding.tlUsername.error = ""

            // Username & Password Validate
            validate(text.toString(), binding.password.text.toString())
        }

        // Password Text Change & Submit Action
        binding.password.apply {

            doOnTextChanged { text, _, _, _ ->
                // Remove Error
                binding.tlPassword.error = ""

                // Username & Password Validate
                validate(binding.username.text.toString(), text.toString())
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        processLogin(
                            binding.username.text.toString(), binding.password.text.toString()
                        )
                    }
                }
                false
            }
        }

        binding.login.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            processLogin(binding.username.text.toString(), binding.password.text.toString())
        }
    }

    /***
     * Validate Username & Password - Trigger
     */
    private fun validate(username: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            loginViewModel.validate(username, password).collect {
                handleValidateResult(it)
            }
        }
    }

    /***
     * Result of Input Validation
     */
    private fun handleValidateResult(loginFormState: LoginFormState?) {
        val loginState = loginFormState ?: return

        // disable login button unless both username / password is valid
        binding.login.isEnabled = loginState.isDataValid

        if (loginState.usernameError != null && loginState.usernameError != 0) {
            binding.tlUsername.error = getString(loginState.usernameError)
        }

        if (loginState.passwordError != null && loginState.passwordError != 0) {
            binding.tlPassword.error = getString(loginState.passwordError)
        }
    }

    /***
     * User Login
     */
    private fun processLogin(username: String, password: String) {
        loginViewModel.login(username, password)
    }

    /********************
     *Start a coroutine in the lifecycle scope
     *repeatOnLifecycle launches lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
     */
    private fun setupObservers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Collect for Content List
                launch {
                    loginViewModel.getLoginSharedFlow().collect { loginResult ->
                        handleLoginResult(loginResult)
                    }
                }
            }
        }
    }

     /***
     * Login Result
     */
    private fun handleLoginResult(loginResult: LoginResult) {
        binding.loading.visibility = View.GONE

        if (loginResult.error != null) { showLoginFailed(loginResult.error) }
        else if (loginResult.success != null) { updateUiWithUser(loginResult.success) }
    }

    /**********************************
    * Start Main Activity On Login
    */
    private fun updateUiWithUser(model: LoggedInUserView) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(IntentConstant.USER_NAME, model.displayName)

        startActivity(intent)
        finish()
    }

    /***
    * Failed Login*/
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}