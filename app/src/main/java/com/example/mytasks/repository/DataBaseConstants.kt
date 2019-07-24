package com.example.mytasks.repository

class DataBaseConstants {

    object USER {
        val TABLE_NAME = "user"

        object COLUMNS {
            val ID = "id"
            val NAME = "name"
            val EMAIL = "email"
            val PASSWORD = "password"
        }
    }

    object PRIORITY {
        val TABLE_NAME = "priority"

        object COLUMNS {
            val ID = "id"
            val DESCRIPTION = "description"
        }
    }

    object TASK {
        val TABLE_NAME = "task"

        object COLUMNS {
            val ID = "id"
            val USER_ID = "userId"
            val PRIORITY_ID = "priorityId"
            val DESCRIPTION = "description"
            val DUEDATE = "duedate"
            val COMPLETE = "complete"
        }
    }
}