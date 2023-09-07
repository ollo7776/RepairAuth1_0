package com.ollo.repairauth1_0.Model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ollo.repairauth1_0.MainActivity
import com.ollo.repairauth1_0.R

class IconAdapter(
    private val datasetIcons: List<IconModel>,
    private var onClickIcon: ((IconModel) -> Unit)? = null
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    fun setOnClickIcon(callback: (IconModel) -> Unit) {
        this.onClickIcon = callback
    }

    class IconViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.icon_view_svg)
        private var iconItem = view.findViewById<LinearLayout>(R.id.icon_item)
        fun bindView(icon: IconModel) {
            if (icon.iconMarked == 0) {
                iconItem.setBackgroundColor(Color.parseColor("#00000000"))
            } else if(icon.iconMarked == 1) {
                iconItem.setBackgroundColor(Color.parseColor("red"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon, parent, false)
        return IconViewHolder(adapterLayout)

    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = datasetIcons[position]
        holder.bindView(icon)
        holder.itemView.setOnClickListener { onClickIcon?.invoke(icon) }
        holder.imageView.setImageResource(icon.imageResourceId)
    }

    override fun getItemCount(): Int {
        return datasetIcons.size
    }
}