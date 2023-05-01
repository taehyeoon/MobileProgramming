package com.example.showapplist

import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showapplist.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:MyData)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding:RowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // row.xml을 나타내는 것으로, 한 행을 터치하면 해당 리스너를 실행시키는 의
            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply{
            applabel.text = items[position].applabel
            appclass.text = items[position].appclass
            imageView.setImageDrawable(items[position].appicon)
        }
    }

    override fun getItemCount(): Int {
        return items.size;
    }
}