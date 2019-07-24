package com.example.mytasks.business

import android.content.Context
import com.example.mytasks.entities.PriorityEntity
import com.example.mytasks.repository.PriorityRepository

class PriorityBusiness(context: Context) {
    private val mPriorityRepository: PriorityRepository = PriorityRepository.getInstance(context)

    fun getList(): MutableList<PriorityEntity> = mPriorityRepository.getList()
}