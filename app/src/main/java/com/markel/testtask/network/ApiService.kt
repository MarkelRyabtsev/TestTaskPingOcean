package com.markel.testtask.network

import com.markel.testtask.models.LoginRequest
import com.markel.testtask.models.LoginResponse
import com.markel.testtask.models.ProfileResponse
import com.markel.testtask.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST(Constants.LOGIN_URL)
    fun jsonLogin(@Body request: LoginRequest): Call<LoginResponse>

    @GET(Constants.PROFILE_URL)
    fun fetchProfile(): Call<ProfileResponse>
}