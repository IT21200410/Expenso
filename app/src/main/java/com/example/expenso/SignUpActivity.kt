package com.example.expenso

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.Toast
import com.example.expenso.databinding.ActivitySignUpBinding
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : BaseActivity() {
    private var binding: ActivitySignUpBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        auth = Firebase.auth
//
        setUpActionBar()

        binding?.btnRegister?.setOnClickListener{
            registerUser()
        }


        binding?.tvLogin?.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun setUpActionBar()
    {
        setSupportActionBar(binding?.toolbarRegisterActivity)

        val actionBar = supportActionBar
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding?.toolbarRegisterActivity?.setNavigationOnClickListener{ onBackPressed() }
    }

    private fun validateRegisterDetails():Boolean {
        return when {
            TextUtils.isEmpty(binding?.etFirstName?.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding?.etLastName?.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding?.etEmail?.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding?.etPassword?.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(binding?.etConfirmPassword?.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password),true)
                false
            }

            binding?.etPassword?.text.toString().trim{ it <= ' '} != binding?.etConfirmPassword?.text.toString().trim{ it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }

            else -> {
                true
            }


        }
    }

    private fun registerUser()
    {
        if(validateRegisterDetails())
        {
            showProgressDialog()
            val email:String = binding?.etEmail?.text.toString().trim{ it <= ' '}
            val password:String = binding?.etPassword?.text.toString().trim{ it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        if (task.isSuccessful)
                        {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar("You are registered successfully. Your user id ${firebaseUser.uid}", true)

                            val user = User(
                                firebaseUser.uid,
                                binding?.etFirstName?.text.toString().trim { it <= ' '},
                                binding?.etLastName?.text.toString().trim { it <= ' '},
                                binding?.etEmail?.text.toString().trim{ it <= ' '}
                            )

                            FireStoreClass().registerUser(this@SignUpActivity, user)

//                            FirebaseAuth.getInstance().signOut()
//                            finish()

                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()

                        }
                        else
                        {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }

    fun userRegistrationSuccess(){
        hideProgressDialog()
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
    }

    fun userRegistrationFail(){
        Toast.makeText(this, "Registration was not successful", Toast.LENGTH_SHORT).show()
    }
}