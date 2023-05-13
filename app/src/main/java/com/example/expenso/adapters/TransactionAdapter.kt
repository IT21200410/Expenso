package com.example.expenso.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.EditTransaction
import com.example.expenso.R
import com.example.expenso.models.Transaction

class TransactionAdapter(private val context: Context, private val transactionList:ArrayList<Transaction>):RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>()
{

    class TransactionViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val tv1: TextView = itemView.findViewById(R.id.rec_Title)
        val tv2: TextView = itemView.findViewById(R.id.recLang)
        val tv3: TextView = itemView.findViewById(R.id.recDesc)
        val card: CardView = itemView.findViewById(R.id.recCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        val amount = transaction.amount.toString()
        holder.tv1.text = transaction.expenseType
        holder.tv2.text = "$${amount}"

        if (transaction.transactionType == "Expense")
        {
            holder.tv2.setTextColor(Color.RED)
        }
        else
        {
            holder.tv2.setTextColor(Color.GREEN)
        }

        holder.tv3.text = transaction.date

        holder.card.setOnClickListener {
            val intent = Intent(context,EditTransaction::class.java)
            intent.putExtra("transaction", transaction)
            context.startActivity(intent)

            if(context is Activity)
            {
                context.finish()
            }
        }
    }

}

