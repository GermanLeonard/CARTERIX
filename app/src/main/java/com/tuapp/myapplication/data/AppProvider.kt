package com.tuapp.myapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tuapp.myapplication.data.database.AppDatabase
import com.tuapp.myapplication.data.remote.RetrofitInstance
import com.tuapp.myapplication.data.repository.categories.CategoryRepository
import com.tuapp.myapplication.data.repository.categories.CategoryRepositoryImpl
import com.tuapp.myapplication.data.repository.finance.FinanceRepository
import com.tuapp.myapplication.data.repository.finance.FinanceRepositoryImpl
import com.tuapp.myapplication.data.repository.incomes.IncomesRepository
import com.tuapp.myapplication.data.repository.incomes.IncomesRepositoryImpl
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepositoryImpl
import com.tuapp.myapplication.data.repository.subCategories.SubCategoryRepository
import com.tuapp.myapplication.data.repository.subCategories.SubCategoryRepositoryImpl
import com.tuapp.myapplication.data.repository.transactions.TransactionsRepository
import com.tuapp.myapplication.data.repository.transactions.TransactionsRepositoryImpl
import com.tuapp.myapplication.data.repository.user.UserRepository
import com.tuapp.myapplication.data.repository.user.UserRepositoryImpl

private const val SENSITIVE_INFO_NAME = "sensitive_info"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SENSITIVE_INFO_NAME)

class AppProvider(context: Context) {

    private val appDatabase = AppDatabase.getDatabase(context)

    private val sensitiveInfoRepository: SensitiveInfoRepository = SensitiveInfoRepositoryImpl(context.dataStore)

    private val userDao = appDatabase.userDao()
    private val userService = RetrofitInstance.getUserService(sensitiveInfoRepository)
    private val userRepository = UserRepositoryImpl(userService, userDao, sensitiveInfoRepository )

    private val financeSummaryDao = appDatabase.resumenFinancieroDao()
    private val financeDataDao = appDatabase.categoriaDataDao()
    private val financeService = RetrofitInstance.getFinanceService(sensitiveInfoRepository)
    private val financeRepository = FinanceRepositoryImpl(financeService, userDao, financeSummaryDao, financeDataDao)

    private val categoryService = RetrofitInstance.getCategoryService(sensitiveInfoRepository)
    private val categoryDao = appDatabase.categoriaEgresoDao()
    private val categoryRepository = CategoryRepositoryImpl(categoryService,categoryDao,userDao)

    private val subCategoryService = RetrofitInstance.getSubcategoryService(sensitiveInfoRepository)
    private val subCategoryDao = appDatabase.subcategoriaDao()
    private val expensesTypeDao = appDatabase.tipoGastosDao()
    private val subCategoryRepository = SubCategoryRepositoryImpl(subCategoryService, subCategoryDao, expensesTypeDao, userDao)

    private val incomesService = RetrofitInstance.getIncomesService(sensitiveInfoRepository)
    private val incomesDao = appDatabase.ingresoDao()
    private val incomesRepository = IncomesRepositoryImpl(incomesService, incomesDao, userDao)

    private val transactionsService = RetrofitInstance.getTransactionsService(sensitiveInfoRepository)
    private val transactionDao = appDatabase.transaccionDao()
    private val transactionsRepository = TransactionsRepositoryImpl(transactionsService, transactionDao, userDao)

    fun provideUserRepository(): UserRepository {
        return userRepository
    }

    fun provideSensitiveInfoRepository(): SensitiveInfoRepository {
        return sensitiveInfoRepository
    }

    fun provideFinanceRepository(): FinanceRepository {
        return financeRepository
    }

    fun provideCategoryRepository(): CategoryRepository{
        return categoryRepository
    }

    fun provideSubCategoryRepository(): SubCategoryRepository {
        return subCategoryRepository
    }

    fun provideIncomesRepository(): IncomesRepository {
        return incomesRepository
    }

    fun provideTransactionRepository(): TransactionsRepository {
        return transactionsRepository
    }
}
