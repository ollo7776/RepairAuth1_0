package com.ollo.repairauth1_0

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.ollo.repairauth1_0.Views.MessageToAdmin

class Login : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var btnActivityMessage: ImageButton
//    private lateinit var registerNow: TextView
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_gray)
        initView()

        btnLogin.setOnClickListener { loginUser() }
        //registerNow.setOnClickListener{ changeToRegisterNow() }
        btnActivityMessage.setOnClickListener { changeToActivityMessage() }
        auth = FirebaseAuth.getInstance()
    }

    fun initView() {
        etEmail = findViewById(R.id.email_edit_text)
        etPassword = findViewById(R.id.password_edit_text)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progress_bar)
        btnActivityMessage = findViewById(R.id.btn_activity_message)
       // registerNow = findViewById(R.id.register_now)
    }

    fun changeToRegisterNow() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }

    fun changeToActivityMessage() {
        val intent = Intent(this, MessageToAdmin::class.java)
        startActivity(intent)
        finish()
    }

    fun loginUser() {
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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
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