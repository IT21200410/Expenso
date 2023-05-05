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
import com.example.expenso.database.TodoDatabase
import com.example.expenso.database.entities.Todo
import com.example.expenso.database.repositories.ExpensesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpensesAdapter: RecyclerView.Adapter<ExpensesAdapter.VH>() {


    lateinit var data:List<Todo>
    lateinit var context: Context


    class VH(view: View):RecyclerView.ViewHolder(view)
    {
        val cbTodo:CheckBox
        val ivDelete:ImageView

        init {
            cbTodo = view.findViewById(R.id.checkBox)
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
        holder.cbTodo.text = data[position].item
        holder.ivDelete.setOnClickListener(){

            if(holder.cbTodo.isChecked){
                val repository = ExpensesRepository(TodoDatabase.getInstance(context))

                CoroutineScope(Dispatchers.IO ).launch {
                    repository.delete(data[position])
                    //
                withContext(Dispatchers.Main){
                    var data = repository.getAllTodos()
                    setData(data,context)
                }
                }
            }else{
                Toast.makeText(context,"Cannot delete unchecked todo item", Toast.LENGTH_LONG ).show()
            }

        }
    }

    fun setData(data:List<Todo> , context: Context ){

        this.data = data
        this.context = context

        notifyDataSetChanged()

    }


}