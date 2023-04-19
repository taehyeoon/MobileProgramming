package com.example.thleemidtest3209

import android.os.Debug
import android.util.DebugUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.thleemidtest3209.databinding.OrderrowBinding

class MyDataAdapter(val items : ArrayList<MyData>)
    : RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {

    var itemClickListener:OnItemClickListener ?= null

    interface OnItemClickListener{
        fun OnItemClick(data: MyData, position:Int, binding: OrderrowBinding) // 인자 설계를 다양하게 할 수 있음
    }

    inner class ViewHolder(val binding : OrderrowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.orderview.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition, binding) // 현재 클릭한 위치 정보 : adapterposition
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = OrderrowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.productname.text = items[position].name
        holder.binding.orderview.text = items[position].num.toString()
        holder.binding.productprice.text = items[position].price.toString()
    }

    fun removeitem(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }
}