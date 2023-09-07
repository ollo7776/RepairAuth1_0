package com.ollo.repairauth1_0.Views

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ollo.repairauth1_0.Data.IconsResource
import com.ollo.repairauth1_0.Helpers.FireBaseHelper
import com.ollo.repairauth1_0.Login
import com.ollo.repairauth1_0.MainActivity
import com.ollo.repairauth1_0.Model.IconAdapter
import com.ollo.repairauth1_0.Model.IconModel
import com.ollo.repairauth1_0.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class EditRecord : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var tvId: TextView
    private lateinit var tvNumber: TextView
    private lateinit var tvStartDate: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvTryToRepair: TextView
    private lateinit var tvStatus: TextView
    private lateinit var rvIcons: RecyclerView

    private lateinit var tvCheckDateList: TextView
    private var iconAdapter: IconAdapter? = null
    private lateinit var btnDateNotRepaired: ImageFilterButton
    private lateinit var btnDateRepaired: ImageFilterButton
    private lateinit var btnClose: ImageButton
    private lateinit var btnDelete: Button
    private lateinit var iconsList: ArrayList<Int>
    private lateinit var currentDateNotRepaired: TextView
    private lateinit var currentDateRepaired: TextView

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItemUser = menu?.findItem(R.id.nav_user_name)
        menuItemUser?.setTitle("User logged in: ${user.email}")
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_main -> changeToMainActivity()
            R.id.nav_archive -> goToArchive()
            R.id.nav_logout -> logoutNow()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_record)
        initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        getSupportActionBar()?.setBackgroundDrawable(
            ColorDrawable(Color.parseColor("#CCCCCC"))
        )
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        val intent = intent
        val id = intent.getStringExtra("id")
        val startDate = intent.getStringExtra("startDate")
        val number = intent.getStringExtra("number")
        val description = intent.getStringExtra("description")
        val tryToRepair = intent.getStringExtra("tryToRepair")
        val status = intent.getStringExtra("status")
        val checkDateListDB = intent.getStringArrayListExtra("checkDateList")
        val endDate = intent.getStringExtra("endDate")

        Log.w(TAG,"Log CheckDateListDB: $checkDateListDB")
        tvId.text = id
        tvNumber.text = number
        tvStartDate.text = startDate
        tvDescription.text = description
        tvTryToRepair.text = tryToRepair
        tvStatus.text = status
        tvCheckDateList.text = checkDateListDB.toString()
        iconsList = intent.getIntegerArrayListExtra("iconsList")!!
        currentDateRepaired.text = getDateTimeToday()
        currentDateNotRepaired.text = getDateTimeToday()

        btnClose.setOnClickListener { changeToMainActivity() }
        btnDelete.setOnClickListener {
            if (id != null) {
                deleteRecordAndClose(id)
            }
        }
        btnDateNotRepaired.setOnClickListener {
            if (id != null && checkDateListDB != null) confirmBrokeDown(
                id,
                checkDateListDB
            )
        }
        btnDateRepaired.setOnClickListener {
            if (id != null) {
                addEndDate(id)
            }
        }
        initRVIcons()

    }

    fun initView() {
        tvId = findViewById(R.id.edit_tv_id)
        tvNumber = findViewById(R.id.edit_tv_number)
        tvStartDate = findViewById(R.id.edit_tv_start_date)
        tvDescription = findViewById(R.id.edit_tv_description)
        tvTryToRepair = findViewById(R.id.edit_tv_try_to_repair)
        tvStatus = findViewById(R.id.edit_tv_status)
        rvIcons = findViewById(R.id.edit_icon_recyclerview)
        tvCheckDateList = findViewById(R.id.edit_tv_check_date_list)

        btnDateNotRepaired = findViewById(R.id.btn_date_not_repaired)
        btnDateRepaired = findViewById(R.id.btn_date_repaired)
        btnClose = findViewById(R.id.btn_edit_close)
        btnDelete = findViewById(R.id.btn_edit_remove)
        currentDateNotRepaired = findViewById(R.id.current_date_not_repaired)
        currentDateRepaired = findViewById(R.id.current_date_repaired)
    }

    fun initRVIcons() {
        var iconsData = IconsResource().loadIcons()
        var iconsForRecord: ArrayList<IconModel> = ArrayList()
        rvIcons.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        for (item in iconsData) {
            if (iconsList?.contains(item.imageResourceId)!!) {
                iconsForRecord.add(item)
            }
        }
        //Log.w(TAG,"iconsForRecord: ${iconsForRecord}")
        iconAdapter = IconAdapter(iconsForRecord)
        rvIcons.adapter = iconAdapter
    }

    fun getDateTimeToday(): String {
        val simpleDate = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        return simpleDate.format(Date()).toString()
    }
    fun getDateToday(): String {
        val simpleDate = SimpleDateFormat("dd.MM.yyyy")
        return simpleDate.format(Date()).toString()
    }

    fun logoutNow() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToArchive() {
        val intent = Intent(this, Archiv::class.java)
        startActivity(intent)
        finish()
    }

    private fun deleteRecordAndClose(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Sind Sie sicher, dass dieser Record gelöscht werden muss? Nach Genehmigung durch den Administrator wird der Datensatz gelöscht.")
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            FireBaseHelper.infoToRemove(id)
            dialog.dismiss()
            changeToMainActivity()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun changeToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun confirmBrokeDown(id: String, checkDateList: ArrayList<String>) {
        //Toast.makeText(this, "Fehler Bestätigt", Toast.LENGTH_SHORT).show()
        var currentDateList = checkDateList
        var dateToday = getDateToday()
        var confirm = false
        for(date in currentDateList) {
            val dateNoTime = date.substring(0,10)
            if (dateNoTime.equals(dateToday)) {
                confirm = true
            }
            if(confirm) break
        }
        if(confirm) {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)
            builder.setMessage("Fehler bereits bestätigt! Danke für Ihre Information")
            builder.setPositiveButton("OK") { dialog, _ ->
                changeToMainActivity()
                }
            builder.show()

        } else  {
            currentDateList?.add(getDateTimeToday())
            FireBaseHelper.addCheckDate(id, currentDateList)
            changeToMainActivity()
        }

    }

    private fun addEndDate(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setMessage("Wenn der Fehler nicht mehr vorhanden ist, klicken Sie auf OK. Dieser Record wird in die Registerkarte „Archiv“ verschoben")
        builder.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            Toast.makeText(this, "An das Archiv gesendet", Toast.LENGTH_SHORT).show()
            FireBaseHelper.addEndDate(id, getDateTimeToday())
            val intent = Intent(this, Archiv::class.java)
            startActivity(intent)
            finish()
        }
        builder.show()
    }

//    private fun pickDate() {
//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//        val datePickerDialog = DatePickerDialog(
//            this,
//            { view, year, monthOfYear, dayOfMonth ->
//                selectedDateTv.text =
//                    (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
//            },
//            year,
//            month,
//            day
//        )
//        datePickerDialog.show()
//    }
}