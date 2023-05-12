package com.example.expenso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class UpdateProfile : BaseActivity() {
    private lateinit var FirstName: EditText
    private lateinit var updateBtn: Button
    private lateinit var LastName: MaterialAutoCompleteTextView
    private lateinit var emailID: EditText

    private lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
    }
}