package com.example.mytasks.repository

import android.content.Context
import android.database.Cursor
import com.example.mytasks.entities.PriorityEntity
import java.lang.Exception

class PriorityRepository private constructor(context: Context) {
    private var mTaskDataBase: TaskDataBase = TaskDataBase(context)

    companion object {
        fun getInstance(context: Context): PriorityRepository {
            if (INSTANCE == null) {
                INSTANCE = PriorityRepository(context)
            }
            return INSTANCE as PriorityRepository
        }

        private var INSTANCE: PriorityRepository? = null
    }

    fun getList(): MutableList<PriorityEntity> {
        val list = mutableListOf<PriorityEntity>()

        try {
            val cursor: Cursor
            val db = mTaskDataBase.readableDatabase

            cursor = db.rawQuery("SELECT * FROM ${DataBaseConstants.PRIORITY.TABLE_NAME}", null)

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.PRIORITY.COLUMNS.ID))
                    val description = cursor.getString(cursor.getColumnIndex(DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION))

                    list.add(PriorityEntity(id, description))
                }
            }

            cursor.close()


        } catch (e: Exception) {
            return list
        }

        return list
    }
}