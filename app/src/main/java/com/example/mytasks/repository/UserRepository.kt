package com.example.mytasks.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mytasks.entities.UserEntity

class UserRepository private constructor(context: Context) {
    private var mTaskDataBase: TaskDataBase = TaskDataBase(context)

    companion object {
        fun getInstance(context: Context): UserRepository {
            if (INSTANCE == null) {
                INSTANCE = UserRepository(context)
            }
            return INSTANCE as UserRepository
        }

        private var INSTANCE: UserRepository? = null
    }

    fun insert(name: String, email: String, password: String): Int {
        val db = mTaskDataBase.writableDatabase

        val insertValues = ContentValues()
        insertValues.put(DataBaseConstants.USER.COLUMNS.NAME, name)
        insertValues.put(DataBaseConstants.USER.COLUMNS.EMAIL, email)
        insertValues.put(DataBaseConstants.USER.COLUMNS.PASSWORD, password)

        return db.insert(DataBaseConstants.USER.TABLE_NAME, null, insertValues).toInt()
    }

    fun get(email: String, password: String): UserEntity? {
        var userEntity: UserEntity? = null
        try {
            val cursor: Cursor
            val db = mTaskDataBase.readableDatabase

            val projection = arrayOf(DataBaseConstants.USER.COLUMNS.ID,
                DataBaseConstants.USER.COLUMNS.NAME,
                DataBaseConstants.USER.COLUMNS.EMAIL,
                DataBaseConstants.USER.COLUMNS.PASSWORD)

            val selection = "${DataBaseConstants.USER.COLUMNS.EMAIL} =? AND ${DataBaseConstants.USER.COLUMNS.PASSWORD} =?"
            val selectionArgs = arrayOf(email, password)

            cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

            if (cursor.count > 0){
                cursor.moveToFirst()

               val userId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.ID))
               val userName = cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.NAME))
               val userEmail = cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.EMAIL))

                userEntity = UserEntity(userId, userName, userEmail)

            }


            cursor.close()
        }catch (e: Exception){
            return userEntity
        }

        return userEntity

    }

    fun isEmailExistent(email: String): Boolean {

        val ret: Boolean
        try {
            val cursor: Cursor
            val db = mTaskDataBase.readableDatabase
            val projection = arrayOf(DataBaseConstants.USER.COLUMNS.ID)
            val selection = "${DataBaseConstants.USER.COLUMNS.EMAIL} =?"
            val selectionArgs = arrayOf(email)

            cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            ret = cursor.count > 0
            cursor.close()
        } catch (e: Exception) {
            throw e
        }

        return ret
    }

}