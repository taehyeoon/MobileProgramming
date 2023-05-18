package com.example.firebasedbapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasedbapp.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MyProductAdapter
    lateinit var rdb:DatabaseReference
    var findQuery = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rdb = Firebase.database.getReference("products/items")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()

        adapter = MyProductAdapter(option)
        adapter.itemClickListener = object :MyProductAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                binding.apply {
                    pIdEdit.setText(adapter.getItem(position).pId.toString())
                    pNameEdit.setText(adapter.getItem(position).pName)
                    pQuantitiyEdit.setText(adapter.getItem(position).pQuantity.toString())
                }
            }
        }

        // Set Buttons features
        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter


            // INSERT
            insertBtn.setOnClickListener {
                initQuery()
                val item = Product(pIdEdit.text.toString().toInt(),
                    pNameEdit.text.toString(),
                    pQuantitiyEdit.text.toString().toInt())
                rdb.child(pIdEdit.text.toString()).setValue(item)
                clearInput();
            }

            // FIND
            findBtn.setOnClickListener {
                if(!findQuery)
                    findQuery = true

                if(adapter != null){
                    // 이전에 수행했었던 것에 대해서 동기화 중지
                    adapter.stopListening()
                }

                // 새로운 쿼리문 작성
                val query = rdb.orderByChild("pname").equalTo(pNameEdit.text.toString())
                val option = FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(query, Product::class.java)
                    .build()

                adapter = MyProductAdapter(option)
                adapter.itemClickListener = object :MyProductAdapter.OnItemClickListener{
                    override fun OnItemClick(position: Int) {
                        binding.apply {
                            pIdEdit.setText(adapter.getItem(position).pId.toString())
                            pNameEdit.setText(adapter.getItem(position).pName)
                            pQuantitiyEdit.setText(adapter.getItem(position).pQuantity.toString())
                        }
                    }
                }
                recyclerView.adapter = adapter
                adapter.startListening()
            }

            // DELETE
            deleteBtn.setOnClickListener {
                initQuery()
                rdb.child(pIdEdit.text.toString()).removeValue()
                clearInput()
            }

            // UPDATE
            updateBtn.setOnClickListener {
                initQuery()
                rdb.child(pIdEdit.text.toString())
                    .child("pquantity")
                    .setValue(pQuantitiyEdit.text.toString().toInt())
                clearInput()
            }
        }
    }

    fun initQuery(){
        // 이전에 실행된 쿼리가 FIND라면 쿼리를 "rdb.limitToLast(50)"로 초기화함
        if(findQuery) {
            findQuery = false

            if (adapter != null) {
                // 이전에 수행했었던 것에 대해서 동기화 중지
                adapter.stopListening()
            }

            // 새로운 쿼리문 작성
            val query = rdb.limitToLast(50)
            val option = FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product::class.java)
                .build()

            adapter = MyProductAdapter(option)
            adapter.itemClickListener = object : MyProductAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    binding.apply {
                        pIdEdit.setText(adapter.getItem(position).pId.toString())
                        pNameEdit.setText(adapter.getItem(position).pName)
                        pQuantitiyEdit.setText(adapter.getItem(position).pQuantity.toString())
                    }
                }
            }
            binding.recyclerView.adapter = adapter
            adapter.startListening()

        }
    }

    fun clearInput(){
        binding.apply {
            pIdEdit.text.clear()
            pNameEdit.text.clear()
            pQuantitiyEdit.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        // 질의문(option)에 해당되는 것이 자동으로 수행이 된다.
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}