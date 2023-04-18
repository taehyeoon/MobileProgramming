package com.example.runningrecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runningrecord.databinding.RowBinding

class RunDataAdapter (val items : ArrayList<RunData>)
    : RecyclerView.Adapter<RunDataAdapter.ViewHolder>(){

    var itemClickListener:OnItemClickListener ?= null

    interface OnItemClickListener{
        fun OnItemClick(data:RunData, pos:Int)
    }

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.imageView.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var levelImgId = when(items[position].level) {
            1 -> R.drawable.run_very_slow
            2 -> R.drawable.run_slow
            3 -> R.drawable.run_normal
            4 -> R.drawable.run_fast
            5 -> R.drawable.run_very_fast
            else -> R.drawable.run_fast
        }
        var weatherImgId = when(items[position].weather){
            1 -> R.drawable.weather_sun
            2 -> R.drawable.weather_cloud
            3 -> R.drawable.weather_fog
            4 -> R.drawable.weather_rain
            5 -> R.drawable.weather_night
            else -> R.drawable.weather_sun
        }

        holder.binding.date.text = items[position].date
        holder.binding.startTime.text = items[position].startTime
        holder.binding.endTime.text = items[position].endTime
        holder.binding.place.text = items[position].place
        holder.binding.distance.text = items[position].distance.toString()
        holder.binding.level.setImageResource(levelImgId)
        holder.binding.weather.setImageResource(weatherImgId)
        holder.binding.calories.text = (items[position].distance * MainActivity.coefficantCalories).toString()

        if(items[position].isShawn){
            holder.binding.caloryLayout.visibility = View.VISIBLE
        }else{
            holder.binding.caloryLayout.visibility = View.GONE
        }
    }

    fun moveItem(oldPos: Int, newPos: Int) {
        val itemVal = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, itemVal)

        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int) {
        // item 리스트에서 pos에 해당하는 값을 삭제 시켜주면 된다
        items.removeAt(pos)
        // pos에 해당하는 아이템이 삭제되었다는 것만 알려주고, 나머지는 자동으로 수행하게 맡긴다
        notifyItemRemoved(pos)
    }
}