package com.example.project_uas.accessRetrofit

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_uas.MainActivity
import com.example.project_uas.helper.AlarmHelper
import com.example.project_uas.modelData.Obat
import com.example.project_uas.modelData.Pengingat
import com.example.project_uas.modelData.RiwayatMinum
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
                        if (!res.error) {
                            getAllPengingat(pengingat.obat_id)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ErrorSave", e.message.toString())
            }
        }
    }

    fun updatePengingat(pengingat: Pengingat){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.updatePengingat(
                    pengingat.id, pengingat.obat_id, pengingat.waktu_minum, pengingat.status_aktif
                )
                if (response.isSuccessful){
                    response.body()?.let { res ->
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                        if (!res.error) {
                            // Sinkronisasi alarm (set jika aktif, cancel jika non-aktif)
                            if (pengingat.status_aktif == 1) {
                                AlarmHelper.setAlarm(activity, pengingat)
                            } else {
                                AlarmHelper.cancelAlarm(activity, pengingat.id)
                            }
                            getAllPengingat(pengingat.obat_id)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("UpdateError", e.message.toString())
            }
        }
    }

    fun deletePengingat(idPengingat: Int, obatId: Int){
        activity.lifecycleScope.launch {
            try {
                AlarmHelper.cancelAlarm(activity, idPengingat)
                val response = RetrofitClient.api.deletePengingat(idPengingat)
                if (response.isSuccessful){
                    response.body()?.let { res ->
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                        if (!res.error) getAllPengingat(obatId)
                    }
                }
            } catch (e: Exception) {
                Log.e("DeleteError", e.message.toString())
            }
        }
    }

    // --- CRUD RIWAYAT MINUM ---

    fun getAllRiwayat(){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.loadDataRiwayat()
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        callback.onRiwayatLoaded(responseBody.data ?: emptyList())
                    } else {
                        callback.onRiwayatLoaded(emptyList())
                    }
                }
            } catch (e: Exception) {
                Log.e("RetrofitError", "GetAllRiwayat: ${e.message}")
                callback.onRiwayatLoaded(emptyList())
            }
        }
    }

    fun saveRiwayat(obatId: Int, namaObat: String, dosis: String, waktuMinum: String){
        activity.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.insertRiwayat(
                    obatId, namaObat, dosis, waktuMinum
                )
                if (response.isSuccessful){
                    response.body()?.let { res ->
                        Toast.makeText(activity, res.message, Toast.LENGTH_LONG).show()
                        if (!res.error) getAllRiwayat()
                    }
                }
            } catch (e: Exception) {
                Log.e("ErrorSaveRiwayat", e.message.toString())
            }
        }
    }
}
