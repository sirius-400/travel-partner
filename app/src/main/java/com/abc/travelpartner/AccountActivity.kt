package com.abc.travelpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abc.travelpartner.databinding.ActivityAccountBinding
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
        }
    }
}