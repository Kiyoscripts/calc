package com.mark.kiyocalc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mark.kiyocalc.data.repository.HistoryRepository
import com.mark.kiyocalc.domain.evaluator.ExpressionEvaluator
import com.mark.kiyocalc.domain.model.CalculatorAction
import com.mark.kiyocalc.domain.model.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val repository: HistoryRepository,
    private val evaluator: ExpressionEvaluator = ExpressionEvaluator()
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeHistory().collect { history ->
                _state.update { it.copy(history = history) }
            }
        }
    }

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> append(action.value)
            is CalculatorAction.Operator -> appendOperator(action.value)
            CalculatorAction.Decimal -> appendDecimal()
            CalculatorAction.Delete -> deleteLast()
            CalculatorAction.Clear -> clearEntry()
            CalculatorAction.AllClear -> reset()
            CalculatorAction.Equals -> evaluateAndStore()
            CalculatorAction.Percent -> applyPercent()
            CalculatorAction.ToggleSign -> toggleSign()
            is CalculatorAction.RestoreHistory -> restore(action.expression)
        }
    }

    private fun append(value: String) {
        _state.update {
            val next = if (it.expression == "0") value else it.expression + value
            it.copy(expression = next, isError = false, errorMessage = null)
        }
        previewResult()
    }

    private fun appendOperator(operator: String) {
        _state.update {
            if (it.expression.isBlank()) return@update it
            val trimmed = it.expression.trimEnd()
            val next = if (trimmed.lastOrNull() in listOf('+', '-', '×', '÷')) {
                trimmed.dropLast(1) + operator
            } else {
                trimmed + operator
            }
            it.copy(expression = next, isError = false, errorMessage = null)
        }
    }

    private fun appendDecimal() {
        _state.update {
            val parts = it.expression.split('+', '-', '×', '÷')
            if (parts.lastOrNull()?.contains('.') == true) return@update it
            val suffix = if (it.expression.isEmpty() || it.expression.last() in listOf('+', '-', '×', '÷')) "0." else "."
            it.copy(expression = it.expression + suffix, isError = false, errorMessage = null)
        }
    }

    private fun deleteLast() {
        _state.update {
            val next = it.expression.dropLast(1)
            it.copy(expression = next, result = if (next.isBlank()) "0" else it.result, isError = false, errorMessage = null)
        }
        previewResult()
    }

    private fun clearEntry() {
        _state.update { it.copy(expression = "", result = "0", isError = false, errorMessage = null) }
    }

    private fun reset() {
        clearEntry()
    }

    private fun applyPercent() {
        val current = _state.value.expression
        val number = current.takeLastWhile { it.isDigit() || it == '.' || it == '-' }
        if (number.isBlank()) return
        val replacement = runCatching {
            evaluator.evaluate("$number/100")
        }.getOrNull() ?: return
        val updated = current.dropLast(number.length) + replacement
        _state.update { it.copy(expression = updated, result = replacement, isError = false, errorMessage = null) }
    }

    private fun toggleSign() {
        val current = _state.value.expression
        val number = current.takeLastWhile { it.isDigit() || it == '.' || it == '-' }
        if (number.isBlank()) return
        val toggled = if (number.startsWith("-")) number.removePrefix("-") else "-$number"
        val updated = current.dropLast(number.length) + toggled
        _state.update { it.copy(expression = updated, isError = false, errorMessage = null) }
        previewResult()
    }

    private fun restore(expression: String) {
        _state.update { it.copy(expression = expression, isError = false, errorMessage = null) }
        previewResult()
    }

    private fun previewResult() {
        val expr = _state.value.expression
        if (expr.isBlank() || expr.lastOrNull() in listOf('+', '-', '×', '÷')) return
        runCatching { evaluator.evaluate(expr) }
            .onSuccess { result -> _state.update { it.copy(result = result, isError = false, errorMessage = null) } }
    }

    private fun evaluateAndStore() {
        val expr = _state.value.expression
        if (expr.isBlank()) return
        runCatching { evaluator.evaluate(expr) }
            .onSuccess { result ->
                _state.update { it.copy(expression = result, result = result, isError = false, errorMessage = null) }
                viewModelScope.launch { repository.save(expr, result) }
            }
            .onFailure { error ->
                _state.update {
                    it.copy(result = "Error", isError = true, errorMessage = error.message ?: "Invalid expression")
                }
            }
    }
}
