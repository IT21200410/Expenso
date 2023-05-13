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
import com.example.expenso.addExpenses
import com.example.expenso.models.ExpensesType

class ExpensesAdapter(private val context: Context, private val expensesList:ArrayList<ExpensesType>):RecyclerView.Adapter<ExpensesAdapter.VH>()
{

    class VH(view: View):RecyclerView.ViewHolder(view)
    {
        val cbTodo:CheckBox
        val ivDelete:ImageView
        val ivEdit:ImageView

        init {
            cbTodo = view.findViewById(R.id.checkBox)
            ivDelete = view.findViewById(R.id.imageButton)
            ivEdit = view.findViewById(R.id.imageBu)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item,parent,false)
       return VH(view)
    }

    override fun getItemCount(): Int {
        return expensesList.size;
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.cbTodo.text = expensesList[position].expensesName
        holder.ivDelete.setOnClickListener{

            if(holder.cbTodo.isChecked){
            if (context is addExpenses){
                context.deleteExpenseType(expensesList[position])
            }
            }
        else
            {
                Toast.makeText(context,"Cannot delete unchecked expenses item", Toast.LENGTH_LONG ).show()
            }
            holder.ivEdit.setOnClickListener{
                if (context is addExpenses){
                    context.updateDialog(expensesList[position])
                }
            }

        }
    }

//    fun setData(data:List<Todo> , context: Context ){
//
//        this.expensesList = data
//
//
//        notifyDataSetChanged()
//
//    }


}