package com.example.voca

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voca.databinding.RowBinding

class MyDataAdapter(val items : ArrayList<MyData>)
    : RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {

    var itemClickListener:OnItemClickListener ?= null

    interface OnItemClickListener{
        fun OnItemClick(data: MyData, position:Int, binding: RowBinding) // 인자 설계를 다양하게 할 수 있음
    }

    inner class ViewHolder(val binding : RowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.textView.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition, binding) // 현재 클릭한 위치 정보 : adapterposition
            }
        }
    }

    fun changeItemOpenState(pos:Int, rowBinding: RowBinding){
        items[pos].isOpened = !items[pos].isOpened
        if(items[pos].isOpened){
            rowBinding.meaning.visibility = View.VISIBLE
        }else{
            rowBinding.meaning.visibility = View.GONE
        }
    }

    fun moveItem(oldPos: Int, newPos : Int){
        val itemVal = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, itemVal)

        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos:Int){
        // item 리스트에서 pos에 해당하는 값을 삭제 시켜주면 된다
        items.removeAt(pos)
        // pos에 해당하는 아이템이 삭제되었다는 것만 알려주고, 나머지는 자동으로 수행하게 맡긴다
        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = items[position].word
        holder.binding.meaning.text = items[position].meaning
    }

    // recycle된 뷰가 나타나기 전에 호출되는 함수

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        Log.d("bye", holder.adapterPosition.toString())

        if(items[holder.adapterPosition.toInt()].isOpened)
            holder.binding.meaning.visibility = View.VISIBLE
        else
            holder.binding.meaning.visibility = View.GONE

    }
}