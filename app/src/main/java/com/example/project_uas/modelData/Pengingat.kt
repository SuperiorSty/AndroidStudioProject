package com.example.project_uas.modelData

import com.google.gson.annotations.SerializedName

data class Pengingat(
    @SerializedName("id_pengingat")
    val id: Int,
    @SerializedName("obat_id")
    val obat_id: Int,
    @SerializedName("nama_obat")
    val nama_obat: String? = null,
    @SerializedName("waktu_minum")
    val waktu_minum: String,
    @SerializedName("status_aktif")
    val status_aktif: Int
)
