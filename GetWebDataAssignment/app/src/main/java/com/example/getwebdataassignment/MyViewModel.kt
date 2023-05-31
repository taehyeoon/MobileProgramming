package com.example.getwebdataassignment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel() {

    val posterSrc = MutableLiveData<String>()
    fun setLiveData(imgSrc:String){
        posterSrc.value = imgSrc
    }

}