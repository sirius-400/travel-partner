package com.abc.travelpartner.ui.account

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class AccountViewModel: ViewModel() {
    private val _userName: MutableLiveData<String>? = null
    val userName: LiveData<String> = _userName!!
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userCollection: CollectionReference = db.collection("users")

    fun getCurrentUserName(userId: String?, context: Context): LiveData<String> {
        userCollection.whereEqualTo("userId",userId).get()
                .addOnSuccessListener {
                    for (document in it.documents){
                        _userName?.value = document.getString("userName")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                }
        return userName
    }
}