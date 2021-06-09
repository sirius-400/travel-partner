package com.abc.travelpartner.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abc.travelpartner.data.entity.User
import com.abc.travelpartner.databinding.ActivityRegisterBinding
import com.abc.travelpartner.ui.map.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userCollection: CollectionReference = db.collection("users")

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = binding.etEmailForm.text.toString()
        val password = binding.etPasswordForm.text.toString()
        val username = binding.etUsernameForm.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(it.isComplete) {
                        val currentUser = firebaseAuth.currentUser!!
                        val currentUserId = currentUser.uid
                        val userName = username
                        userCollection.add(User(currentUserId,userName))
                        val intent = Intent(this, MapsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this,"please input all fields",Toast.LENGTH_SHORT).show()
        }
    }
}