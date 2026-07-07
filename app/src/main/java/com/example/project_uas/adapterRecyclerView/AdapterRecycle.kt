package com.example.project_uas.adapterRecyclerView

import com.example.project_uas.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas.modelData.Obat

class AdapterRecycle(
    private var listObat: List<Obat>,
    private val listener: OnObatClickListener
) : RecyclerView.Adapter<AdapterRecycle.ObatViewHolder>() {

    interface OnObatClickListener {
        fun onEdit(obat: Obat)
        fun onDelete(obat: Obat)
        fun onReminder(obat: Obat)
        fun onMinum(obat: Obat)
    }

    inner class ObatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.textViewNamaObat)
        val tvDosis: TextView = itemView.findViewById(R.id.textViewDosis)
        val tvStok: TextView = itemView.findViewById(R.id.textViewStok)
        val tvCatatan: TextView = itemView.findViewById(R.id.textViewCatatan)
        val btnEdit: Button = itemView.findViewById(R.id.buttonEditObat)
        val btnDelete: Button = itemView.findViewById(R.id.buttonDeleteObat)
        val btnReminder: Button = itemView.findViewById(R.id.buttonReminderObat)
        val btnMinum: Button = itemView.findViewById(R.id.buttonMinumObat)

        fun bind(obat: Obat) {
            tvNama.text = obat.nama_obat
            tvDosis.text = "Dosis: ${obat.dosis}"
            tvStok.text = "Stok: ${obat.stok_saat_ini}"
            tvCatatan.text = "Catatan: ${obat.catatan}"

            btnEdit.setOnClickListener { listener.onEdit(obat) }
            btnDelete.setOnClickListener { listener.onDelete(obat) }
            btnReminder.setOnClickListener { listener.onReminder(obat) }
            btnMinum.setOnClickListener { listener.onMinum(obat) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listdataobat, parent, false)
        return ObatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ObatViewHolder,
        position: Int
    ) {
        holder.bind(listObat[position])
    }

    override fun getItemCount(): Int = listObat.size

    fun updateData(newList: List<Obat>) {
        listObat = newList
        notifyDataSetChanged()
    }
}
