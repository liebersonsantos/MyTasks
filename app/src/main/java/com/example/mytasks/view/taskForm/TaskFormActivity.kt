package com.example.mytasks.view.taskForm

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mPriorityBusiness = PriorityBusiness(this@TaskFormActivity)
        mTaskBusiness = TaskBusiness(this@TaskFormActivity)
        mSecurity = SecurityPreferences(this@TaskFormActivity)

        setListeners()
        loadPriorities()
    }

    private fun setListeners() {
        btnDate.setOnClickListener(this)
        btnSaveDate.setOnClickListener(this)
    }

    private fun loadPriorities() {
        mListPriorityEntity = mPriorityBusiness.getList()
        val listPriorities =  mListPriorityEntity.map { it.description }
        mListPrioritiesId = mListPriorityEntity.map { it.id }.toMutableList()


        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPriorities)
        spinnerPriority.adapter = adapter
    }

    override fun onClick(view: View?) {
        when(view?.id){
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

            val taskEntity = TaskEntity(0, userId, priorityId, description, dueDate, complete)
            mTaskBusiness.insert(taskEntity)

            finish()

        }catch (e: Exception){
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
        fun getIntent(context: Context): Intent {
            return Intent(context, TaskFormActivity::class.java)
        }
    }
}
