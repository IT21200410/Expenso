package com.example.expenso.data

import CompletionResponse
import com.example.expenso.API_KEY.MY_API_KEY
import com.example.expenso.model.CompletionRequest
//import com.example.expenso.model.CompletionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {
    @Headers("Authorization: Bearer $MY_API_KEY")
    @POST("v1/completions")
    suspend fun getCompletions(@Body completionResponse: CompletionRequest) : Response<CompletionResponse>
}