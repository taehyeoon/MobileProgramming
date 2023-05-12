package com.example.roomdb

import androidx.room.*

@Dao
interface ProductDAO {

    // 동일한 id가 들어갈 수 없는데 insert를 시도하는 경우 -> ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Query("Select * from products")
    fun getAllRecord(): List<Product>

    @Query("Select * from products where pname = :name")
    fun findProduct(name:String) : List<Product>

    @Query("Select * from products where pname like :name")
    fun findProduct2(name: String) : List<Product>
}