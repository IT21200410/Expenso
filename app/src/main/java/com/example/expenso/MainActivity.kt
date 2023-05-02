package com.example.expenso

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat)

    }


    fun getResponse(text : String){
        val apiUrl = "https://api.openai.com/v1/completions"
        val apiKey = "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"

        val requestBody = """{
            "model": "text-davinci-003",
            "prompt": "Say this is a test",
            "max_tokens": 500,
            "temperature": 0
            }""".trimMargin()

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error","API failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val jsonObject = JSONObject(body)
                val jsonArray:JSONArray = jsonObject.getJSONArray("choices")
                val reply = jsonArray.getJSONObject(0).getString("text")

                if (reply != null) {
                    Log.v("reply",reply)
                }else{
                    Log.v("reply","empty")
                }

            }



        })
    }


}

