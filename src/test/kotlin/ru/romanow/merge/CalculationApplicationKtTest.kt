package ru.romanow.merge

import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import ru.romanow.merge.exceptions.WrongBracketsException
import java.util.stream.Stream

internal class CalculationApplicationTest {
    private val app = CalculationApplication()

    @MethodSource("factory")
    @ParameterizedTest(name = "''{0}'' -> ''{1}''")
    fun calculate_success(expr: String, result: Double) {
        assertThat(app.calculate(expr)).isEqualTo(result)
    }

    @ValueSource(strings = ["(2+2", "2+2)"])
    @ParameterizedTest(name = "''{0}'' -> WrongBracketsException")
    fun calculate_error(expr: String) {
        assertThatExceptionOfType(WrongBracketsException::class.java).isThrownBy { app.calculate(expr) }
    }

    companion object {
        @JvmStatic
        fun factory() = Stream.of(
            Arguments.of("2+2", 4.0),
            Arguments.of("3 * 3", 9.0),
            Arguments.of("3^3*3+3/3", 82.0),
            Arguments.of("22 + 22", 44.0),
            Arguments.of("(3+3)*(3-3^1)", 0),
            Arguments.of("3-(3 + 3)*3", -15.0),
            Arguments.of("(3-(3 + 3)*3)", -15.0),
        )
    }
}