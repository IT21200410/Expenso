package com.example.expenso.api

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class chatAPI {

    var client = OkHttpClient()
    val apiUrl = "https://api.openai.com/v1/completions"
    val apiKey = "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"
    fun getResponse(text : String){


        val requestBody = """{
            "model": "text-davinci-003",
            "prompt": "$text",
            "max_tokens": 500,
            "temperature": 0
            }""".trimMargin()

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error","API failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val jsonObject = JSONObject(body)
                val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
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