package com.example.project_uas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas.accessRetrofit.CRUDDataClass
import com.example.project_uas.accessRetrofit.RetrofitClient
import com.example.project_uas.adapterRecyclerView.AdapterReminder
import com.example.project_uas.helper.AlarmHelper
import com.example.project_uas.modelData.Obat
import com.example.project_uas.modelData.Pengingat

class ReminderActivity : AppCompatActivity(), 
    RetrofitClient.RetrofitCallback, 
    AdapterReminder.OnReminderClickListener {

    private lateinit var tvMedicineName: TextView
    private lateinit var etWaktu: EditText
    private lateinit var btnSimpan: Button
    private lateinit var rvReminder: RecyclerView

    private lateinit var crudData: CRUDDataClass
    private lateinit var reminderAdapter: AdapterReminder
    private var listReminder = ArrayList<Pengingat>()

    private var selectedObatId: Int = -1
    private var selectedMedicineName: String = ""
    
    private var mode = "Insert"
    private var selectedReminderId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reminder)

        selectedObatId = intent.getIntExtra("OBAT_ID", -1)
        selectedMedicineName = intent.getStringExtra("OBAT_NAMA") ?: "Obat"

        tvMedicineName = findViewById(R.id.textViewMedicineName)
        etWaktu = findViewById(R.id.editTextWaktu)
        btnSimpan = findViewById(R.id.buttonSimpanReminder)
        rvReminder = findViewById(R.id.recyclerViewReminder)

        tvMedicineName.text = "Pengingat: $selectedMedicineName"

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        reminderAdapter = AdapterReminder(listReminder, this)
        rvReminder.layoutManager = LinearLayoutManager(this)
        rvReminder.adapter = reminderAdapter

        crudData = CRUDDataClass(this, this)
        
        if (selectedObatId != -1) {
            crudData.getAllPengingat(selectedObatId)
        }

        btnSimpan.setOnClickListener {
            val waktu = etWaktu.text.toString()
            if (waktu.isEmpty()) {
                Toast.makeText(this, "Waktu harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mode == "Insert") {
                val newReminder = Pengingat(0, selectedObatId, selectedMedicineName, waktu, 1)
                crudData.savePengingat(newReminder)
            } else {
                val updatedReminder = Pengingat(selectedReminderId, selectedObatId, selectedMedicineName, waktu, 1)
                crudData.updatePengingat(updatedReminder)
            }
            resetForm()
        }
    }

    private fun resetForm() {
        etWaktu.setText("")
        mode = "Insert"
        selectedReminderId = -1
        btnSimpan.text = "Simpan Pengingat"
    }

    override fun onObatLoaded(data: List<Obat>) {}

    override fun onPengingatLoaded(data: List<Pengingat>) {
        runOnUiThread {
            listReminder.clear()
            listReminder.addAll(data)
            reminderAdapter.updateData(listReminder)
            
            // Mengaktifkan kembali jadwal alarm di HP berdasarkan data terbaru
            AlarmHelper.syncAlarms(this, data)
        }
    }

    override fun onEdit(pengingat: Pengingat) {
        etWaktu.setText(pengingat.waktu_minum)
        mode = "Update"
        selectedReminderId = pengingat.id
        btnSimpan.text = "Update Waktu"
        etWaktu.requestFocus()
    }

    override fun onDelete(pengingat: Pengingat) {
        crudData.deletePengingat(pengingat.id, selectedObatId)
    }

    override fun onSwitch(pengingat: Pengingat, isActive: Boolean) {
        val updatedReminder = Pengingat(
            pengingat.id, 
            pengingat.obat_id, 
            pengingat.nama_obat, 
            pengingat.waktu_minum, 
            if (isActive) 1 else 0
        )
        crudData.updatePengingat(updatedReminder)
    }
}
