package com.example.scmu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.scmu.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, RegisterActivity::class.java)
        //startActivity(intent)

        //dummyUser()
    }

    private fun dummyUser() {
        val email = "ujeendanko@gmail.com"
        val password = "qwerty123"

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //uploadImageToFirebaseStorage()

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(com.example.scmu.registerlogin.TAG, "createUserWithEmail:success")

                            val user = task.result?.user//auth.currentUser
                            Toast.makeText(
                                    baseContext, "Registration success.",
                                    Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(com.example.scmu.registerlogin.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                    baseContext, "Registration failed.",
                                    Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                        }
                    }
                    .addOnFailureListener(this) { task ->
                        task.message?.let { Log.d(com.example.scmu.registerlogin.TAG, it) }
                    }

        }

    }
}