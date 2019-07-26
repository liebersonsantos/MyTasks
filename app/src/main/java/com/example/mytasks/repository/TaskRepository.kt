package com.example.mytasks.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mytasks.entities.TaskEntity
import java.lang.Exception

class TaskRepository private constructor(context: Context) {
    private var mTaskDataBase: TaskDataBase = TaskDataBase(context)

    companion object {
        fun getInstance(context: Context): TaskRepository {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
            return INSTANCE as TaskRepository
        }

        private var INSTANCE: TaskRepository? = null
    }

    fun get(id: Int): TaskEntity? {
        var taskEntity: TaskEntity? = null
        try {
            val cursor: Cursor
            val db = mTaskDataBase.readableDatabase

            val projection = arrayOf(
                    DataBaseConstants.TASK.COLUMNS.ID,
                    DataBaseConstants.TASK.COLUMNS.USER_ID,
                    DataBaseConstants.TASK.COLUMNS.PRIORITY_ID,
                    DataBaseConstants.TASK.COLUMNS.DESCRIPTION,
                    DataBaseConstants.TASK.COLUMNS.DUEDATE,
                    DataBaseConstants.TASK.COLUMNS.COMPLETE)

            val selection = "${DataBaseConstants.TASK.COLUMNS.ID} = ?"
            val selectionArgs = arrayOf(id.toString())

            cursor = db.query(DataBaseConstants.TASK.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

            if (cursor.count > 0) {
                cursor.moveToFirst()

                val taskId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                val userId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.USER_ID))
                val priorityId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITY_ID))
                val description = cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))
                val dueDate = cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                val complete = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE))) == 1

                taskEntity = TaskEntity(taskId, userId, priorityId, description, dueDate, complete)

            }


            cursor.close()
        } catch (e: Exception) {
            return taskEntity
        }

        return taskEntity

    }

    fun insert(task: TaskEntity) {
        try {
            val db = mTaskDataBase.writableDatabase
            val complete: Int = if (task.complete) 1 else 0

            val insertValues = ContentValues()
            insertValues.put(DataBaseConstants.TASK.COLUMNS.USER_ID, task.userId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITY_ID, task.priorityId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)

            db.insert(DataBaseConstants.TASK.TABLE_NAME, null, insertValues)
        } catch (e: Exception) {
            throw e
        }

    }

    fun update(task: TaskEntity) {
        try {
            val db = mTaskDataBase.writableDatabase
            val complete: Int = if (task.complete) 1 else 0

            val updateValues = ContentValues()
            updateValues.put(DataBaseConstants.TASK.COLUMNS.USER_ID, task.userId)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITY_ID, task.priorityId)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)

            val selection = "${DataBaseConstants.TASK.COLUMNS.ID} = ?"
            val selectionArgs = arrayOf(task.id.toString())

            db.update(DataBaseConstants.TASK.TABLE_NAME, updateValues, selection, selectionArgs)
        } catch (e: Exception) {
            throw e
        }

    }

    fun delete(id: Int) {
        try {
            val db = mTaskDataBase.writableDatabase

            val where = "${DataBaseConstants.TASK.COLUMNS.ID} = ?"
            val whereArgs = arrayOf(id.toString())

            db.delete(DataBaseConstants.TASK.TABLE_NAME, where, whereArgs)
        } catch (e: Exception) {
            throw e
        }

    }

    fun getList(userId: Int, mTaskFilter: Int): MutableList<TaskEntity> {
        val list = mutableListOf<TaskEntity>()

        try {
            val cursor: Cursor
            val db = mTaskDataBase.readableDatabase

            cursor = db.rawQuery("SELECT * FROM ${DataBaseConstants.TASK.TABLE_NAME} " +
                    "WHERE ${DataBaseConstants.TASK.COLUMNS.USER_ID} = $userId " +
                    "AND ${DataBaseConstants.TASK.COLUMNS.COMPLETE} = $mTaskFilter", null)

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                    val priorityId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITY_ID))
                    val description = cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))
                    val dueDate = cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                    val complete = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE))) == 1

                    list.add(TaskEntity(id, userId, priorityId, description, dueDate, complete))
                }
            }

            cursor.close()


        } catch (e: Exception) {
            return list
        }

        return list
    }
}