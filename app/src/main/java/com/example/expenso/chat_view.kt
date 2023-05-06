package com.example.expenso

import CompletionResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenso.data.ApiClient
import com.example.expenso.models.CompletionRequest
import com.example.expenso.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class chat_view : ViewModel(){
    val messageList = MutableLiveData<MutableList <Message> >()
    val mList : LiveData<MutableList<Message>> get() = messageList

    init {
        messageList.value = mutableListOf()
    }
    fun addToChat(message:String , sendBy : String , timestamp : String ){
        val currentList = messageList.value ?: mutableListOf()
        Log.v("msg",currentList.toString())
        currentList.add(Message(message,sendBy))
        messageList.postValue(currentList)
    }

    private fun addResponse(response : String){
        messageList.value?.removeAt(messageList.value?.size?.minus(1) ?: 0)
        addToChat(response, Message.BOT,getCurrentTimestamp())
    }

    fun callApi(question : String){
        addToChat("Typing....", Message.BOT,getCurrentTimestamp())

        val completionRequest = CompletionRequest(
            model = "text-davinci-003",
            prompt = question,
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCompletions(completionRequest)
                handleApiResponse(response)
            }catch (e : SocketTimeoutException){
                addResponse("Timeout : $e")
            }
        }
    }

    suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main){
            if (response.isSuccessful){
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text
                    if (result != null){
                        addResponse(result.trim())
                    }else{
                        addResponse("try again")
                    }
                }
            }else{
                addResponse("Failed to get response ${response.errorBody()}")
            }
        }

    }

    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh mm a", Locale.getDefault()).format(Date())
    }

}