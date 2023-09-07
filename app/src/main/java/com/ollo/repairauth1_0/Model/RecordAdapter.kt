package com.ollo.repairauth1_0.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ollo.repairauth1_0.Data.IconsResource
import com.ollo.repairauth1_0.R

class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    private var recordsList: ArrayList<RecordModel> = ArrayList()
    private var onClickEditRecord: ((RecordModel) -> Unit)? = null

    fun addRecord(items: ArrayList<RecordModel>) {
        this.recordsList = items
        notifyDataSetChanged()
    }

    fun setOnClickEditRecord(callback: (RecordModel) -> Unit) {
        this.onClickEditRecord = callback
    }

    inner class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tv_id)
        private var number = view.findViewById<TextView>(R.id.tv_number)
        private var description = view.findViewById<TextView>(R.id.tv_description)
        private var tryToRepair = view.findViewById<TextView>(R.id.tv_try_to_repair)
        private var status = view.findViewById<TextView>(R.id.tv_status)
        var btnEdit = view.findViewById<AppCompatImageButton>(R.id.btn_edit_record)
        val recyclerViewIconsRecord: RecyclerView = view.findViewById(R.id.rv_icons_on_record)
        private var startDatum = view.findViewById<TextView>(R.id.start_date)
        private var checkDateList = view.findViewById<TextView>(R.id.check_date_list)
        fun bindView(record: RecordModel) {
            id.text = record.id
            number.text = record.number
            description.text = record.description
            tryToRepair.text = record.tryToRepair
            status.text = record.status
            startDatum.text = record.startDate.toString()
            checkDateList.text =
                if(record.checkDateList.isEmpty()){
                    "Keine Einträge"
                } else {
                    record.checkDateList.toString()
                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecordViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
    )

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = recordsList[position]
        holder.bindView(record)
        holder.btnEdit.setOnClickListener { onClickEditRecord?.invoke(record) }
        holder.recyclerViewIconsRecord.layoutManager = LinearLayoutManager(
            holder.recyclerViewIconsRecord.context,
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
        holder.recyclerViewIconsRecord.adapter = adapter
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }

}