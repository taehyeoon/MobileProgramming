package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TodoRowBinding

class TodoAdapter(var items:ArrayList<TodoItem>)
    : RecyclerView.Adapter<TodoAdapter.ViewHolder>()
{
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }

    inner class ViewHolder(val binding : TodoRowBinding) :RecyclerView.ViewHolder(binding.root){
        init {
            binding.todoRowLayout.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    var itemClickListener:OnItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TodoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.contentText.text = items[position].content
        holder.binding.dateTimeText.text =
            "${items[position].year}/ ${(items[position].month + 1)}/ ${items[position].day}"
        holder.binding.priorityText.text = items[position].priority.toString()
    }
}