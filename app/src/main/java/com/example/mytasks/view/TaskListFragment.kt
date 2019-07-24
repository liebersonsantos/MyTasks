package com.example.mytasks.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasks.R
import com.example.mytasks.TasksConstants.TaskConstants
import com.example.mytasks.adapter.TaskListAdapter
import com.example.mytasks.business.TaskBusiness
import com.example.mytasks.util.SecurityPreferences
import com.example.mytasks.view.taskForm.TaskFormActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TaskListFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var mRecyclerViewListFragment: RecyclerView
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        mContext = rootView.context
        mTaskBusiness = TaskBusiness(mContext)
        mSecurityPreferences = SecurityPreferences(mContext)

        mRecyclerViewListFragment = rootView.findViewById(R.id.recyclerViewListFragment)
        rootView.findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener(this)

        mRecyclerViewListFragment.adapter = TaskListAdapter(mutableListOf())
        mRecyclerViewListFragment.layoutManager = LinearLayoutManager(mContext)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        mRecyclerViewListFragment.adapter = TaskListAdapter(mTaskBusiness.getList())
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.fabAddTask -> {
                val intent = TaskFormActivity.getIntent(mContext)
                mContext.startActivity(intent)
            }
        }

    }

    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }
}
