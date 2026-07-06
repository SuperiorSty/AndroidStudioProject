package com.example.project_uas.accessRetrofit

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_uas.MainActivity
import com.example.project_uas.modelData.Obat
import com.example.project_uas.modelData.Pengingat
import kotlinx.coroutines.launch

class CRUDDataClass (
    private val activity: ComponentActivity,
    private val callback : RetrofitClient.RetrofitCallback
) {
    // --- CRUD OBAT ---

    fun getAllObat(query: String = ""){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.loadDataObat(query)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        callback.onObatLoaded(responseBody.data ?: emptyList())
                    } else {
                        callback.onObatLoaded(emptyList())
                    }
                } else {
                    callback.onObatLoaded(emptyList())
                }
            } catch (e: Exception) {
                Log.e("RetrofitError", "GetAllObat: ${e.message}")
                callback.onObatLoaded(emptyList())
            }
        }
    }

    fun saveObat(obat: Obat){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.insertObat(
                    obat.nama_obat, obat.dosis, obat.stok_saat_ini, obat.catatan
                )
                if (response.isSuccessful){
                    response.body()?.let { res ->
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                        if (!res.error) {
                            getAllObat()
                            (activity as? MainActivity)?.refresh()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ErrorSave", e.message.toString())
            }
        }
    }

    fun updateObat(obat: Obat){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.updateObat(
                    obat.id, obat.nama_obat, obat.dosis, obat.stok_saat_ini, obat.catatan
                )
                if (response.isSuccessful){
                    response.body()?.let { res ->
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                        if (!res.error) {
                            getAllObat()
                            (activity as? MainActivity)?.refresh()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("UpdateError", e.message.toString())
            }
        }
    }

    fun deleteObat(id: Int){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.deleteObat(id)
                if (response.isSuccessful){
                    response.body()?.let { res ->
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                        if (!res.error) getAllObat()
                    }
                }
            } catch (e: Exception) {
                Log.e("DeleteError", e.message.toString())
            }
        }
    }

    // --- CRUD PENGINGAT ---

    fun getAllPengingat(obatId: Int){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.loadDataPengingat(obatId)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        // Karena PHP tidak memfilter berdasarkan ID, 
                        // kita filter di sisi client agar hanya muncul pengingat untuk obat ini saja.
                        val allData = responseBody.data ?: emptyList()
                        val filteredData = allData.filter { it.obat_id == obatId }
                        callback.onPengingatLoaded(filteredData)
                    } else {
                        callback.onPengingatLoaded(emptyList())
                    }
                }
            } catch (e: Exception) {
                Log.e("RetrofitError", "GetAllPengingat: ${e.message}")
                callback.onPengingatLoaded(emptyList())
            }
        }
    }

    fun savePengingat(pengingat: Pengingat){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.insertPengingat(
                    pengingat.obat_id, pengingat.waktu_minum, pengingat.status_aktif
                )
                if (response.isSuccessful){
                    response.body()?.let { res ->
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                        if (!res.error) getAllPengingat(pengingat.obat_id)
                    }
                }
            } catch (e: Exception) {
                Log.e("ErrorSave", e.message.toString())
            }
        }
    }
}
