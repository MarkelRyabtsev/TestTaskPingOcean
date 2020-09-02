package com.markel.testtask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.markel.testtask.R
import com.markel.testtask.models.LoginRequest
import com.markel.testtask.models.LoginResponse
import com.markel.testtask.models.ProfileResponse
import com.markel.testtask.network.ApiClient
import com.markel.testtask.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private lateinit var textViewName: TextView
    private lateinit var textViewPosition: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var imageViewLogout: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        textViewName = findViewById(R.id.textview_name_activity_main)
        textViewPosition = findViewById(R.id.textview_position_activity_main)
        textViewPhone = findViewById(R.id.textview_phone_activity_main)
        textViewEmail = findViewById(R.id.textview_email_activity_main)
        imageViewLogout = findViewById(R.id.imageview_logout_activity_main)

        setListeners()

        apiClient.getApiService(this).fetchProfile()
            .enqueue(object : Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    showErrorMessage()
                }

                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {

                    val profileResponse = response.body()

                    if (response.code() == 200) {
                        textViewName.text = profileResponse?.name
                        textViewPosition.text = profileResponse?.position
                        textViewPhone.text = profileResponse?.phone
                        textViewEmail.text = profileResponse?.email
                    } else {
                        showErrorMessage()
                    }
                }
            })
    }

    private fun setListeners(){

        imageViewLogout.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
    }
}