package com.example.expenso.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.EditReminder
import com.example.expenso.EditTransaction
import com.example.expenso.R
import com.example.expenso.models.Reminder

class ReminderAdapter(private val context: Context, private val reminderList:ArrayList<Reminder>):RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>()
{

    class ReminderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val title: TextView = itemView.findViewById(R.id.rec_Title)
        val amount: TextView = itemView.findViewById(R.id.recLang)
        val date: TextView = itemView.findViewById(R.id.recDesc)
        val card: CardView = itemView.findViewById(R.id.recCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ReminderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminderList[position]
        val amount = reminder.amount.toString()
        holder.title.text = reminder.type
        holder.amount.text = "$${amount}"
        holder.date.text = reminder.date

        holder.card.setOnClickListener {
            val intent = Intent(context, EditReminder::class.java)
            intent.putExtra("reminder", reminder)
            context.startActivity(intent)
        }
    }

}

