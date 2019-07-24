package com.example.mytasks.business

import android.content.Context
import com.example.mytasks.R
import com.example.mytasks.TasksConstants.TaskConstants
import com.example.mytasks.entities.UserEntity
import com.example.mytasks.repository.UserRepository
import com.example.mytasks.util.SecurityPreferences
import com.example.mytasks.util.ValidationException

class UserBusiness(val context: Context) {

    private val mUserRepository: UserRepository = UserRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun login(email: String, password: String): Boolean{
        val user: UserEntity? = mUserRepository.get(email, password)
        return if (user != null){
            mSecurityPreferences.storeString(TaskConstants.USER_ID, user.id.toString())
            mSecurityPreferences.storeString(TaskConstants.USER_NAME, user.name)
            mSecurityPreferences.storeString(TaskConstants.USER_EMAIL, user.email)

            true
        }else {
            false
        }

    }

    fun insert(name: String, email: String, password: String){
       try {
           if (name == "" || email == "" || password == ""){
               throw ValidationException(context.getString(R.string.complete_data))
           }

           if(mUserRepository.isEmailExistent(email)){
               throw ValidationException(context.getString(R.string.invalid_email))
           }
           val userId = mUserRepository.insert(name, email, password)
           mSecurityPreferences.storeString(TaskConstants.USER_ID, userId.toString())
           mSecurityPreferences.storeString(TaskConstants.USER_NAME, name)
           mSecurityPreferences.storeString(TaskConstants.USER_EMAIL, email)


       }catch (e: Exception){
           throw e
       }

    }
}