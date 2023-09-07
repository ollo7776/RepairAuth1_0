package com.ollo.repairauth1_0.Views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ollo.repairauth1_0.Login
import com.ollo.repairauth1_0.MainActivity
import com.ollo.repairauth1_0.R

class AccDelActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var tvMessageEmail: TextView
    private lateinit var etMessageTitle: EditText
    private lateinit var etMessageText: EditText
    private lateinit var adminEmail: String
    private lateinit var btnMessageSend: Button
    private lateinit var btnChangeToMain: Button
    var isAllFieldsChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avtivity_msg_acc_del)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        initView()
        btnChangeToMain.setOnClickListener { changeToMainNow() }
        btnMessageSend.setOnClickListener { sendEmailToAdmin() }
        //        FIREBASE AUTH
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        tvMessageEmail.text = user.email.toString()
    }

    fun initView() {
        tvMessageEmail = findViewById(R.id.tv_message_email)
        etMessageTitle = findViewById(R.id.et_message_title)
        etMessageText = findViewById(R.id.et_message_text)
        btnMessageSend = findViewById(R.id.btn_message_send)
        btnChangeToMain = findViewById(R.id.btn_change_to_main)
        adminEmail = "ollo7776@gmail.com"
    }

    fun sendEmailToAdmin() {
        isAllFieldsChecked = checkAllFields()
        val email = "ollo7776@gmail.com"
        val subject = etMessageTitle.text.toString()
        val text = etMessageText.text.toString() + "\nMein email: " + tvMessageEmail.text.toString()
        val addresses = email.split(",".toRegex()).toTypedArray()
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        if (isAllFieldsChecked == true) {
            startActivity(intent)
            Toast.makeText(this, "Danke für Ihre Nachricht", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "Bitte korrigieren Sie die erforderlichen Formularfelder",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun changeToMainNow() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkAllFields(): Boolean {
        if (etMessageTitle!!.length() == 0) {
            etMessageTitle!!.error = "Dieses Feld ist erforderlich"
            return false
        }
        if (etMessageText!!.length() == 0) {
            etMessageText!!.error = "Dieses Feld ist erforderlich"
            return false
        }
        return true
    }

}