package com.example.mytasks.business

import android.content.Context
import com.example.mytasks.TasksConstants.TaskConstants
import com.example.mytasks.entities.TaskEntity
import com.example.mytasks.repository.TaskRepository
import com.example.mytasks.util.SecurityPreferences

class TaskBusiness(context: Context) {
    private val mTaskRepository: TaskRepository = TaskRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun getList(): MutableList<TaskEntity> {
        val userId = mSecurityPreferences.getStoredString(TaskConstants.USER_ID)!!.toInt()
        return mTaskRepository.getList(userId)
    }

    fun insert(taskEntity: TaskEntity) = mTaskRepository.insert(taskEntity)
}