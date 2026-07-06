package com.example.project_uas.modelData

import com.google.gson.annotations.SerializedName

data class ResponseKonfirmasi(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)
