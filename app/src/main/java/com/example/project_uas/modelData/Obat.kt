package com.example.project_uas.modelData

import com.google.gson.annotations.SerializedName

data class Obat(
    @SerializedName("id_obat")
    val id: Int,
    @SerializedName("nama_obat")
    val nama_obat: String,
    @SerializedName("dosis")
    val dosis: String,
    @SerializedName("stok_saat_ini")
    val stok_saat_ini: Int,
    @SerializedName("catatan")
    val catatan: String
)
