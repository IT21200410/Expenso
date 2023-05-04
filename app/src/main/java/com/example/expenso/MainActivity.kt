package com.example.expenso

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
//    var client = OkHttpClient()
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))

        val drawerLayout:DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.nav_settings -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
            }

            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }



//    fun getResponse(text : String){
//        val apiUrl = "https://api.openai.com/v1/completions"
//        val apiKey = "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"
//
//        val requestBody = """{
//            "model": "text-davinci-003",
//            "prompt": "Say this is a test",
//            "max_tokens": 500,
//            "temperature": 0
//            }""".trimMargin()
//
//        val request = Request.Builder()
//            .url(apiUrl)
//            .addHeader("Content-Type", "application/json")
//            .addHeader("Authorization", "Bearer $apiKey")
//            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
//            .build()
//
//        client.newCall(request).enqueue(object : Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                Log.e("error","API failed")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body?.string()
//
//                val jsonObject = JSONObject(body)
//                val jsonArray:JSONArray = jsonObject.getJSONArray("choices")
//                val reply = jsonArray.getJSONObject(0).getString("text")
//
//                if (reply != null) {
//                    Log.v("reply",reply)
//                }else{
//                    Log.v("reply","empty")
//                }
//
//            }
//
//
//
//        })
//    }


}

