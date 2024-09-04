package com.ollo.repairauth1_0.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.collection.LLRBNode.Color
import com.ollo.repairauth1_0.R

class DatesListAdapter(
    private var datesList: List<String>
) : RecyclerView.Adapter<DatesListAdapter.DatesViewHolder>() {
    class DatesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private var itemDate = view.findViewById<TextView>(R.id.item_date)

        fun bindView(date: String) {
            itemDate.text = date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return DatesViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: DatesViewHolder, position: Int) {
        val date = datesList[position]
        holder.bindView(date)

    }
    override fun getItemCount(): Int {
        return datesList.size
    }

}