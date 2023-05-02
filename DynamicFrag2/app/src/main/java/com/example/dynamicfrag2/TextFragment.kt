package com.example.dynamicfrag2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.dynamicfrag2.databinding.FragmentTextBinding

class TextFragment : Fragment() {
    var binding: FragmentTextBinding?= null
    val data = arrayListOf<String>("ImageData1", "ImageData2", "ImageData3")
    val model:MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTextBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // requireActivity는 메인일 수도 있고, 세컨일 수도 있다
//        val i = requireActivity().intent
//        val imgNum = i.getIntExtra("imgNum", -1)
//        // 세로 모드인 경우
//        if(imgNum != -1){
//            // 프래그먼트가 세컨에 붙어있는 경우
//            binding!!.textView.text = data[imgNum]
//
//        }else{
            // 메인 액티비티가 가로모드인 액티비티에 프래그먼트가 부착 경우
            model.selectedNum.observe(viewLifecycleOwner, Observer{
                // viewmodel에서 selectedNum에 해당하는 value에 대한 타입이 it으로 넘어온다
                binding!!.textView.text = data[it]
            })
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}