package com.example.expenso.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.R
import com.example.expenso.models.Expenses


class Adapter {


    class ExAdapter(private val expenseList: ArrayList<Expenses>) : RecyclerView.Adapter<ExAdapter.MyViewHolder>(){
        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val tvExpensesName: TextView = itemView.findViewById(R.id.profilename)
            val tvDetails: TextView = itemView.findViewById(R.id.detail)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.list_expenses,parent,false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return expenseList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tvExpensesName.text = expenseList[position].name
            holder.tvDetails.text = expenseList[position].details
        }
    }
}