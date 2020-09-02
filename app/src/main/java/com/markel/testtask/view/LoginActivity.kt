package com.markel.testtask.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.markel.testtask.R
import com.markel.testtask.models.LoginRequest
import com.markel.testtask.models.LoginResponse
import com.markel.testtask.network.ApiClient
import com.markel.testtask.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        editTextEmail = findViewById(R.id.edittext_email_activity_login)
        editTextPassword = findViewById(R.id.edittext_password_activity_login)
        progressBarLoading = findViewById(R.id.progressbar_loading_activity_login)
        buttonLogin = findViewById(R.id.botton_login_activity_login)

        setListeners()
    }

    private fun setListeners(){
        buttonLogin.setOnClickListener{
            progressBarLoading.visibility = View.VISIBLE

            apiClient.getApiService(applicationContext).jsonLogin(LoginRequest(email = editTextEmail.text.toString(), password = editTextPassword.text.toString()))
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        showLoginFailed()
                        progressBarLoading.visibility = View.GONE
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                        val loginResponse = response.body()

                        if (response.code() == 200 && !loginResponse?.authToken.isNullOrEmpty()) {
                            sessionManager.saveAuthToken(loginResponse!!.authToken)
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showLoginFailed()
                        }

                        progressBarLoading.visibility = View.GONE
                    }
                })
        }

        editTextEmail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                setButtonLiginEnabled()
            }

        })

        editTextPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                setButtonLiginEnabled()
            }

        })
    }

    private fun setButtonLiginEnabled(){
        buttonLogin.isEnabled = (isEmailValid(editTextEmail.text.toString())
                && isPasswordValid(editTextPassword.text.toString()))
    }

    private fun showLoginFailed() {
        Toast.makeText(applicationContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
    }

    private fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}