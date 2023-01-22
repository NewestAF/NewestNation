package com.newestaf.newestnation.util.impl

import com.newestaf.newestnation.util.SqlCondition
import com.newestaf.newestutil.util.LogUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.util.UUID

class SQLiteManagerImplTest {

    @Test
    fun createTable() {
        val manager = SQLiteManagerImpl(File("data.db"))
        val column = List(5) {
            "columnTest$it INT"
        }
        manager.createTable("test", column)
    }

    @Test
    fun insert() {
        val manager = SQLiteManagerImpl(File("testBukkit/plugins/NewestNation/data/data.db"))
        manager.insert("start_location", listOf("uuid", "x", "y", "z"), listOf(
            "\"${UUID.randomUUID()}\"", "10", "60", "10"
        ))
    }

    @Test
    fun testInsert() {
    }

    @Test
    fun isExists() {
    }

    @Test
    fun select() {
        val manager = SQLiteManagerImpl(File("testBukkit/plugins/NewestNation/data/data.db"))
        manager.select(
            "start_location", listOf("x", "y", "z"), SqlCondition(
                "uuid", "5b2c67a0-c1fe-42c5-94c3-c33a9d70c15f", SqlCondition.ConditionType.EQUALS
            )
        )

    }

    @Test
    fun testSelect() {
    }

    @Test
    fun close() {
    }
}
