package com.ollo.repairauth1_0.Views

import android.animation.LayoutTransition
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ollo.repairauth1_0.Login
import com.ollo.repairauth1_0.MainActivity
import com.ollo.repairauth1_0.Model.RecordAdapter
import com.ollo.repairauth1_0.Model.RecordAdapterArchiv
import com.ollo.repairauth1_0.Model.RecordModel
import com.ollo.repairauth1_0.R

class Archiv: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var repairsList: ArrayList<RecordModel>
    private lateinit var dbRef: DatabaseReference
    private var adapter: RecordAdapterArchiv? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCloseArchive: AppCompatImageButton

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItemUser = menu?.findItem(R.id.nav_user_name)
        menuItemUser?.setTitle("User logged in: ${user.email}")
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_main -> changeToMainActivity()
            R.id.nav_logout -> logoutNow()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        getSupportActionBar()?.setBackgroundDrawable(
            ColorDrawable(Color.parseColor("#CCCCCC"))
        )
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        initView()
        initRecyclerView()
        repairsList = arrayListOf()
        fetchRepairs()
        btnCloseArchive.setOnClickListener { changeToMainActivity() }
    }

    fun initView() {
        recyclerView = findViewById(R.id.recycler_view_archiv)
        btnCloseArchive = findViewById(R.id.btn_arvhive_close)
    }

    fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecordAdapterArchiv()
        recyclerView.adapter = adapter
    }

    private fun changeToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun logoutNow() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchRepairs() {
        dbRef = FirebaseDatabase.getInstance().getReference("Repairs")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                repairsList.clear()
                if (snapshot.exists()) {
                    for (repairSnap in snapshot.children) {
                        val repairData = repairSnap.getValue(RecordModel::class.java)
                        if (repairData?.endDate != "")
                        repairsList.add(repairData!!)
                    }
                    adapter?.addRecord(repairsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        })
    }
}