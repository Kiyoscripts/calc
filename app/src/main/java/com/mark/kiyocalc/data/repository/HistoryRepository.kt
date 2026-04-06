package com.mark.kiyocalc.data.repository

import com.mark.kiyocalc.data.local.HistoryDao
import com.mark.kiyocalc.data.local.HistoryEntity
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val dao: HistoryDao) {
    fun observeHistory(): Flow<List<HistoryEntity>> = dao.observeAll()

    suspend fun save(expression: String, result: String) {
        dao.insert(
            HistoryEntity(
                expression = expression,
                result = result,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun clearHistory() = dao.clearAll()
}
