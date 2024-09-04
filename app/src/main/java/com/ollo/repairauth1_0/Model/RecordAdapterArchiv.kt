package com.ollo.repairauth1_0.Model

import android.animation.LayoutTransition
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
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
        //private var aCheckDate = view.findViewById<TextView>(R.id.tva_check_date_list)
        private var checkDateLastOne = view.findViewById<TextView>(R.id.a_check_date_last_one)
        var layoutLastCheckDate = view.findViewById<LinearLayout>(R.id.a_check_date_layout)
        val recyclerViewDates: RecyclerView = view.findViewById(R.id.a_rv_dates_on_record)
        private var layoutDatesExpand = view.findViewById<LinearLayoutCompat>(R.id.a_layout_dates_expand)

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
            //aCheckDate.text = record.checkDateList.toString()
            var lastDate = "Keine Einträge"
            if (record.checkDateList.isNotEmpty()) {
                val lastIndex = record.checkDateList.size - 1
                lastDate = record.checkDateList[lastIndex]
            }
            checkDateLastOne.text = lastDate
            layoutLastCheckDate.setOnClickListener { expandLayoutWithDates() }
            aDescription.text = record.description
            aTryToRepair.text = record.tryToRepair
            aStatus.text = record.status
            aEndDate.text = record.endDate
        }
        fun expandLayoutWithDates() {
            layoutDatesExpand.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            checkDateLastOne.setOnClickListener {
                val v = if (layoutDatesExpand.visibility == View.GONE) View.VISIBLE else View.GONE
                layoutDatesExpand.visibility = v
            }
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

        // RECYCLERVIEW WITH CHECKDATES
        holder.recyclerViewDates.layoutManager = LinearLayoutManager(
            holder.recyclerViewDates.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        val datesListAdapter = DatesListAdapter(record.checkDateList)
        holder.recyclerViewDates.adapter = datesListAdapter
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }
}



