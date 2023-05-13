package com.example.expenso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.User
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class UpdateProfile : BaseActivity() {
    private lateinit var FirstName: EditText
    private lateinit var LastName: EditText
    private lateinit var emailID: EditText
    private var userID:String? = ""
    private lateinit var updateBtn: Button

    private lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        FirstName = findViewById(R.id.tv_first_name)
        LastName = findViewById(R.id.tv_last_name)
        emailID = findViewById(R.id.tv_email)
        updateBtn = findViewById(R.id.btn_update)

        updateBtn.setOnClickListener{

            val updatedUser = User(userID!!,
                FirstName.text.toString().trim { it <= ' ' },
                LastName.text.toString().trim { it <= ' ' },
                emailID.text.toString().trim { it <= ' ' })

            FireStoreClass().updateUserDetails(this, updatedUser)
            finish()
        }

        FireStoreClass().getUserDetails(this)
    }

    fun displayUser(user: String?, firstName: String?, lastName: String?, email:String?)
    {
        userID = user
        FirstName.setText(firstName)
        LastName.setText(lastName)
        emailID.setText(email)
    }

    fun updateSuccess(){
        Toast.makeText(this, "Transaction edited", Toast.LENGTH_SHORT).show()
    }

    fun updateFail(){
        Toast.makeText(this, "Couldn't edit transaction", Toast.LENGTH_SHORT).show()
    }
}