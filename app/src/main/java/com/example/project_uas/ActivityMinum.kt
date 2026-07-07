package com.example.project_uas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_uas.accessRetrofit.CRUDDataClass
import com.example.project_uas.accessRetrofit.RetrofitClient
import com.example.project_uas.modelData.Obat
import com.example.project_uas.modelData.Pengingat
import com.example.project_uas.modelData.RiwayatMinum
import com.example.project_uas.modelData.ResponseKonfirmasi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityMinum : AppCompatActivity() {

    private lateinit var tvNamaObat: TextView
    private lateinit var tvDosis: TextView
    private lateinit var btnCatatMinum: Button
    private lateinit var btnKembali: Button
    private lateinit var crudData: CRUDDataClass

    private var obatId: Int = -1
    private var obatNama: String = ""
    private var obatDosis: String = ""
    private var obatStok: Int = 0
    private var obatCatatan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_minum)

        obatId = intent.getIntExtra("OBAT_ID", -1)
        obatNama = intent.getStringExtra("OBAT_NAMA") ?: "Obat"
        obatDosis = intent.getStringExtra("OBAT_DOSIS") ?: "-"
        obatStok = intent.getIntExtra("OBAT_STOK", 0)
        obatCatatan = intent.getStringExtra("OBAT_CATATAN") ?: ""

        crudData = CRUDDataClass(this, object : RetrofitClient.RetrofitCallback {
            override fun onObatLoaded(data: List<Obat>) {}
            override fun onPengingatLoaded(data: List<Pengingat>) {}
            override fun onRiwayatLoaded(data: List<RiwayatMinum>) {}
        })

        tvNamaObat = findViewById(R.id.textViewNamaObatMinum)
        tvDosis = findViewById(R.id.textViewDosisMinum)
        btnCatatMinum = findViewById(R.id.buttonCatatMinum)
        btnKembali = findViewById(R.id.buttonKembaliMinum)

        tvNamaObat.text = obatNama
        tvDosis.text = "Dosis: $obatDosis"

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        btnCatatMinum.setOnClickListener {
            val waktuMinum = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            crudData.saveRiwayat(obatId, obatNama, obatDosis, waktuMinum)

            if (obatStok > 0) {
                val updatedObat = Obat(obatId, obatNama, obatDosis, obatStok - 1, obatCatatan)
                crudData.updateObat(updatedObat)
            }

            val intent = Intent(this, ActivityRiwayat::class.java)
            startActivity(intent)
            finish()
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }
}
