package com.ollo.repairauth1_0.Model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class IconModel(
    @StringRes
    val stringResourceId: Int,
    @DrawableRes
    val imageResourceId: Int,
    var iconMarked: Int = 0
)