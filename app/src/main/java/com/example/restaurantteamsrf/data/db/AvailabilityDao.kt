package com.example.restaurantteamsrf.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.restaurantteamsrf.data.db.model.AvailabilityEntity
import com.example.restaurantteamsrf.util.Constants

@Dao
interface AvailabilityDao {

        // Create
        @Insert(/*onConflict = OnConflictStrategy.REPLACE*/)
        suspend fun insertAvailability(availabilityEntity: AvailabilityEntity)

        // Update
        @Update
        suspend fun updateAvailability(availabilityEntity: AvailabilityEntity)

        // Delete
        @Delete
        suspend fun deleteAvailability(availabilityEntity: AvailabilityEntity)

        // Read
        @Query("SELECT * FROM ${Constants.DATABASE_AVAILABILITY_TABLE} WHERE availability_id = :availability_id")
        suspend fun getAvailability(availability_id: Long): AvailabilityEntity


    }

