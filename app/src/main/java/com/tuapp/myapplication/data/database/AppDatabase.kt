package com.tuapp.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuapp.myapplication.data.database.dao.category.CategoriaEgresoDao
import com.tuapp.myapplication.data.database.dao.SubcategoriaDao
import com.tuapp.myapplication.data.database.dao.IngresoDao
import com.tuapp.myapplication.data.database.dao.finance.CategorieDataDao
import com.tuapp.myapplication.data.database.dao.finance.FinanceSummaryDao
import com.tuapp.myapplication.data.database.dao.subCategory.ExpensesTypeDao
import com.tuapp.myapplication.data.database.dao.subCategory.SubCategoryDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.finance.CategorieDataEntity
import com.tuapp.myapplication.data.database.entities.finance.FinanceSummaryEntity
import com.tuapp.myapplication.data.database.entities.user.UserEntity
import com.tuapp.myapplication.data.database.entities.category.CategoriaEgresoEntity
import com.tuapp.myapplication.data.database.entities.subCategory.ExpensesTypesEntity
import com.tuapp.myapplication.data.database.entities.subCategory.SubCategoriaEgresoEntity
import com.tuapp.myapplication.data.models.Subcategoria
import com.tuapp.myapplication.data.models.Ingreso

@Database(
    entities = [
        Ingreso::class,
        UserEntity::class,
        FinanceSummaryEntity::class,
        CategorieDataEntity::class,
        CategoriaEgresoEntity::class,
        SubCategoriaEgresoEntity::class,
        ExpensesTypesEntity::class,
               ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    //User
    abstract fun userDao(): UserDao

    //Finanza
    abstract fun resumenFinancieroDao(): FinanceSummaryDao
    abstract fun categoriaDataDao(): CategorieDataDao

    //Tipos
    abstract fun tipoGastosDao(): ExpensesTypeDao

    //BD
    abstract fun categoriaEgresoDao(): CategoriaEgresoDao
    abstract fun subcategoriaDao(): SubCategoryDao
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
                    .also{
                        INSTANCE = it
                    }
                instance
            }
        }
    }
}

