package com.openplaytech.openplay.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDbTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertModel(movieDbTable: MovieDbTable)

    @Query("select * from MovieDbTable order by dateAdded DESC")
    fun loadAll(): List<MovieDbTable>

}