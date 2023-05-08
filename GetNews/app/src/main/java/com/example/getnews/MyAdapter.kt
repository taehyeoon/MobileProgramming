package com.example.getnews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.getnews.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>)
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>()
{
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }

    inner class MyViewHolder(val binding : RowBinding) :RecyclerView.ViewHolder(binding.root){
        init {
            binding.newstitle.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    var itemClickListener:OnItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.newstitle.text = items[position].newstitle
    }
}