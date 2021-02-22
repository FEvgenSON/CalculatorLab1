package ru.fevgenson.calculator

private typealias MathParserExpression = org.mariuszgromada.math.mxparser.Expression

class SyntaxException : ArithmeticException()
class DivideByZeroException : ArithmeticException()

class CalculateExpressionUseCase {

    private companion object {

        const val NEXT_ID_OFFSET = 1
        const val SYNTAX_ERROR = "lexical error"
    }

    /**
     * Вычисление выражения при помощи mXparser
     */
    operator fun invoke(expression: Expression): Expression {
        val mathParserExpression = MathParserExpression(expression.value)
        mathParserExpression.setVerboseMode()
        val answer = mathParserExpression.calculate()
        val error = mathParserExpression.errorMessage
        handleError(error, answer)
        return Expression(
            id = expression.id + NEXT_ID_OFFSET,
            value = formatAnswer(answer)
        )
    }

    /**
     * Обработка ошибко от mXparser
     * Если результат NaN и была ошибка в синтаксисе, то выкидываем SyntaxException, инчае DivideByZeroException
     */
    private fun handleError(error: String, answer: Double) {
        when {
            !answer.isNaN() -> return
            error.contains(SYNTAX_ERROR) -> throw SyntaxException()
            else -> throw DivideByZeroException()
        }
    }

    /**
     * Убираем лишние нули после запятой у целых чисел
     */
    private fun formatAnswer(answer: Double) =
        (answer.toInt().takeIf { it - answer == 0.0 } ?: answer).toString()
}