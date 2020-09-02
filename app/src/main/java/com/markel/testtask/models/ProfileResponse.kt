package com.markel.testtask.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse (

    @SerializedName("name")
    var name: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("avatar")
    var avatar: String,

    @SerializedName("position")
    var position: String,

    @SerializedName("company_name")
    var companyName: String
)
