package com.example.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "Tododb")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) var id:Long,
    @ColumnInfo(name = "dateTime") var dateTime: String,
    @ColumnInfo(name = "content") var content:String,
    @ColumnInfo(name = "done") var done: Boolean = false,
    @ColumnInfo(name = "priority") var priority: Int,
)