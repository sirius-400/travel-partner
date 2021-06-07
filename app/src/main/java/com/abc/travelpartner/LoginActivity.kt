package com.abc.travelpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abc.travelpartner.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun checkLoggedInState() {
        if(firebaseAuth.currentUser == null) {
            Toast.makeText(this,"You are Logged In", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "You are not Logged In yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser() {
        val email = binding.etEmailForm.text.toString()
        val password = binding.etPasswordForm.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
            checkLoggedInState()
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }
}