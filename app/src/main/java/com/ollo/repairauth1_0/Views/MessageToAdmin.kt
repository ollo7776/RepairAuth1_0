package com.ollo.repairauth1_0.Views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.SyncStateContract.Helpers
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ollo.repairauth1_0.Helpers.Validation
import com.ollo.repairauth1_0.Login
import com.ollo.repairauth1_0.R

class MessageToAdmin : AppCompatActivity() {
    private lateinit var etMessageEmail: EditText
    private lateinit var etMessageEmailConfirm: EditText
    private lateinit var etMessageTitle: EditText
    private lateinit var etMessageText: EditText
    private lateinit var adminEmail: String
    private lateinit var btnMessageSend: Button
    private lateinit var btnChangeToLogin: Button
    var isAllFieldsChecked = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avtivity_message)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        initView()
        btnMessageSend.setOnClickListener { sendEmailToAdmin() }
        btnChangeToLogin.setOnClickListener { changeToLoginNow() }
    }

    fun initView() {
        etMessageEmail = findViewById(R.id.et_message_email)
        etMessageEmailConfirm = findViewById(R.id.et_message_email_confirm)
        etMessageTitle = findViewById(R.id.et_message_title)
        etMessageText = findViewById(R.id.et_message_text)
        btnMessageSend = findViewById(R.id.btn_message_send)
        btnChangeToLogin = findViewById(R.id.btn_change_to_login_from_msg)
        adminEmail = "ollo7776@gmail.com"
    }

    fun sendEmailToAdmin() {
        isAllFieldsChecked = checkAllFields()
        val email = "ollo7776@gmail.com"
        val subject = etMessageTitle.text.toString()
        val text = etMessageText.text.toString() + "\nMein email: " + etMessageEmail.text.toString()
        val addresses = email.split(",".toRegex()).toTypedArray()
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        if (isAllFieldsChecked == true) {
            startActivity(intent)
            Toast.makeText(this, "Dabke für Ihre Nachricht", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "Bitte korrigieren Sie die erforderlichen Formularfelder",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun changeToLoginNow() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkAllFields(): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        if (etMessageEmail!!.length() == 0) {
            etMessageEmail!!.error = "Dieses Feld ist erforderlich"
            return false
        }
        if (!etMessageEmail.text.toString().matches(emailPattern)) {
            etMessageEmail!!.error = "Dies ist keine gültige e-mail Adresse"
            return false
        }
        if (etMessageEmailConfirm!!.text.toString() != etMessageEmail.text.toString()) {
            etMessageEmailConfirm!!.error = "Die angegebenen E-Mail-Adressen sind nicht identisch"
            return false
        }
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