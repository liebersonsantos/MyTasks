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
import com.example.mytasks.entities.OnTaskListFragmentInteractionListener
import com.example.mytasks.util.SecurityPreferences
import com.example.mytasks.view.taskForm.TaskFormActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var mRecyclerViewListFragment: RecyclerView
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mListener: OnTaskListFragmentInteractionListener
    private var mTaskFilter: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        mContext = rootView.context
        mTaskBusiness = TaskBusiness(mContext)
        mSecurityPreferences = SecurityPreferences(mContext)

        mListener = object: OnTaskListFragmentInteractionListener{
            override fun onListClick(taskId: Int) {

            }
        }

        if (arguments != null){
            mTaskFilter = arguments!!.getInt(TaskConstants.KEY_FILTER)
        }

        mRecyclerViewListFragment = rootView.findViewById(R.id.recyclerViewListFragment)
        rootView.findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener(this)

        mRecyclerViewListFragment.adapter = TaskListAdapter(mutableListOf(), null)
        mRecyclerViewListFragment.layoutManager = LinearLayoutManager(mContext)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        mRecyclerViewListFragment.adapter = TaskListAdapter(mTaskBusiness.getList(mTaskFilter)){ task ->
            val intent = TaskFormActivity.getIntent(mContext, task)
            mContext.startActivity(intent)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.fabAddTask -> {
                val intent = TaskFormActivity.getIntent(mContext, null)
                mContext.startActivity(intent)
            }
        }

    }

    companion object {
        fun newInstance(taskFilter: Int): TaskListFragment {
            val args = Bundle()
            args.putInt(TaskConstants.KEY_FILTER, taskFilter)

            val fragment = TaskListFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
