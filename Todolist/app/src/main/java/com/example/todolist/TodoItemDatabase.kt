package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TodoItem::class],
    version = 1
)
abstract class TodoItemDatabase : RoomDatabase() {
    abstract fun todoDao():TodoDAO

    // database는 싱글톤 형태로 사용
    companion object{
        private var INSTANCE: TodoItemDatabase?= null

        fun getDatabase(context: Context) : TodoItemDatabase{
            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }
            // database 생성
            val instance = Room.databaseBuilder(
                context,
                TodoItemDatabase::class.java,
                "Tododb"
            ).build()

            INSTANCE = instance
            return instance
        }
    }
}