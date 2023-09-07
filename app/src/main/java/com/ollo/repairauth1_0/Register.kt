package com.ollo.repairauth1_0

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.util.*

class Register : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var emailContainer: TextInputLayout
    private lateinit var etPassword: EditText
    private lateinit var passwordContainer: TextInputLayout
    private lateinit var etConfirmPassword: EditText
    private lateinit var confirmPasswordContainer: TextInputLayout
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loginNow: TextView
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
        emailFocusListener()
        passwordFocusListener()
        passwordConfirmFocusListener()
        btnRegister.setOnClickListener { registerUser() }
        loginNow.setOnClickListener { changeToLoginNow() }
        auth = FirebaseAuth.getInstance()
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
    }

    fun initView() {
        etEmail = findViewById(R.id.email_edit_text)
        emailContainer = findViewById(R.id.email_container)
        etPassword = findViewById(R.id.password_edit_text)
        passwordContainer = findViewById(R.id.password_container)
        etConfirmPassword = findViewById(R.id.confirm_password_edit_text)
        confirmPasswordContainer = findViewById(R.id.confirm_password_layout)
        btnRegister = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progress_bar)
        loginNow = findViewById(R.id.login_now)
    }

    private fun emailFocusListener() {
        etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = etEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "E-Mail Validierung nicht möglich!"
        }
        return null
    }

    private fun passwordFocusListener() {
        etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = etPassword.text.toString()
        if(passwordText.length < 7) {
            return "Mindestens 6 Buchstaben."
        }
//        if(!passwordText.matches(".*[A-Z].*".toRegex())) {
//            return "Must Contain 1 Upper-case Charakter"
//        }
//        if(!passwordText.matches(".*[a-z].*".toRegex())) {
//            return "Must Contain 1 Lower-case Charakter"
//        }
//        if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
//            return "Must Contain 1 Special Charakter (@#\$%^&+=)"
//        }
        return null
    }

    private fun passwordConfirmFocusListener() {
        etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                confirmPasswordContainer.helperText = validConfirmPassword()
            }
        }
    }

    private fun validConfirmPassword(): String? {
        val passwordText = etPassword.text.toString()
        val confirmPasswordText = etConfirmPassword.text.toString()
        if(passwordText != confirmPasswordText) {
            return "Die Passwörter müssen übereinstimmen"
        }
        return null
    }


    fun changeToLoginNow() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun registerUser() {
        val userEmail = etEmail.text.toString()
        val userPassword = etPassword.text.toString()
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if (userPassword.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseAuthentificationWith(userEmail, userPassword)
    }

    fun firebaseAuthentificationWith(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        baseContext,
                        "Account created.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d(TAG, "createUserWithEmail:success")
                    //val userId = auth!!.currentUser!!.uid
                    //val currentUserDb = FirebaseAuth.getInstance().currentUser
                    //updateUI(user)
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    progressBar.visibility = View.GONE
                    //updateUI(null)
                }
            }
    }
}