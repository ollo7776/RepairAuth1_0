package com.ollo.repairauth1_0.Model

import java.text.SimpleDateFormat

class RecordModel(
    var id: String = "",
    var number: String = "",
    var description: String = "",
    var tryToRepair: String = "",
    var status: String = "",
    var iconsList: ArrayList<Int> = arrayListOf(),
    var startDate: String = "",
    var checkDateList: ArrayList<String> = arrayListOf(),
    var endDate: String = "",
    var infoToRemove: Boolean = false,
    var userEmail: String = ""
)