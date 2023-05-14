package com.example.todolist

import androidx.room.*
import java.time.LocalDateTime

@Dao
interface TodoDAO {

    // 동일한 id가 들어갈 수 없는데 insert를 시도하는 경우 -> ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTodoItem(todoitem: TodoItem)

    @Delete
    fun deleteTodoItem(todoitem: TodoItem)

    @Update
    fun updateTodoItem(todoitem: TodoItem)

    @Query("Select * from Tododb")
    fun getAllRecord(): List<TodoItem>

    @Query("Delete from Tododb")
    fun deleteAllRecord()

//    @Query("Select * from Tododb where dateTime = :dateTime")
//    fun findTodoItem(dateTime: LocalDateTime) : List<TodoItem>

//    @Query("Select * from products where pname like :name")
//    fun findProduct2(name: String) : List<TodoItem>
}