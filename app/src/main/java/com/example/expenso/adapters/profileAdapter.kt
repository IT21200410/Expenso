package com.example.expenso.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.R
import com.example.expenso.database.ProfileDatabase
import com.example.expenso.database.entities.Pro

import com.example.expenso.database.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class profileAdapter: RecyclerView.Adapter<profileAdapter.VH>() {


    lateinit var data:List<Pro>
    lateinit var context: Context


    class VH(view: View):RecyclerView.ViewHolder(view)
    {
        val cbPro:CheckBox
        val ivDelete:ImageView

        init {
            cbPro = view.findViewById(R.id.checkBox)
            ivDelete = view.findViewById(R.id.imageButton)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item,parent,false)
       return VH(view)
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.cbPro.text = data[position].item
        holder.ivDelete.setOnClickListener(){

            if(holder.cbPro.isChecked){
                val repository = ProfileRepository(ProfileDatabase.getInstance(context))

                CoroutineScope(Dispatchers.IO ).launch {
                    repository.delete(data[position])
                    //
                withContext(Dispatchers.Main){
                    var data = repository.getAllPros()
                    setData(data,context)
                }
                }
            }else{
                Toast.makeText(context,"Cannot delete unchecked todo item", Toast.LENGTH_LONG ).show()
            }

        }
    }

    fun setData(data:List<Pro> , context: Context ){

        this.data = data
        this.context = context

        notifyDataSetChanged()

    }


}