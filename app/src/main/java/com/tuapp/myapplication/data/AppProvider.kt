package com.tuapp.myapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.tuapp.myapplication.data.database.AppDatabase
import com.tuapp.myapplication.data.remote.RetrofitInstance
import com.tuapp.myapplication.data.repository.categories.CategoryRepository
import com.tuapp.myapplication.data.repository.categories.CategoryRepositoryImpl
import com.tuapp.myapplication.data.repository.finance.FinanceRepository
import com.tuapp.myapplication.data.repository.finance.FinanceRepositoryImpl
import com.tuapp.myapplication.data.repository.ia.GenerativeRepository
import com.tuapp.myapplication.data.repository.ia.GenerativeRepositoryImpl
import com.tuapp.myapplication.data.repository.incomes.IncomesRepository
import com.tuapp.myapplication.data.repository.incomes.IncomesRepositoryImpl
import com.tuapp.myapplication.data.repository.savings.SavingsRepository
import com.tuapp.myapplication.data.repository.savings.SavingsRepositoryImpl
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

    val financeMembersDao = appDatabase.miembrosFinanzasDao()
    val conjFinanceDao = appDatabase.finanzaConjuntaDao()
    val financeSummaryDao = appDatabase.resumenFinancieroDao()
    val categorieDataDao = appDatabase.categoriaDataDao()
    val financeService = RetrofitInstance.getFinanceService(sensitiveInfoRepository)
    val financeRepository = FinanceRepositoryImpl(financeService, userDao, financeMembersDao, conjFinanceDao, financeSummaryDao, categorieDataDao)


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

    private val savingsService = RetrofitInstance.getSavingsService(sensitiveInfoRepository)
    private val savingsDao = appDatabase.ahorroDao()
    private val savingsRepository = SavingsRepositoryImpl(savingsService, savingsDao, userDao)

    private val generativeModel = Firebase.ai(backend = GenerativeBackend.vertexAI()).generativeModel("gemini-2.5-flash")
    private val generativeRepository = GenerativeRepositoryImpl(generativeModel)

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

    fun provideSavingRepository(): SavingsRepository {
        return savingsRepository
    }

    fun provideGenerativeRepository(): GenerativeRepository {
        return generativeRepository
    }
}
