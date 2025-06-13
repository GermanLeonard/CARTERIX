package com.tuapp.myapplication.helpers

import com.tuapp.myapplication.data.database.dao.user.UserDao
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


suspend fun getFinanceId(userDao: UserDao): Int {
    return userDao.getUser().map { it.finanzaId }.firstOrNull() ?: 0
}
