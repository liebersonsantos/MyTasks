package com.example.mytasks.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val createTableUser = """ CREATE TABLE ${DataBaseConstants.USER.TABLE_NAME} (
        ${DataBaseConstants.USER.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${DataBaseConstants.USER.COLUMNS.NAME} TEXT,
        ${DataBaseConstants.USER.COLUMNS.EMAIL} TEXT,
        ${DataBaseConstants.USER.COLUMNS.PASSWORD} TEXT
    
    );"""

    private val createTablePriority = """ CREATE TABLE ${DataBaseConstants.PRIORITY.TABLE_NAME} (
        ${DataBaseConstants.PRIORITY.COLUMNS.ID} INTEGER PRIMARY KEY,
        ${DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION} TEXT
        ); """

    private val createTableTask = """ CREATE TABLE ${DataBaseConstants.TASK.TABLE_NAME} ( 
        ${DataBaseConstants.TASK.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${DataBaseConstants.TASK.COLUMNS.USER_ID} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.PRIORITY_ID} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.DESCRIPTION} TEXT,
        ${DataBaseConstants.TASK.COLUMNS.COMPLETE} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.DUEDATE} TEXT
        );"""

    private val insertPriorities = """ INSERT INTO ${DataBaseConstants.PRIORITY.TABLE_NAME} 
        VALUES (1, 'Baixa'), (2, 'Média'), (3, 'Alta'), (4, 'Crítica') """

    private val deleteUserTable = "DROP TABLE IF EXISTS ${DataBaseConstants.USER.TABLE_NAME}"
    private val deletePriorityTable = "DROP TABLE IF EXISTS ${DataBaseConstants.PRIORITY.TABLE_NAME}"
    private val deleteTaskTable = "DROP TABLE IF EXISTS ${DataBaseConstants.TASK.TABLE_NAME}"

    override fun onCreate(sqLite: SQLiteDatabase?) {
        sqLite?.execSQL(createTableUser)
        sqLite?.execSQL(createTablePriority)
        sqLite?.execSQL(createTableTask)
        sqLite?.execSQL(insertPriorities)

    }

    override fun onUpgrade(sqLite: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        sqLite?.execSQL(deleteUserTable)
        sqLite?.execSQL(deletePriorityTable)
        sqLite?.execSQL(deleteTaskTable)
        sqLite?.execSQL(createTableUser)
        sqLite?.execSQL(createTablePriority)
        sqLite?.execSQL(createTableTask)

    }


    companion object {
        private val DATABASE_VERSION: Int = 1
        private val DATABASE_NAME: String = "task.db"
    }

    }

