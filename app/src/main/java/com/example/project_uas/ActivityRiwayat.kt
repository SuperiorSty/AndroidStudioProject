package com.example.project_uas

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas.accessRetrofit.CRUDDataClass
import com.example.project_uas.accessRetrofit.RetrofitClient
import com.example.project_uas.adapterRecyclerView.AdapterRiwayat
import com.example.project_uas.modelData.Obat
import com.example.project_uas.modelData.Pengingat
import com.example.project_uas.modelData.RiwayatMinum

class ActivityRiwayat : AppCompatActivity(), RetrofitClient.RetrofitCallback {

    private lateinit var rvRiwayat: RecyclerView
    private lateinit var btnKembali: Button
    private lateinit var crudData: CRUDDataClass
    private lateinit var riwayatAdapter: AdapterRiwayat
    private var listRiwayat = ArrayList<RiwayatMinum>()
    private var obatId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat)

        obatId = intent.getIntExtra("OBAT_ID", -1)

        rvRiwayat = findViewById(R.id.recyclerViewRiwayat)
        btnKembali = findViewById(R.id.buttonKembaliRiwayat)

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        riwayatAdapter = AdapterRiwayat(listRiwayat)
        rvRiwayat.layoutManager = LinearLayoutManager(this)
        rvRiwayat.adapter = riwayatAdapter

        crudData = CRUDDataClass(this, this)

        crudData.getAllRiwayat()

        btnKembali.setOnClickListener {
            finish()
        }
    }

    override fun onObatLoaded(data: List<Obat>) {}
    override fun onPengingatLoaded(data: List<Pengingat>) {}

    override fun onRiwayatLoaded(data: List<RiwayatMinum>) {
        runOnUiThread {
            listRiwayat.clear()
            listRiwayat.addAll(data)
            riwayatAdapter.updateData(listRiwayat)
        }
    }
}
