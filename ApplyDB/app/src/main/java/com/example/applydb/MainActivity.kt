package com.example.applydb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.applydb.databinding.ActivityMainBinding
import com.example.dbapp.MyDBHelper
import com.example.dbapp.Product
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var myDBHelper : MyDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDB()
        init()
        getAllRecord()
    }



    fun getAllRecord(){
        myDBHelper.getAllRecord()
    }

    fun clearEditText(){
        binding.apply {
            pIdEdit.text.clear()
            pNameEdit.text.clear()
            pQuantitiyEdit.text.clear()
        }
    }


    private fun initDB() {
        val dbfile = getDatabasePath("mydb.db")

        // database라는 폴더 자체가 없는 경우
        if(!dbfile.parentFile.exists()){
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            // DB를 읽어온다
            val file = resources.openRawResource(R.raw.mydb)
            // DB의 전체 바이트 수를 측정
            val fileSize = file.available()
            // DB크기 만큼의 배열 생성
            val buffer = ByteArray(fileSize)
            // file을 읽어서 buffer에 저장
            file.read(buffer)
            file.close()
            // dbfile이 없는 경우 새로 생성
            dbfile.createNewFile()
            // dbfile에 쓸 output 객체 생성
            val output = FileOutputStream(dbfile)
            // buffer의 내용을 dbfile에 출력
            output.write(buffer)
            output.close()
        }
    }


    private fun init(){
        myDBHelper = MyDBHelper(this)

        binding.apply {

            testsql.addTextChangedListener {
                val pname = it.toString()
                val result = myDBHelper.findProduct2(pname)
            }




            insertBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val quantity = pQuantitiyEdit.text.toString().toInt()
                val product = Product(0, name, quantity)
                val result = myDBHelper.insertProduct(product)
                if(result){
                    getAllRecord()
                    Toast.makeText(this@MainActivity, "Data insert success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Data insert failed", Toast.LENGTH_SHORT).show()
                }
                clearEditText()
            }


            findBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val result = myDBHelper.findProduct(name)
                if(result){
                    Toast.makeText(this@MainActivity, "RECORD FOUND", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "NO MATCH FOUND", Toast.LENGTH_SHORT).show()
                }
            }



            deleteBtn.setOnClickListener {
                val pid = pIdEdit.text.toString()
                val result = myDBHelper.deleteProduct(pid)
                if(result){
                    Toast.makeText(this@MainActivity, "Data delete success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Data delete failed", Toast.LENGTH_SHORT).show()
                }
                getAllRecord()
                clearEditText()
            }



            updateBtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantitiyEdit.text.toString().toInt()
                val product = Product(pid, name, quantity)
                val result = myDBHelper.updateProduct(product)
                if(result){
                    getAllRecord()
                    Toast.makeText(this@MainActivity, "Data update success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Data update failed", Toast.LENGTH_SHORT).show()
                }
                clearEditText()

            }
        }
    }
}