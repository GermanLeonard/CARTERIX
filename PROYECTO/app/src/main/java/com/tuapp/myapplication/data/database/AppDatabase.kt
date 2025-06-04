package com.tuapp.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuapp.myapplication.data.dao.CategoriaEgresoDao
import com.tuapp.myapplication.data.dao.SubcategoriaDao
import com.tuapp.myapplication.data.dao.IngresoDao
import com.tuapp.myapplication.data.models.CategoriaEgreso
import com.tuapp.myapplication.data.models.Subcategoria
import com.tuapp.myapplication.data.models.Ingreso

@Database(
    entities = [CategoriaEgreso::class, Subcategoria::class, Ingreso::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriaEgresoDao(): CategoriaEgresoDao
    abstract fun subcategoriaDao(): SubcategoriaDao
    abstract fun ingresoDao(): IngresoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "carterix_db"
                ).fallbackToDestructiveMigration() // ← para evitar errores si cambia la versión
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

