package com.example.project_uas.adapterRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas.R
import com.example.project_uas.modelData.Pengingat

class AdapterReminder(
    private var listPengingat: List<Pengingat>,
    private val listener: OnReminderClickListener
) : RecyclerView.Adapter<AdapterReminder.ReminderViewHolder>() {

    interface OnReminderClickListener {
        fun onEdit(pengingat: Pengingat)
        fun onDelete(pengingat: Pengingat)
        fun onSwitch(pengingat: Pengingat, isActive: Boolean)
    }

    inner class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWaktu: TextView = itemView.findViewById(R.id.textViewWaktuMinum)
        val tvStatus: TextView = itemView.findViewById(R.id.textViewStatusAktif)
        val swReminder: Switch = itemView.findViewById(R.id.switchReminder)
        val btnEdit: Button = itemView.findViewById(R.id.buttonEditReminder)
        val btnDelete: Button = itemView.findViewById(R.id.buttonDeleteReminder)

        fun bind(pengingat: Pengingat) {
            tvWaktu.text = pengingat.waktu_minum
            tvStatus.text = if (pengingat.status_aktif == 1) "Status: Aktif" else "Status: Tidak Aktif"
            swReminder.isChecked = pengingat.status_aktif == 1

            btnEdit.setOnClickListener { listener.onEdit(pengingat) }
            btnDelete.setOnClickListener { listener.onDelete(pengingat) }
            swReminder.setOnCheckedChangeListener { _, isChecked -> 
                listener.onSwitch(pengingat, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listpengingat, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(listPengingat[position])
    }

    override fun getItemCount(): Int = listPengingat.size

    fun updateData(newList: List<Pengingat>) {
        listPengingat = newList
        notifyDataSetChanged()
    }
}
