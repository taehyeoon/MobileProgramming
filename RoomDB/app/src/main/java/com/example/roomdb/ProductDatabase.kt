package com.example.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Product::class],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao():ProductDAO

    // database는 싱글톤 형태로 사용
    companion object{
        private var INSTANCE: ProductDatabase?= null

        fun getDatabase(context: Context) : ProductDatabase{
            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }
            // database 생성
            val instance = Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                "productdb"
            ).build()

            INSTANCE = instance
            return instance
        }
    }
}