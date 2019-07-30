package com.example.mytasks.view.taskForm

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mytasks.R
import com.example.mytasks.TasksConstants.TaskConstants
import com.example.mytasks.business.PriorityBusiness
import com.example.mytasks.business.TaskBusiness
import com.example.mytasks.entities.PriorityEntity
import com.example.mytasks.entities.TaskEntity
import com.example.mytasks.util.SecurityPreferences
import com.example.mytasks.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_task_form.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mSecurity: SecurityPreferences
    private val mSimpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    private var mListPriorityEntity: MutableList<PriorityEntity> = mutableListOf()
    private var mListPrioritiesId: MutableList<Int> = mutableListOf()

    private var mTaskId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mPriorityBusiness = PriorityBusiness(this@TaskFormActivity)
        mTaskBusiness = TaskBusiness(this@TaskFormActivity)
        mSecurity = SecurityPreferences(this@TaskFormActivity)

        setListeners()
        loadPriorities()

        loadDataFromActivity()
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras

        if (bundle != null) {
            mTaskId = bundle.getInt(TaskConstants.TASK_ID)

            val task = mTaskBusiness.get(mTaskId)
            task?.let { taskData ->
                editDescription.setText(taskData.description)
                btnDate.text = taskData.dueDate
                checkComplete.isChecked = taskData.complete
                spinnerPriority.setSelection(getIndex(taskData.priorityId))
            }
        }

    }

    private fun getIndex(id: Int): Int {
        var index = 0
        for (i in 0..mListPriorityEntity.size){
            if (mListPriorityEntity[i].id == id){
                index = i
                break
            }
        }

        return index
    }

    private fun setListeners() {
        btnDate.setOnClickListener(this)
        btnSaveDate.setOnClickListener(this)
    }

    private fun loadPriorities() {
        mListPriorityEntity = mPriorityBusiness.getList()
        val listPriorities = mListPriorityEntity.map { it.description }
        mListPrioritiesId = mListPriorityEntity.map { it.id }.toMutableList()


        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPriorities)
        spinnerPriority.adapter = adapter
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnDate -> openDatePickerDialog()
            R.id.btnSaveDate -> handleSave()
        }

    }

    private fun handleSave() {
        try {
            val description = editDescription.text.toString()
            val priorityId = mListPrioritiesId[spinnerPriority.selectedItemPosition]
            val complete = checkComplete.isChecked
            val dueDate = btnDate.text.toString()
            val userId = mSecurity.getStoredString(TaskConstants.USER_ID)!!.toInt()

            val taskEntity = TaskEntity(mTaskId, userId, priorityId, description, dueDate, complete)

            if (mTaskId == 0){
                mTaskBusiness.insert(taskEntity)
                Log.d("TASK_ENTITY", "insert -> " + taskId)
                Toast.makeText(this@TaskFormActivity, "Tarefa incluída com sucesso", Toast.LENGTH_LONG).show()
            }else {
                mTaskBusiness.update(taskEntity)
                Log.d("TASK_ENTITY", "update -> " + taskId)
                Toast.makeText(this@TaskFormActivity, "Tarefa alterada com sucesso", Toast.LENGTH_LONG).show()
            }

            finish()

        } catch (e: Exception) {
            Toast.makeText(this@TaskFormActivity, R.string.generic_error, Toast.LENGTH_LONG).show()
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        btnDate.text = mSimpleDateFormat.format(calendar.time)

    }

    private fun openDatePickerDialog() {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    companion object {
        fun getIntent(context: Context, taskEntity: TaskEntity?): Intent {
            return Intent(context, TaskFormActivity::class.java).apply {
                putExtra(TaskConstants.TASK_ID, taskEntity?.id)
            }
        }
    }
}

