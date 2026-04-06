package com.mark.kiyocalc.domain.model

sealed interface CalculatorAction {
    data class Number(val value: String) : CalculatorAction
    data class Operator(val value: String) : CalculatorAction
    data object Decimal : CalculatorAction
    data object Clear : CalculatorAction
    data object AllClear : CalculatorAction
    data object Delete : CalculatorAction
    data object Equals : CalculatorAction
    data object Percent : CalculatorAction
    data object ToggleSign : CalculatorAction
    data class RestoreHistory(val expression: String) : CalculatorAction
}
