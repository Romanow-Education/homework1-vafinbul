package ru.romanow.merge

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("Requires only one input parameter")
    }

    val expression = args[0]
    val result = CalculationApplication().calculate(expression)
    println("Expression $expression = $result")
}

class CalculationApplication {
    fun calculate(expr: String): Double {
        return 0.0
    }
}