package com.example.pbpkel4_tubes.user

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addNote(note: User)
    @Update
    suspend fun updateNote(note: User)
    @Delete
    suspend fun deleteNote(note: User)
    @Query("SELECT * FROM user")
    suspend fun getNotes() : List<User>
    @Query("SELECT * FROM user WHERE id =:username_id")
    suspend fun getNote(username_id: Int) : List<User>
}