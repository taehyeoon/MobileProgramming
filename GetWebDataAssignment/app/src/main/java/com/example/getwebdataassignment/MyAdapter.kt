package com.example.getwebdataassignment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getwebdataassignment.databinding.RowBinding

class MyAdapter (val items : ArrayList<MovieData>)
    : RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    var itemClickListener:OnItemClickListener ?= null

    interface OnItemClickListener{
        fun OnItemClick(data:MovieData, pos:Int)
    }

    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.binding.title.text = items[position].title
        holder.binding.openingDate.text = items[position].openingDate
        Log.d("in adapter", items[position].title + "   " + items[position].openingDate )
    }

    override fun getItemCount(): Int {
        return items.size
    }
}