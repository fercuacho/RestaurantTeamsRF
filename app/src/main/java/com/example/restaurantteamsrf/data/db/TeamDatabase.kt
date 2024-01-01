package com.example.restaurantteamsrf.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.restaurantteamsrf.converters.Converters
import com.example.restaurantteamsrf.data.db.model.AvailabilityEntity
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.util.Constants


@Database(
    entities = [TeamEntity::class, UserEntity::class, AvailabilityEntity::class],
    version = 15, //version de la DB. Importante para las migraciones
    exportSchema = true //por defecto es true
)
@TypeConverters(Converters::class) // Agrega la anotación @TypeConverters y la clase Converters

abstract class TeamDatabase: RoomDatabase() {

    abstract fun teamDao(): TeamDao
    abstract fun userDao(): UserDao
    abstract fun availabilityDao(): AvailabilityDao



    //Sin inyección de dependencias, metemos la creación de la bd con un singleton aquí
    companion object{
        @Volatile //lo que se escriba en este campo, será inmediatamente visible a otros hilos
        private var INSTANCE: TeamDatabase? = null

        fun getDatabase(context: Context): TeamDatabase{
            return INSTANCE?: synchronized(this){
                //Si la instancia no es nula, entonces se regresa
                // si es nula, entonces se crea la base de datos (patrón singleton)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TeamDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration() //Permite a Room recrear las tablas de la BD si las migraciones no se encuentran
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

}