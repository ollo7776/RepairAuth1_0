package com.ollo.repairauth1_0.Helpers

import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText

class Validation {

    fun String.isEmpty(): Boolean {
        return (TextUtils.isEmpty(this)
                || this.equals("", ignoreCase = true)
                || this.equals("{}", ignoreCase = true)
                || this.equals("null", ignoreCase = true)
                || this.equals("undefined", ignoreCase = true))
    }

    fun AppCompatEditText.isValidEmail(): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return !this.text.toString().isEmpty() && this.text.toString().matches(emailPattern)
    }

    fun EditText.isValidEmail(): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return !this.text.toString().isEmpty() && this.text.toString().matches(emailPattern)
    }

    fun String.isValidEmail(): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return !this.isEmpty() && this.matches(emailPattern)
    }
}