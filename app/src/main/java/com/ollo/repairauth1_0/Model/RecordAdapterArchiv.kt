package com.ollo.repairauth1_0.Model

import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ollo.repairauth1_0.Data.IconsResource
import com.ollo.repairauth1_0.R

class RecordAdapterArchiv : RecyclerView.Adapter<RecordAdapterArchiv.RecordViewHolderArchiv>() {
    private var recordsList: ArrayList<RecordModel> = ArrayList()

    fun addRecord(items: ArrayList<RecordModel>) {
        this.recordsList = items
        notifyDataSetChanged()
    }

    inner class RecordViewHolderArchiv(view: View) : RecyclerView.ViewHolder(view) {
        private var aId = view.findViewById<TextView>(R.id.tva_id)
        private var aNumber = view.findViewById<TextView>(R.id.tva_number)
        private var aStartDate = view.findViewById<TextView>(R.id.tva_start_date)
        private var aCheckDate = view.findViewById<TextView>(R.id.tva_check_date_list)
        private var aDescription = view.findViewById<TextView>(R.id.tva_description)
        private var aTryToRepair = view.findViewById<TextView>(R.id.tva_try_to_repair)
        private var aStatus = view.findViewById<TextView>(R.id.tva_status)
        val aRecyclerViewIconsRecordArchiv: RecyclerView? =
            view.findViewById<RecyclerView>(R.id.tva_icons_on_record)
        private var aEndDate = view.findViewById<TextView>(R.id.tva_end_date)

        fun bindView(record: RecordModel) {
            aId.text = record.id
            aNumber.text = record.number
            aStartDate.text = record.startDate
            aCheckDate.text = record.checkDateList.toString()
            aDescription.text = record.description
            aTryToRepair.text = record.tryToRepair
            aStatus.text = record.status
            aEndDate.text = record.endDate
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecordViewHolderArchiv(
        LayoutInflater.from(parent.context).inflate(R.layout.item_archiv, parent, false)
    )

    override fun onBindViewHolder(holder: RecordViewHolderArchiv, position: Int) {

        val record = recordsList[position]
        holder.bindView(record)
        holder.aRecyclerViewIconsRecordArchiv?.layoutManager = LinearLayoutManager(
            holder.aRecyclerViewIconsRecordArchiv?.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        var iconsData = IconsResource().loadIcons()
        var iconsForRecord: ArrayList<IconModel> = ArrayList()
        for (item in iconsData) {
            if (record.iconsList.contains(item.imageResourceId)) {
                iconsForRecord.add(item)
            }
        }
        val adapter = IconAdapter(iconsForRecord)
        holder.aRecyclerViewIconsRecordArchiv?.adapter = adapter
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }
}



