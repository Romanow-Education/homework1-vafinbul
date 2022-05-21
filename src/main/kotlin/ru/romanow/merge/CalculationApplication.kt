package ru.romanow.merge

import ru.romanow.merge.exceptions.WrongBracketsException
import java.util.*
import kotlin.math.pow

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("Requires only one input parameter")
    }

    val expression = args[0]
    val result = CalculationApplication().calculate(expression)
    println("Expression $expression = $result")
}

class CalculationApplication {
    private val operationPriority = mapOf(
        '(' to 0,
        '+' to 1, '-' to 1,
        '*' to 2, '/' to 2,
        '^' to 3
    )

    fun calculate(expr: String): Double {
        val postfix = buildPostfix(expr)
        return evaluate(postfix)
    }

    private fun evaluate(postfix: List<String>): Double {
        val stack = Stack<Double>()
        val operations = operationPriority.keys.map { it.toString() }
        for (item in postfix) {
            when {
                isNumber(item) -> stack.push(item.toDouble())
                item in operations -> {
                    val second = if (!stack.empty()) stack.pop() else 0.0
                    val first = if (!stack.empty()) stack.pop() else 0.0
                    stack.push(execute(item, first, second))
                }
            }
        }

        return stack.pop()
    }

    private fun execute(operation: String, first: Double, second: Double): Double {
        return when (operation) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> first / second
            "^" -> first.pow(second)
            else -> throw IllegalArgumentException("Operation '$operation' not found")
        }
    }

    private fun buildPostfix(expr: String): List<String> {
        val postfix = arrayListOf<String>()
        val operations = Stack<Char>()

        val idx = IntHolder(0)
        while (idx.value < expr.length) {
            when (val chr = expr[idx.value]) {
                in '0'..'9' -> postfix += readNumber(expr, idx)
                '(' -> operations.push(chr)
                ')' -> {
                    while (!operations.empty() && operations.peek() != '(') {
                        postfix += operations.pop().toString()
                    }
                    if (operations.empty()) {
                        throw WrongBracketsException("Missing open bracket")
                    }
                    operations.pop()
                }
                '+', '-', '*', '/', '^' -> {
                    while (!operations.empty() && (operationPriority[operations.peek()]!! >= operationPriority[chr]!!)) {
                        postfix += operations.pop().toString()
                    }
                    operations.push(chr)
                }
            }

            idx.value++
        }

        if ('(' in operations) {
            throw WrongBracketsException("Missing close bracket")
        }
        operations.reversed().forEach { postfix += it.toString() }

        return postfix
    }

    private fun readNumber(expr: String, idx: IntHolder): String {
        var result = ""
        while (idx.value < expr.length && expr[idx.value].isDigit()) {
            result += expr[idx.value++]
        }
        idx.value--
        return result
    }

    private fun isNumber(str: String) = str.toIntOrNull()?.let { true } ?: false

    private data class IntHolder(var value: Int)
}