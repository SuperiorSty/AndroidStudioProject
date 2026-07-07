package com.example.project_uas.modelData

import com.google.gson.annotations.SerializedName

data class RiwayatMinum(
    @SerializedName("id_riwayat")
    val id: Int,
    @SerializedName("obat_id")
    val obat_id: Int,
    @SerializedName("nama_obat")
    val nama_obat: String,
    @SerializedName("dosis")
    val dosis: String,
    @SerializedName("waktu_minum")
    val waktu_minum: String
)
