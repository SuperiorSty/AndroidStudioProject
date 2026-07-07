package com.example.project_uas.adapterRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas.R
import com.example.project_uas.modelData.RiwayatMinum

class AdapterRiwayat(
    private var listRiwayat: List<RiwayatMinum>
) : RecyclerView.Adapter<AdapterRiwayat.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWaktu: TextView = itemView.findViewById(R.id.textViewWaktuMinum)
        val tvNama: TextView = itemView.findViewById(R.id.textViewNamaObatRiwayat)
        val tvDosis: TextView = itemView.findViewById(R.id.textViewDosisRiwayat)

        fun bind(riwayat: RiwayatMinum) {
            tvWaktu.text = riwayat.waktu_minum
            tvNama.text = riwayat.nama_obat
            tvDosis.text = "Dosis: ${riwayat.dosis}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        holder.bind(listRiwayat[position])
    }

    override fun getItemCount(): Int = listRiwayat.size

    fun updateData(newList: List<RiwayatMinum>) {
        listRiwayat = newList
        notifyDataSetChanged()
    }
}
