package com.ollo.repairauth1_0.Model

import android.animation.LayoutTransition
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.core.graphics.toColor
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
        private var userEmail = view.findViewById<TextView>(R.id.tv_user_email)
        private var id = view.findViewById<TextView>(R.id.tv_id)
        private var number = view.findViewById<TextView>(R.id.tv_number)
        private var description = view.findViewById<TextView>(R.id.tv_description)
        private var tryToRepair = view.findViewById<TextView>(R.id.tv_try_to_repair)
        private var status = view.findViewById<TextView>(R.id.tv_status)
        var btnEdit = view.findViewById<AppCompatImageButton>(R.id.btn_edit_record)
        val recyclerViewIconsRecord: RecyclerView = view.findViewById(R.id.rv_icons_on_record)
        private var startDatum = view.findViewById<TextView>(R.id.start_date)
        private var checkDateLastOne = view.findViewById<TextView>(R.id.check_date_last_one)
        var layoutLastCheckDate = view.findViewById<LinearLayout>(R.id.check_date_layout)
        val recyclerViewDates: RecyclerView = view.findViewById(R.id.rv_dates_on_record)
        private var layoutDatesExpand = view.findViewById<LinearLayoutCompat>(R.id.layout_dates_expand)

        fun bindView(record: RecordModel) {
            userEmail.text = record.userEmail
            id.text = record.id
            number.text = record.number
            description.text = record.description
            tryToRepair.text = record.tryToRepair
            status.text = record.status
            status.setTextColor(changeColorOfStatus(record.status))
            startDatum.text = record.startDate.toString()
            var lastDate = "Keine Einträge"
            if (record.checkDateList.isNotEmpty()) {
                val lastIndex = record.checkDateList.size - 1
                lastDate = record.checkDateList[lastIndex]
            }
            checkDateLastOne.text = lastDate
            layoutLastCheckDate.setOnClickListener { expandLayoutWithDates() }
        }
       private fun changeColorOfStatus(text: String): Int {
           var color: Int = Color.RED
            if(text == "Status fahrbar") {
                color = Color.parseColor("#00B300")
            } else if (text == "Status Warnung") {
                color = Color.parseColor("#FFBF00")
            } else if (text == "Status Gefährlich") {
                color = Color.parseColor("#CC2200")
            }
            return color
        }
        fun expandLayoutWithDates() {
            layoutDatesExpand.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            checkDateLastOne.setOnClickListener {
                val v = if (layoutDatesExpand.visibility == View.GONE) View.VISIBLE else View.GONE
                layoutDatesExpand.visibility = v
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

        // RECYCLER VIEW WITH CHECKDATES
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