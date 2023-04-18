package com.example.prepareformidterm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepareformidterm.databinding.RowBinding

class EmployeeDataAdapter(val items:ArrayList<EmployeeData>) :RecyclerView.Adapter<EmployeeDataAdapter.ViewHolder>() {

    var itemClickListener:OnItemClickListener ?= null

    interface OnItemClickListener{
        fun OnitemClick(data : EmployeeData, pos:Int, binding: RowBinding)
        fun onImageClick(data : EmployeeData, pos:Int, binding: RowBinding)
    }

    inner class ViewHolder(val binding : RowBinding) : RecyclerView.ViewHolder(binding.root) {
        init{
            binding.name.setOnClickListener {
                itemClickListener?.OnitemClick(items[adapterPosition],adapterPosition,binding)
            }

            binding.imageView.setOnClickListener {
                itemClickListener?.onImageClick(items[adapterPosition],adapterPosition,binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = items[position].name
        holder.binding.company.text = items[position].company
        holder.binding.phoneNumber.text = items[position].phoneNumber
        holder.binding.imageView.setImageResource(R.drawable.person_emoji)
        holder.binding.callNumber.text = items[position].callNumber.toString()
    }
}