package com.mark.kiyocalc.domain.model

import com.mark.kiyocalc.data.local.HistoryEntity

data class CalculatorState(
    val expression: String = "",
    val result: String = "0",
    val history: List<HistoryEntity> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String? = null
)
