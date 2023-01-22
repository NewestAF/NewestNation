package com.newestaf.newestnation.util

class SqlCondition(
    private val column: String,
    private val value: String,
    private val operator: ConditionType) {

    enum class ConditionType{
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN_OR_EQUALS,
    }

    override fun toString(): String {
        return when (operator) {
            ConditionType.EQUALS -> "$column = '$value'"
            ConditionType.NOT_EQUALS -> "$column != '$value'"
            ConditionType.GREATER_THAN -> "$column > '$value'"
            ConditionType.LESS_THAN -> "$column < '$value'"
            ConditionType.GREATER_THAN_OR_EQUALS -> "$column >= '$value'"
            ConditionType.LESS_THAN_OR_EQUALS -> "$column <= '$value'"
        }
    }


}
