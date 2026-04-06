package com.mark.kiyocalc.domain.evaluator

import java.math.BigDecimal
import java.math.RoundingMode

class ExpressionEvaluator {
    fun evaluate(expression: String): String {
        val normalized = expression.replace('×', '*').replace('÷', '/').replace(" ", "")
        if (normalized.isBlank()) return "0"

        val tokens = tokenize(normalized)
        val reduced = reduceMulDiv(tokens)
        val result = reduceAddSub(reduced)
        return format(result)
    }

    private fun tokenize(expression: String): MutableList<String> {
        val tokens = mutableListOf<String>()
        val current = StringBuilder()

        fun flush() {
            if (current.isNotEmpty()) {
                tokens += current.toString()
                current.clear()
            }
        }

        expression.forEachIndexed { index, char ->
            when {
                char.isDigit() || char == '.' -> current.append(char)
                char in listOf('+', '-', '*', '/') -> {
                    if (char == '-' && (index == 0 || expression[index - 1] in listOf('+', '-', '*', '/'))) {
                        current.append(char)
                    } else {
                        flush()
                        tokens += char.toString()
                    }
                }
                else -> throw IllegalArgumentException("Unsupported character: $char")
            }
        }
        flush()
        return tokens
    }

    private fun reduceMulDiv(tokens: MutableList<String>): MutableList<String> {
        val output = mutableListOf<String>()
        var index = 0
        while (index < tokens.size) {
            val token = tokens[index]
            if (token == "*" || token == "/") {
                val left = output.removeLast().toBigDecimal()
                val right = tokens.getOrNull(index + 1)?.toBigDecimalOrNull()
                    ?: throw IllegalArgumentException("Invalid expression")
                val value = if (token == "*") {
                    left.multiply(right)
                } else {
                    if (right.compareTo(BigDecimal.ZERO) == 0) throw ArithmeticException("Cannot divide by zero")
                    left.divide(right, 12, RoundingMode.HALF_UP)
                }
                output += value.stripTrailingZeros().toPlainString()
                index += 2
            } else {
                output += token
                index += 1
            }
        }
        return output
    }

    private fun reduceAddSub(tokens: MutableList<String>): BigDecimal {
        var result = tokens.firstOrNull()?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        var index = 1
        while (index < tokens.size) {
            val op = tokens[index]
            val next = tokens.getOrNull(index + 1)?.toBigDecimalOrNull()
                ?: throw IllegalArgumentException("Invalid expression")
            result = when (op) {
                "+" -> result.add(next)
                "-" -> result.subtract(next)
                else -> throw IllegalArgumentException("Invalid operator: $op")
            }
            index += 2
        }
        return result
    }

    private fun format(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return if (stripped.scale() <= 0) stripped.toPlainString() else stripped.toPlainString()
    }
}
