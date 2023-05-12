package com.example.dbapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView

class MyDBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME = "mydb.db"
        val DB_VERSION = 1
        val TABLE_NAME = "products"
        val PID = "pid"
        val PNAME = "pname"
        val PQUANTITIY = "pquantity"
    }

    // DB가 처음에 생성될 때 호출
    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME(" +
                "$PID integer primary key autoincrement, "+
                "$PNAME text, " +
                "$PQUANTITIY integer);"
        db!!.execSQL(create_table)
    }

    // DB의 버전이 변경될 때 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }


    fun getAllRecord(){
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        showRecord(cursor)
        cursor.close()
        db.close()
    }

    private fun showRecord(cursor: Cursor) {
        cursor.moveToFirst() //커서를 처음 위치로 이동
        val attrCount = cursor.columnCount
        val activity = context as MainActivity
        // 기존 테이블 삭제
        activity.binding.tableLayout.removeAllViewsInLayout()


        // 타이틀 만들기
        val tableRow = TableRow(activity)
        val rowParam = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        tableRow.layoutParams = rowParam
        // 테이블의 각 row에 들어가는 view의 파라미터 의미
        val viewParam = TableRow.LayoutParams(0, 100, 1f)


        for(i in 0 until attrCount){
            val textView = TextView(activity)
            textView.layoutParams = viewParam
            textView.text = cursor.getColumnName(i)
            textView.setBackgroundColor(Color.LTGRAY)
            textView.textSize = 15.0f
            textView.gravity = Gravity.CENTER
            tableRow.addView(textView)
        }
        activity.binding.tableLayout.addView(tableRow)

        // 레코드가 하나도 없는 경우, 즉시 종료
        if(cursor.count == 0) return
        // 레코드 추가하기
        do{
            val row = TableRow(activity)
            row.layoutParams = rowParam

            // row를 클릭했을 때 이벤트 발생
            row.setOnClickListener {
                for(i in 0 until attrCount){
                    val textView = row.getChildAt(i) as TextView
                    when(textView.tag){
                        0->activity.binding.pIdEdit.setText(textView.text)
                        1->activity.binding.pNameEdit.setText(textView.text)
                        2->activity.binding.pQuantitiyEdit.setText(textView.text)
                    }
                }
            }

            for(i in 0 until attrCount){
                val textView = TextView(activity)
                textView.tag = i
                textView.layoutParams = viewParam
                textView.text = cursor.getString(i)
                textView.textSize = 13.0f
                textView.gravity = Gravity.CENTER
                row.addView(textView)
            }
            activity.binding.tableLayout.addView(row)
        }while (cursor.moveToNext())
    }







    fun insertProduct(product: Product):Boolean{
        val values = ContentValues()
        values.put(PNAME, product.pName)
        values.put(PQUANTITIY, product.pQuantity)
        val db = writableDatabase

        // insert가 오류가 나는경우 -1을 리턴함
        val flag = db.insert(TABLE_NAME, null, values) > 0
        db.close()
        return flag
    }



    // select * from product where name = 'pname';
    fun findProduct(name: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $PNAME = '$name';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count != 0

        showRecord(cursor)
        cursor.close()
        db.close()

        return flag
    }

    // select * from product where pname like '김%';
    fun findProduct2(name: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $PNAME like '$name%';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count != 0

        showRecord(cursor)
        cursor.close()
        db.close()

        return flag
    }


    // select * from product where name = 'pid';
    fun deleteProduct(pid: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $PID = '$pid';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count != 0

        if(flag){
            cursor.moveToFirst()
            // 아이디가 일치하는 객체 1개만 삭제, id는 중복이 안되기 때문에 문제없음
            db.delete(TABLE_NAME, "$PID =?", arrayOf(pid)) // ?로 인자를 나타낸다 값은 다음 인자인 array에 있음
        }
        cursor.close()
        db.close()

        return flag
    }


    fun updateProduct(product: Product): Boolean {
        val pid = product.pid
        val strsql = "select * from $TABLE_NAME where $PID = '$pid';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count != 0

        if(flag){
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(PNAME, product.pName)
            values.put(PQUANTITIY, product.pQuantity)
            db.update(TABLE_NAME, values, "$PID=?", arrayOf(pid.toString()))
        }
        cursor.close()
        db.close()

        return flag
    }


}














