package com.example.scmu.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.scmu.R
import com.example.scmu.User
import com.example.scmu.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

private lateinit var database: DatabaseReference
val TAG = "MyTagActivity"

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Log.d("MyTagActivity", "Application started 2")
    }

    fun registerFun(view: View) {
        val username = findViewById<TextView>(R.id.username_edittext_register).text.toString()
        val email = findViewById<TextView>(R.id.email_edittext_register).text.toString()
        val password = findViewById<TextView>(R.id.password_edittext_register).text.toString()

        Log.d("MyTagActivity", "Email is$email")
        Log.d("MyTagActivity", "Password is$password")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        uploadImageToFirebaseStorage()

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        val user = task.result?.user//auth.currentUser
                        Toast.makeText(
                            baseContext, "Registration success.",
                            Toast.LENGTH_SHORT
                        ).show()
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Registration failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        //updateUI(null)
                    }
                }
                .addOnFailureListener(this) {task ->
                    task.message?.let { Log.d(TAG, it) }
                }
        } else {
            Toast.makeText(
                baseContext, "Registration failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun alreadyRegisteredFun(view: View) {
        val username = findViewById<TextView>(R.id.username_edittext_register).text.toString()
        val email = findViewById<TextView>(R.id.email_edittext_register).text.toString()
        val password = findViewById<TextView>(R.id.password_edittext_register).text.toString()

        Log.d("MyTagActivity", "testFun2")
        Log.d("MyTagActivity", "Email is$email")
        Log.d("MyTagActivity", "Password is$password")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun selectPhotoFun(view: View)
    {
        Log.d(TAG, "photo!")

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)
        {
            Log.d(TAG, "photo was selected!")
            
            selectedPhotoUri = data.data

            val selectPhotoButton = findViewById<Button>(R.id.selectphoto_button_register)
            selectPhotoButton.alpha = 0f

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val selectPhotoCircleView = findViewById<CircleImageView>(R.id.selectphoto_imageview_register)
            selectPhotoCircleView.setImageBitmap(bitmap)
        }
    }

    private fun uploadImageToFirebaseStorage()
    {
        Log.d(TAG, "uploadImageToFirebaseStorage START")

        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File location: ${it.toString()}")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "uploadImageToFirebaseStorage FAIL")
                //
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl:String)
    {
        Log.d(TAG, "saveUserToFirebaseDatabase START")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        val username = findViewById<TextView>(R.id.username_edittext_register).text.toString()

        val user = User(uid, username, profileImageUrl)
        Log.d(TAG, "We saved user to Firebase Database")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "We saved user to Firebase Database")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "We DON'T saved user to Firebase Database")
            }

        Log.d(TAG, "saveUserToFirebaseDatabase END")
    }
}
