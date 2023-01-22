package com.newestaf.newestnation.util

interface DatabaseManager : AutoCloseable {

    fun createTable(tableName: String, columns: List<String>)
    fun insert(tableName: String, values: List<String>)
    fun insert(tableName: String, columns: List<String>, values: List<String>)
    fun isExists(tableName: String, predicates: SqlCondition): Boolean
    fun select(tableName: String, predicates: SqlCondition): List<HashMap<String, Any>>
    fun select(tableName: String, columns: List<String>, predicates: SqlCondition): List<HashMap<String, Any>>
    override fun close()
}



