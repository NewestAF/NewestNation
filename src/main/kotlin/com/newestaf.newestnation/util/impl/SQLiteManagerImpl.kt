package com.newestaf.newestnation.util.impl

import com.newestaf.newestnation.util.DatabaseManager
import com.newestaf.newestnation.util.SqlCondition
import com.newestaf.newestutil.util.LogUtils
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import java.io.File
import java.sql.*

@Factory(binds = [DatabaseManager::class])
class SQLiteManagerImpl(@InjectedParam private val dbFile: File) : DatabaseManager {

    private var connection: Connection

    init {
        if (!dbFile.exists()) {
            dbFile.createNewFile()
        }
        connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.absolutePath}")
    }

    override fun createTable(tableName: String, columns: List<String>) {
        val statement = connection.createStatement()
        try {
            if (!tableExists(tableName)) {
                val queryBuilder = StringBuilder("CREATE TABLE IF NOT EXISTS $tableName (")
                queryBuilder.append(handleColumnOrValue(columns))
                queryBuilder.append(")")
                statement.execute(queryBuilder.toString())
            }
        }
        catch (e: SQLException) {
//            LogUtils.warning("can't execute " + statement + ": " + e.message)
            throw e
        }
        finally {
            statement.close()
        }
    }

    override fun insert(tableName: String, values: List<String>) {
        val statement = connection.createStatement()
        try {
            if (tableExists(tableName)) {
                val queryBuilder = StringBuilder("INSERT INTO $tableName VALUES (")
                queryBuilder.append(handleColumnOrValue(values))
                queryBuilder.append(")")
                statement.execute(queryBuilder.toString())
            }
        }
        catch (e: SQLException) {
//            LogUtils.warning("can't execute " + statement + ": " + e.message)
            throw e
        }
        finally {
            statement.close()
        }
    }

    override fun insert(tableName: String, columns: List<String>, values: List<String>) {
        val statement = connection.createStatement()
        try {
            if (tableExists(tableName)) {
                val queryBuilder = StringBuilder("INSERT INTO $tableName (")
                queryBuilder.append(handleColumnOrValue(columns))
                queryBuilder.append(") ")
                queryBuilder.append("VALUES (")
                queryBuilder.append(handleColumnOrValue(values))
                queryBuilder.append(")")
                statement.execute(queryBuilder.toString())
            }
        }
        catch (e: SQLException) {
//            LogUtils.warning("can't execute " + statement + ": " + e.message)
            throw e
        }
        finally {
            statement.close()
        }
    }

    override fun isExists(tableName: String, predicates: SqlCondition): Boolean {
        val statement = connection.createStatement()
        try {
            val queryBuilder = StringBuilder("SELECT EXISTS(SELECT 1 FROM $tableName WHERE $predicates) as success")
            val resultSet = statement.executeQuery(queryBuilder.toString())
            if (resultSet.next()) {
                return resultSet.getBoolean("success")
            }
            return false
        }
        catch (e: SQLException) {
//            LogUtils.warning("can't execute " + statement + ": " + e.message)
            throw e
        }
        finally {
            statement.close()
        }
    }

    override fun select(tableName: String, predicates: SqlCondition): List<HashMap<String, Any>> {
        val statement = connection.createStatement()
        try {
            if (tableExists(tableName)) {
                val queryBuilder = StringBuilder("SELECT * FROM $tableName WHERE $predicates")
                val resultSet = statement.executeQuery(queryBuilder.toString())
                return handleResultSet(resultSet)
            }
            return emptyList()
        }
        catch (e: SQLException) {
//            LogUtils.warning("can't execute " + statement + ": " + e.message)
            throw e
        }
        finally {
            statement.close()
        }
    }

    override fun select(tableName: String, columns: List<String>, predicates: SqlCondition): List<HashMap<String, Any>> {
        val statement = connection.createStatement()
        try {
            if (tableExists(tableName)) {
                val queryBuilder = StringBuilder("SELECT ${handleColumnOrValue(columns)} FROM $tableName WHERE $predicates")
                val resultSet = statement.executeQuery(queryBuilder.toString())
                return handleResultSet(resultSet)
            }
            return emptyList()
        }
        catch (e: SQLException) {
//            LogUtils.warning("can't execute " + statement + ": " + e.message)
            throw e
        }
        finally {
            statement.close()
        }
    }

    override fun close() {
        try {
            if (!connection.autoCommit) {
                connection.rollback()
            }
            // Logging
            connection.close()
        }
        catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    private fun tableExists(tableName: String): Boolean {
        val dbm: DatabaseMetaData = connection.metaData
        val tables: ResultSet = dbm.getTables(null, null, tableName, null)
        return tables.next()
    }

    private fun handleColumnOrValue(columnsOrValue: List<String>): String {
        val stringbuilder = StringBuilder()
        columnsOrValue.forEach {
            stringbuilder.append(it)
            stringbuilder.append(", ")
        }
        stringbuilder.deleteCharAt(stringbuilder.length - 1)
        stringbuilder.deleteCharAt(stringbuilder.length - 1)
        return stringbuilder.toString()
    }

    private fun handleResultSet(resultSet: ResultSet): List<HashMap<String, Any>> {
        val result = mutableListOf<HashMap<String, Any>>()
        while (resultSet.next()) {
            val row = hashMapOf<String, Any>()
            for (i in 1..resultSet.metaData.columnCount) {
                row.put(resultSet.metaData.getColumnName(i), resultSet.getObject(i))
            }
            result.add(row)
        }
        return result
    }

}
