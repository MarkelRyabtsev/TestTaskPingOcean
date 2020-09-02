package com.markel.testtask.models

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("token")
    var authToken: String
)