package com.ollo.repairauth1_0.Data

import com.ollo.repairauth1_0.Model.IconModel
import com.ollo.repairauth1_0.R

class IconsResource() {
    fun loadIcons(): List<IconModel> {
        return listOf<IconModel>(
            IconModel(R.string.breaks_tabs , R.drawable.ic_t_breaks_tabs_red_24, 0),
            IconModel(R.string.abs, R.drawable.ic_t_abs_24, 0),
            IconModel(R.string.volume, R.drawable.ic_t_volume_off_24, 0),
            IconModel(R.string.doors, R.drawable.ic_t_doors24, 0),
            IconModel(R.string.engine_red, R.drawable.ic_t_engine_red_24, 0),
            IconModel(R.string.engine_yellow, R.drawable.ic_t_engine_yellow_24, 0),
            IconModel(R.string.filter_red, R.drawable.ic_t_filter_red_24, 0),
            IconModel(R.string.filter_yellow, R.drawable.ic_t_filter_yellow_24, 0),
            IconModel(R.string.abs, R.drawable.ic_t_haltestelle, 0),
            IconModel(R.string.abs, R.drawable.ic_t_kneeing, 0),
            IconModel(R.string.abs, R.drawable.ic_t_lights, 0),
            IconModel(R.string.abs, R.drawable.ic_t_settings, 0),
            IconModel(R.string.abs, R.drawable.ic_t_tachograf, 0),
            IconModel(R.string.abs, R.drawable.ic_t_warning, 0),
            IconModel(R.string.abs, R.drawable.ic_t_child, 0),
            IconModel(R.string.abs, R.drawable.ic_t_tire, 0),
            IconModel(R.string.abs, R.drawable.ic_t_baterie, 0),
            IconModel(R.string.abs, R.drawable.ic_t_water, 0),
            IconModel(R.string.abs, R.drawable.ic_t_lamp, 0),
        )
    }
}