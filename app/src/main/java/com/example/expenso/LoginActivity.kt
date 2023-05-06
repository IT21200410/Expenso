package com.example.expenso

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.expenso.databinding.ActivityLoginBinding
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    private var binding:ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding?.btnLogin?.setOnClickListener(this)
        binding?.tvRegister?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        if( v != null)
        {
            when(v.id){
                R.id.tv_forgot_password -> {

                }
                R.id.btn_login -> {
                    loginRegisteredUser()
                }
                R.id.tv_register -> {
                binding?.tvRegister?.setOnClickListener {
                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                }
            }
        }
    }
    private fun validateLoginDetails():Boolean {
        return when{
            TextUtils.isEmpty(binding?.etEmail?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding?.etPassword?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginRegisteredUser()
    {
        if(validateLoginDetails())
        {
            showProgressDialog()

            val email = binding?.etEmail?.text.toString().trim{ it <= ' '}
            val password = binding?.etPassword?.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    task ->

                    if(task.isSuccessful)
                    {
                        FireStoreClass().getUserDetails(this)
                    }
                    else
                    {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user: User)
    {
        hideProgressDialog()

        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

}