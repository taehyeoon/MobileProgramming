package com.example.getwebdataassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.getwebdataassignment.databinding.FragmentPosterBinding

class PosterFragment : Fragment() {

    lateinit var binding: FragmentPosterBinding
    val model: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPosterBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

}