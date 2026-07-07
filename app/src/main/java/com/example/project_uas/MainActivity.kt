package com.example.project_uas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas.accessRetrofit.CRUDDataClass
import com.example.project_uas.accessRetrofit.RetrofitClient
import com.example.project_uas.adapterRecyclerView.AdapterRecycle
import com.example.project_uas.modelData.Obat
import com.example.project_uas.modelData.Pengingat
import com.example.project_uas.modelData.RiwayatMinum

class MainActivity : AppCompatActivity(),
    RetrofitClient.RetrofitCallback,
    AdapterRecycle.OnObatClickListener {

    private lateinit var etNama: EditText
    private lateinit var etDosis: EditText
    private lateinit var etStok: EditText
    private lateinit var etCatatan: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnRiwayat: Button
    private lateinit var rvObat: RecyclerView

    private lateinit var crudData: CRUDDataClass
    private lateinit var obatAdapter: AdapterRecycle
    private var listObat = ArrayList<Obat>()

    private var mode = "Insert"
    private var selectedObatId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inisialisasi View
        etNama = findViewById(R.id.editTextNamaObat)
        etDosis = findViewById(R.id.editTextDosis)
        etStok = findViewById(R.id.editTextStok)
        etCatatan = findViewById(R.id.editTextCatatan)
        btnSimpan = findViewById(R.id.buttonSimpanObat)
        btnRiwayat = findViewById(R.id.buttonRiwayat)
        rvObat = findViewById(R.id.recyclerViewObat)

        // Penanganan System Bars (Edge to Edge)
        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Setup RecyclerView
        obatAdapter = AdapterRecycle(listObat, this)
        rvObat.layoutManager = LinearLayoutManager(this)
        rvObat.adapter = obatAdapter

        // Inisialisasi CRUD
        crudData = CRUDDataClass(this, this)

        // Load Data Awal
        crudData.getAllObat()

        // Listener Simpan
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val dosis = etDosis.text.toString()
            val stokStr = etStok.text.toString()
            val stok = if (stokStr.isNotEmpty()) stokStr.toInt() else 0
            val catatan = etCatatan.text.toString()

            if (nama.isEmpty() || dosis.isEmpty()) {
                Toast.makeText(this, "Nama dan Dosis harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mode == "Insert") {
                val newObat = Obat(0, nama, dosis, stok, catatan)
                crudData.saveObat(newObat)
            } else {
                val updatedObat = Obat(selectedObatId, nama, dosis, stok, catatan)
                crudData.updateObat(updatedObat)
            }
            refresh()
        }

        btnRiwayat.setOnClickListener {
            val intent = Intent(this, ActivityRiwayat::class.java)
            startActivity(intent)
        }
    }

    fun refresh() {
        etNama.setText("")
        etDosis.setText("")
        etStok.setText("")
        etCatatan.setText("")
        mode = "Insert"
        selectedObatId = -1
        btnSimpan.text = "Simpan Obat"
    }

    override fun onObatLoaded(data: List<Obat>) {
        runOnUiThread {
            listObat.clear()
            listObat.addAll(data)
            obatAdapter.updateData(listObat)
        }
    }

    override fun onPengingatLoaded(data: List<Pengingat>) {
        // Callback untuk pengingat (bisa dikosongkan jika tidak dipakai di sini)
    }

    override fun onRiwayatLoaded(data: List<RiwayatMinum>) {
        // Callback untuk riwayat (bisa dikosongkan jika tidak dipakai di sini)
    }

    override fun onEdit(obat: Obat) {
        etNama.setText(obat.nama_obat)
        etDosis.setText(obat.dosis)
        etStok.setText(obat.stok_saat_ini.toString())
        etCatatan.setText(obat.catatan)

        mode = "Update"
        selectedObatId = obat.id
        btnSimpan.text = "Update Obat"
    }

    override fun onDelete(obat: Obat) {
        crudData.deleteObat(obat.id)
    }

    override fun onReminder(obat: Obat) {
        // Pastikan ReminderActivity sudah dibuat filenya
        val intent = Intent(this, ReminderActivity::class.java)
        intent.putExtra("OBAT_ID", obat.id)
        intent.putExtra("OBAT_NAMA", obat.nama_obat)
        startActivity(intent)
    }

    override fun onMinum(obat: Obat) {
        val intent = Intent(this, ActivityMinum::class.java)
        intent.putExtra("OBAT_ID", obat.id)
        intent.putExtra("OBAT_NAMA", obat.nama_obat)
        intent.putExtra("OBAT_DOSIS", obat.dosis)
        intent.putExtra("OBAT_STOK", obat.stok_saat_ini)
        intent.putExtra("OBAT_CATATAN", obat.catatan)
        startActivity(intent)
    }
}