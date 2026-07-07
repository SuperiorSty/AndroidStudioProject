package com.example.project_uas.modelData

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    // --- KELOLA OBAT ---

    @FormUrlEncoded
    @POST("api.php?apicall=insertData")
    suspend fun insertObat(
        @Field("NamaObat") namaObat: String,
        @Field("Dosis") dosis: String,
        @Field("Stok") stok: Int,
        @Field("Catatan") catatan: String
    ): Response<ResponseKonfirmasi>

    @FormUrlEncoded
    @POST("api.php?apicall=loadData")
    suspend fun loadDataObat(
        @Field("NamaObat") query: String = ""
    ): Response<ResponseDataObat>

    @FormUrlEncoded
    @POST("api.php?apicall=updateData")
    suspend fun updateObat(
        @Field("IdObat") id: Int,
        @Field("NamaObat") namaObat: String,
        @Field("Dosis") dosis: String,
        @Field("Stok") stok: Int,
        @Field("Catatan") catatan: String
    ): Response<ResponseKonfirmasi>

    @FormUrlEncoded
    @POST("api.php?apicall=deleteData")
    suspend fun deleteObat(
        @Field("IdObat") id: Int
    ): Response<ResponseKonfirmasi>

    // --- PENGINGAT ---

    @FormUrlEncoded
    @POST("api.php?apicall=insertPengingat")
    suspend fun insertPengingat(
        @Field("ObatId") obatId: Int,
        @Field("WaktuMinum") waktuMinum: String,
        @Field("StatusAktif") statusAktif: Int
    ): Response<ResponseKonfirmasi>

    @FormUrlEncoded
    @POST("api.php?apicall=loadPengingat")
    suspend fun loadDataPengingat(
        @Field("ObatId") obatId: Int
    ): Response<ResponseDataPengingat>

    @FormUrlEncoded
    @POST("api.php?apicall=deletePengingat")
    suspend fun deletePengingat(
        @Field("IdPengingat") idPengingat: Int
    ): Response<ResponseKonfirmasi>

    @FormUrlEncoded
    @POST("api.php?apicall=updatePengingat")
    suspend fun updatePengingat(
        @Field("IdPengingat") idPengingat: Int,
        @Field("WaktuMinum") waktuMinum: String,
        @Field("StatusAktif") statusAktif: Int
    ): Response<ResponseKonfirmasi>
}
