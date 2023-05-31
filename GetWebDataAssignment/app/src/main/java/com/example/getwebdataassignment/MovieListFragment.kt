package com.example.getwebdataassignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getwebdataassignment.databinding.FragmentMovieListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MovieListFragment : Fragment() {

    var binding : FragmentMovieListBinding ?= null
    val model : MyViewModel by activityViewModels()

    lateinit var adapter : MyAdapter


    val movieurl = "http://www.cgv.co.kr/movies/?lt=1&ft=0"
    val scope = CoroutineScope(Dispatchers.IO)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieListBinding.inflate(layoutInflater)
        if(binding != null) Log.d("binding", "not null")
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
//
//        return binding!!.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }


    private fun initRecyclerView() {
        val temp: ArrayList<MovieData> = ArrayList()
        temp.add(MovieData("aa", "11", "qa"))
        temp.add(MovieData("aa", "11", "qa"))
        temp.add(MovieData("aa", "11", "qa"))
        temp.add(MovieData("aa", "11", "qa"))

        binding?.recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = MyAdapter(temp)
        Log.d("data Num", temp.size.toString())
        adapter.itemClickListener = object :MyAdapter.OnItemClickListener{
            override fun OnItemClick(data: MovieData, pos: Int) {
//                val intent = Intent(activity, )
            }
        }

        binding!!.recyclerView.adapter = adapter
        if(adapter == null) Log.d("adapter", "is null")
        Log.d("adapter", "attached")
    }

}