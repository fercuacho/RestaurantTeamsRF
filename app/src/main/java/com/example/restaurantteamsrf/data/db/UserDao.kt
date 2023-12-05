package com.example.restaurantteamsrf.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.util.Constants

@Dao
interface UserDao {

    // Create
    @Insert
    suspend fun insertUser(userEntity: UserEntity)

    // Update
    @Update
    suspend fun updateUser(userEntity: UserEntity)

    // Delete
    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    // Read
    @Query("SELECT * FROM ${Constants.DATABASE_USERS_TABLE} WHERE user_email = :email AND user_password = :password")
    fun getCurrentUser(email: String, password: String): UserEntity?

    @Query("SELECT * FROM ${Constants.DATABASE_USERS_TABLE} WHERE user_identifier = :identificador")
    fun getCurrentUser(identificador: String): UserEntity?

}
