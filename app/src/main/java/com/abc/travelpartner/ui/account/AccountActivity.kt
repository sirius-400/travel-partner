package com.abc.travelpartner.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.abc.travelpartner.databinding.ActivityAccountBinding
import com.abc.travelpartner.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        viewModel.getCurrentUserName(userId,this).observe(this,{userName ->
            binding.tvUsername.text = userName
        })

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            Toast.makeText(this,"You are Logged out",Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }
}