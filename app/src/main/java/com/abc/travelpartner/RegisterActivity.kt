package com.abc.travelpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abc.travelpartner.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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
        if(email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(task: Task<AuthResult>) {
                            if(task.isComplete) {
                                val currentUser = firebaseAuth.currentUser!!
                                val currentUserId = currentUser.uid
                                val userName = "@bambang123"
                                userCollection.add(User(currentUserId,userName))
                            }
                        }
                    })
        }
    }
}