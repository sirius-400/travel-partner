package com.abc.travelpartner.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abc.travelpartner.ui.register.RegisterActivity
import com.abc.travelpartner.databinding.ActivityLoginBinding
import com.abc.travelpartner.ui.map.MapsActivity
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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun checkLoggedInState() {
        if(firebaseAuth.currentUser != null) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = binding.etEmailForm.text.toString()
        val password = binding.etPasswordForm.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()) {
            binding.progressbar.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    binding.progressbar.visibility = View.GONE
                    checkLoggedInState()
                }
                .addOnFailureListener {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this,"please input all field",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }
}