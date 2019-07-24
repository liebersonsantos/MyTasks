package com.example.mytasks.repository

import com.example.mytasks.entities.PriorityEntity

class PriorityCacheConstants private constructor(){

    companion object{
        private val mPriorityCache = hashMapOf<Int, String>()

        fun setCache(list: List<PriorityEntity>){
            for (item in list){
                mPriorityCache.put(item.id, item.description)
            }
        }

        fun getPriorityDescription(id: Int): String{
            if (mPriorityCache[id] == null){
                return ""
            }

            return mPriorityCache[id].toString()
        }
    }
}