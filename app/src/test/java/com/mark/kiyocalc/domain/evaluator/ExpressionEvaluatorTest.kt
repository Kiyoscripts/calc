package com.mark.kiyocalc.domain.evaluator

import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionEvaluatorTest {
    private val evaluator = ExpressionEvaluator()

    @Test
    fun respectsOperatorPrecedence() {
        assertEquals("22", evaluator.evaluate("12+3×4-2"))
    }

    @Test
    fun handlesDecimals() {
        assertEquals("5.5", evaluator.evaluate("2.2+3.3"))
    }

    @Test(expected = ArithmeticException::class)
    fun throwsOnDivideByZero() {
        evaluator.evaluate("9÷0")
    }
}
