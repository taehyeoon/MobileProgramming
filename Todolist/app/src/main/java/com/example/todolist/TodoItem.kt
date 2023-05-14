package com.example.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "Tododb")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) var id:Long,
//    @ColumnInfo(name = "dateTime") var dateTime: LocalDateTime,
    @ColumnInfo(name = "year") var year :Int,
    @ColumnInfo(name = "month") var month :Int,
    @ColumnInfo(name = "day") var day :Int,
    @ColumnInfo(name = "content") var content:String,
    @ColumnInfo(name = "done") var done: Boolean = false,
    @ColumnInfo(name = "priority") var priority: Int,
)