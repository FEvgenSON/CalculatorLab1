package ru.fevgenson.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    enum class ExpressionParts(
        val value: String
    ) {
        ZERO("0"),
        ONE("1"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),

        DOT("."),
        PLUS("+"),
        MINUS("-"),
        DIVIDE("/"),
        MULTIPLY("*"),
        SINUS("sin("),
        LEFT_BRACKET("("),
        RIGHT_BRACKET(")")
    }

    private companion object {

        const val FIRST_ITEM_ID = 1
        const val EMPTY_EXPRESSION_VALUE = ""
        const val FIRST_ITEM_INDEX = 0
        const val ONE_SYMBOL = 1
    }

    private val calculateExpressionUseCase = CalculateExpressionUseCase()

    /**
     * Список всех выражений, работа идет всегда с первым.
     */
    private val _expressionsList =
        MutableLiveData(listOf(Expression(FIRST_ITEM_ID, EMPTY_EXPRESSION_VALUE)))
    val expressionList: LiveData<List<Expression>>
        get() = _expressionsList

    private val _error = MutableSingleEventLiveData<Int>()
    val error: SingleEventLiveData<Int>
        get() = _error

    fun addExpressionPart(expressionPart: ExpressionParts) {
        _expressionsList.addExpressionPart(expressionPart.value)
    }

    fun calculate() {
        try {
            val calculatedExpression =
                calculateExpressionUseCase(expressionList.requiredValue.first())
            _expressionsList.addExpression(calculatedExpression)
        } catch (e: DivideByZeroException) {
            _error.dispatchEvent(R.string.divide_by_o_error)
        } catch (e: SyntaxException) {
            _error.dispatchEvent(R.string.syntax_error)
        }
    }

    fun clear() {
        _expressionsList.value = listOf(Expression(FIRST_ITEM_ID, EMPTY_EXPRESSION_VALUE))
    }

    fun removeLastSymbol() {
        _expressionsList.removeLastSymbol()
    }

    /**
     * Добавляет новое выражение на первое место
     */
    private fun MutableLiveData<List<Expression>>.addExpression(expression: Expression) {
        value = requiredValue.toMutableList().apply { add(FIRST_ITEM_INDEX, expression) }
    }

    /**
     * Добавляет символы к первому выражению
     */
    private fun MutableLiveData<List<Expression>>.addExpressionPart(expression: String) {
        value = requiredValue.toMutableList().apply {
            this[FIRST_ITEM_INDEX] = first().copy(value = first().value + expression)
        }
    }

    /**
     * Удаляет последний символ у первого выражения
     */
    private fun MutableLiveData<List<Expression>>.removeLastSymbol() {
        value = requiredValue.toMutableList().apply {
            this[FIRST_ITEM_INDEX] = first().copy(value = first().value.dropLast(ONE_SYMBOL))
        }
    }

    private val <T>LiveData<T>.requiredValue: T
        get() = requireNotNull(value)
}