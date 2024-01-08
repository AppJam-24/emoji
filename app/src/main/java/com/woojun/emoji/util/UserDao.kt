package com.woojun.emoji.util

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM User")
    fun getUser(): User

    @Delete
    fun deleteUser(user: User)
}