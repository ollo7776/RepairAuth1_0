package com.ollo.repairauth1_0

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ollo.repairauth1_0.Data.IconsResource
import com.ollo.repairauth1_0.Helpers.FireBaseHelper
import com.ollo.repairauth1_0.Model.IconAdapter
import com.ollo.repairauth1_0.Model.IconModel
import com.ollo.repairauth1_0.Model.RecordAdapter
import com.ollo.repairauth1_0.Model.RecordModel
import com.ollo.repairauth1_0.Views.AccDelActivity
import com.ollo.repairauth1_0.Views.Archiv
import com.ollo.repairauth1_0.Views.EditRecord
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var actNumber: AutoCompleteTextView
    private lateinit var listNumber: Array<String>
    private lateinit var edDescription: EditText
    private lateinit var edTryToRepair: EditText
    private lateinit var tvStatus: TextView
    private lateinit var checkedStatus: String
    private lateinit var radioGroupStatus: RadioGroup
    private lateinit var radioBtnStatus1: RadioButton
    private lateinit var radioBtnStatus2: RadioButton
    private lateinit var radioBtnStatus3: RadioButton

    private lateinit var iconsData: List<IconModel>
    private lateinit var recyclerViewIcons: RecyclerView
    private var iconAdapter: IconAdapter? = null
    private lateinit var iconList: ArrayList<Int>

    private lateinit var repairsList: ArrayList<RecordModel>
    private lateinit var dbRef: DatabaseReference

    private lateinit var cardFormExpand: CardView
    private lateinit var layoutFormExpand: LinearLayoutCompat
    private lateinit var titleFormExpand: TextView


    private lateinit var btnFetch: AppCompatImageButton
    private lateinit var recyclerView: RecyclerView
    private var adapter: RecordAdapter? = null
    private lateinit var btnInsert: AppCompatButton
    private lateinit var btnDelete: AppCompatImageButton
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        getSupportActionBar()?.setBackgroundDrawable(
            ColorDrawable(Color.parseColor("#CCCCCC"))
        )
        initView()
        initRecyclerViewIcons()
        initRecyclerView()
        expandLayoutForm()
        fetchRepairs()
        iconList = arrayListOf()

//        FIREBASE AUTH
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        if (user == null) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            // userDetails.setText(user.email)
            // menuItemUser.setTitle(user.email)
        }
        // btnLogout.setOnClickListener { logoutNow() }

//        DATA FOR REALTME DATABASE
        listNumber = resources.getStringArray(R.array.numbers_array)
        val numberAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listNumber)
        actNumber.setAdapter(numberAdapter)
        repairsList = arrayListOf()
        dbRef = FirebaseDatabase.getInstance().getReference("Repairs")
        btnFetch.setOnClickListener { fetchRepairs() }
        adapter?.setOnClickEditRecord {
            editRepair(
                it.id,
                it.number,
                it.startDate,
                it.description,
                it.tryToRepair,
                it.status,
                it.checkDateList,
                it.iconsList,
            )
        }
        btnInsert.setOnClickListener { insertRepairs() }
        btnDelete.setOnClickListener { clearEditFields() }
        checkedStatus = ""
        radioGroupStatus.setOnCheckedChangeListener { _, checkedId ->
            //Log.w(TAG, "toggeld id radioBtn: ${checkedId} and ${R.id.radio_btn_status_2}")
            if (checkedId == R.id.radio_btn_status_1) {
                tvStatus.text = "Gefährlich"
                checkedStatus = "Status gefährlich"
            }
            if (checkedId == R.id.radio_btn_status_2) {
                tvStatus.text = "Warnung"
                checkedStatus = "Status Warnung"
            }
            if (checkedId == R.id.radio_btn_status_3) {
                tvStatus.text = "Fahrbar"
                checkedStatus = "Status fahrbar"
            }
            progressMoves()
        }


        iconAdapter?.setOnClickIcon {
            for (icon in iconsData) {
                if (icon.imageResourceId == it.imageResourceId) {
                    if (icon.iconMarked == 0) {
                        icon.iconMarked = 1
                    } else if (icon.iconMarked == 1) {
                        icon.iconMarked = 0
                    }
                }
            }
            addToIconIdList(it.imageResourceId)
            getIconIdList()
            Log.w(TAG, "IconList: ${iconList}")
        }
        actNumber.setOnFocusChangeListener {  _, hasFocus ->
            if (!hasFocus) {
                progressMoves()
            }
        }
        edDescription.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                progressMoves()
            }
        }
        edTryToRepair.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                progressMoves()
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItemUser = menu?.findItem(R.id.nav_user_name)
        menuItemUser?.setTitle(" ${user.email}")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_archive -> goToArchive()
            R.id.nav_logout -> logoutNow()
            R.id.user_account_delete -> goToUserAccDel()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun progressMoves() {
        if (actNumber.text.isNotEmpty()) {
            progressBar.setProgress(progressBar.progress + 10)
        }
        if (edDescription.text.isNotEmpty()) {
            progressBar.setProgress(progressBar.progress + 10)
        }
        if (edTryToRepair.text.isNotEmpty()) {
            progressBar.setProgress(progressBar.progress + 10)
        }
    }

    private fun getIconIdList() {
        iconAdapter?.notifyDataSetChanged()
    }

    private fun addToIconIdList(imageId: Int) {
        findInList(imageId)
    }

    private fun findInList(idToFind: Int) {
        if (iconList.contains(idToFind)) {
            iconList.remove(idToFind)
        } else {
            iconList.add(idToFind)
        }
    }

    fun initView() {
        actNumber = findViewById(R.id.act_number)
        edDescription = findViewById(R.id.et_description)
        edTryToRepair = findViewById(R.id.et_try_to_repair)
        tvStatus = findViewById(R.id.tv_status)
        radioGroupStatus = findViewById(R.id.radio_group_status)

        btnFetch = findViewById(R.id.btn_fetch)
        btnInsert = findViewById(R.id.btn_insert)
        btnDelete = findViewById(R.id.btn_delete)
        progressBar = findViewById(R.id.progress_bar)
        radioBtnStatus1 = findViewById(R.id.radio_btn_status_1)
        radioBtnStatus2 = findViewById(R.id.radio_btn_status_2)
        radioBtnStatus3 = findViewById(R.id.radio_btn_status_3)

        cardFormExpand = findViewById(R.id.card_form_expand)
        titleFormExpand = findViewById(R.id.title_form_expand)
        layoutFormExpand = findViewById(R.id.layout_form_expand)

        recyclerViewIcons = findViewById(R.id.recycler_view_icons)
        recyclerView = findViewById(R.id.recycler_view)

    }

    fun expandLayoutForm() {
        layoutFormExpand.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        cardFormExpand.setOnClickListener {
            val v = if (layoutFormExpand.visibility == View.GONE) View.VISIBLE else View.GONE
            layoutFormExpand.visibility = v
        }
    }

    fun initRecyclerViewIcons() {
        iconsData = IconsResource().loadIcons()
        recyclerViewIcons.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        iconAdapter = IconAdapter(iconsData)
        recyclerViewIcons.adapter = iconAdapter
    }

    fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecordAdapter()
        recyclerView.adapter = adapter
    }

    fun logoutNow() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun getDateTimeToday(): String {
        val simpleDate = SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm:ss")
        return simpleDate.format(Date()).toString()
    }

    private fun goToArchive() {
        val intent = Intent(this, Archiv::class.java)
        startActivity(intent)
        finish()
    }
    private fun goToUserAccDel() {
        val intent = Intent(this, AccDelActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearEditFields() {
        actNumber.setText("")
        edDescription.setText("")
        checkedStatus = ""
        radioGroupStatus.clearCheck()
        edTryToRepair.setText("")
        tvStatus.text = "Status"
        iconList.clear()
        for (item in iconsData) {
            item.iconMarked = 0
        }
        iconAdapter?.notifyDataSetChanged()
        val v = if (layoutFormExpand.visibility == View.GONE) View.VISIBLE else View.GONE
        layoutFormExpand.visibility = v
        progressBar.setProgress(10)
    }

    private fun insertRepairs() {
        val checkDateList = arrayListOf<String>()
        val startDate = getDateTimeToday()
        val number = actNumber.text.toString()
        val description = edDescription.text.toString()
        val tryToRepair = edTryToRepair.text.toString()
        val status = checkedStatus
        val iconList = iconList
        val infoToRemove = false
        val userEmail = user.email!!
        if (number.isEmpty()) {
            actNumber.error = "Nummer eingeben."
        } else if (description.isEmpty()) {
            edDescription.error = "Beschreibung eingeben."
        } else if (tvStatus.text == "Status") {
            tvStatus.error = "Status wählen."
        } else {
            var dbMessage: String =
                FireBaseHelper.insertRepair(
                    number,
                    description,
                    tryToRepair,
                    status,
                    iconList,
                    startDate,
                    checkDateList,
                    "",
                    infoToRemove,
                    userEmail
                )
            if (dbMessage == "0") {
                Toast.makeText(this, "Daten gespeichert", Toast.LENGTH_SHORT).show()
            } else if (dbMessage == "1") {
                Toast.makeText(this, "Error: ${dbMessage}", Toast.LENGTH_SHORT).show()
            }
            progressBar.setProgress(100)
            clearEditFields()
            expandLayoutForm()
        }
    }

    private fun fetchRepairs() {
        dbRef = FirebaseDatabase.getInstance().getReference("Repairs")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                repairsList.clear()
                if (snapshot.exists()) {
                    for (repairSnap in snapshot.children) {
                        val repairData = repairSnap.getValue(RecordModel::class.java)
                        if (repairData?.endDate == "")
                            repairsList.add(repairData!!)
                    }
                    adapter?.addRecord(repairsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }
        })
    }


    private fun editRepair(
        id: String,
        number: String,
        startDate: String,
        description: String,
        tryToRepair: String,
        status: String,
        checkDateList: ArrayList<String>,
        iconsList: ArrayList<Int>
    ) {
        val intent = Intent(this, EditRecord::class.java)
        intent.putExtra("id", id)
        intent.putExtra("number", number)
        intent.putExtra("startDate", startDate)
        intent.putExtra("description", description)
        intent.putExtra("tryToRepair", tryToRepair)
        intent.putExtra("status", status)
        intent.putExtra("checkDateList", checkDateList)
        intent.putExtra("iconsList", iconsList)
        startActivity(intent)
        finish()
    }

//    private fun editRecord(
//        id: String,
//        number: String,
//        description: String,
//        tryToRepair: String,
//        status: String,
//        iconsList: ArrayList<Int>
//    ) {
//        val builder = AlertDialog.Builder(this)
//        builder.setCancelable(true)
//        builder.setNegativeButton("Schliesßen") { dialog, _ -> dialog.dismiss() }
//        val inflater = layoutInflater
//        builder.setTitle("Record ändern")
//        val dialogLayout = inflater.inflate(R.layout.edit_record, null)
//        val editId = dialogLayout.findViewById<TextView>(R.id.edit_tv_id)
//        val editNumber = dialogLayout.findViewById<TextView>(R.id.edit_tv_number)
//        val editDescription = dialogLayout.findViewById<TextView>(R.id.edit_tv_description)
//        val editTryToRepair = dialogLayout.findViewById<TextView>(R.id.edit_tv_try_to_repair)
//        val editStatus = dialogLayout.findViewById<TextView>(R.id.edit_tv_status)
//        val editIconRecycler = dialogLayout.findViewById<RecyclerView>(R.id.edit_icon_recyclerview)
//        editId.setText(id)
//        editNumber.setText(number)
//        editDescription.setText(description)
//        editTryToRepair.setText(tryToRepair)
//        editStatus.setText(status)
////ICONS RECYCLER ON UPDATE WINDOW
//        var iconsForRecord = ArrayList<IconModel>()
//        iconsData = IconsResource().loadIcons()
//        for (item in iconsData) {
//            if (iconsList.contains(item.imageResourceId)) {
//                iconsForRecord.add(item)
//            }
//        }
//        editIconRecycler.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        iconAdapter = IconAdapter(iconsForRecord)
//        editIconRecycler.adapter = iconAdapter
//        builder.setView(dialogLayout)
//
//        builder.setPositiveButton("Archive") { dialog, i ->
//            Toast.makeText(applicationContext, "In der Archive gespeichert.", Toast.LENGTH_SHORT)
//                .show()
//            dialog.dismiss()
//        }
//        builder.setPositiveButton("Entfernen") { dialogInterface, i ->
//            FireBaseHelper.deleteRecord(id)
//            Toast.makeText(applicationContext, "Record gelöscht.", Toast.LENGTH_SHORT).show()
//            dialogInterface.dismiss()
//        }
//        builder.show()
//    }

    private fun deleteRecord(id: String) {
        if (id == null) return
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Record löschen?")
        builder.setCancelable(true)
        builder.setPositiveButton("JA") { dialog, _ ->
            FireBaseHelper.deleteRecord(id)
            dialog.dismiss()
        }
        builder.setNegativeButton("NEIN") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

}