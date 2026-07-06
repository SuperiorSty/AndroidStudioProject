package com.example.project_uas.modelData

import com.google.gson.annotations.SerializedName

data class ResponseDataObat(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<Obat>?
)

data class ResponseDataPengingat(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<Pengingat>?
)
