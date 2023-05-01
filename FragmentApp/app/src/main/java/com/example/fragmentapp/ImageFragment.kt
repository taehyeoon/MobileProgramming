package com.example.fragmentapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.fragmentapp.databinding.FragmentImageBinding as FragmentImageBinding1

class ImageFragment : Fragment() {

    var binding : FragmentImageBinding1?= null
    val model:MyViewModel by activityViewModels()
    val imgList = arrayListOf<Int>(R.drawable.cat, R.drawable.dog, R.drawable.racoon)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding1.inflate(layoutInflater,container, false)
//        return inflater.inflate(R.layout.fragment_image, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // onCreateView 함수 이후에 실행됨
        binding!!.apply {

            imageView.setOnClickListener {
                if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                    // activity는 현재 프래그먼트가 부탁되어 있는 액티비티를 의미
                    val intent = Intent(activity, SecondActivity::class.java)
                    intent.putExtra("imgNum", model.selectedNum.value)
                    startActivity(intent)
                }
            }


            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.radioButton1 ->{
                        model.setLiveData(0)
                    }
                    R.id.radioButton2 ->{
                        model.setLiveData(1)

                    }
                    R.id.radioButton3 ->{
                        model.setLiveData(2)

                    }
                }
                imageView.setImageResource(imgList[model.selectedNum.value!!])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*
        fragmemt 의 바인딩과 view의 바인딩은 라이프사이클이 다르다
        view는 사라져도 이 바인딩 객체가 계속 view를 참조하고 있으면, 바인딩이 널이 아니기 때문에
        가비지 컬렉터가 메모리를 정리하지 않는다 > 따라서 메모리 누수가 발생한다
        이를 막기 위해서는 뷰가 삭제되면 참조하고 있었던 바인딩 값을 null로 수정하야 한다
         */
        binding = null
    }

}