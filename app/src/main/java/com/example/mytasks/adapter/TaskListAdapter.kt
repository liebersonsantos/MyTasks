package com.example.mytasks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasks.R
import com.example.mytasks.adapter.TaskListAdapter.TaskListViewHolder
import com.example.mytasks.entities.TaskEntity
import com.example.mytasks.repository.PriorityCacheConstants
import kotlinx.android.synthetic.main.row_task_list.view.*

class TaskListAdapter(
    val taskList: MutableList<TaskEntity>,
    val itemClickListener: ((task: TaskEntity) -> Unit)?
) : RecyclerView.Adapter<TaskListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_task_list, parent, false)
        return TaskListViewHolder(itemView, itemClickListener)
    }

    override fun getItemCount(): Int {
        return taskList.count()
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.bind(taskList[position])
    }


    class TaskListViewHolder(
        itemView: View,
        val itemClickListener: ((task: TaskEntity) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {

        private val description = itemView.textDescription
        private val priority = itemView.textPriority
        private val imageTask = itemView.imageTask
        private val dueDate = itemView.textDueDate

        fun bind(taskList: TaskEntity){
            description.text = taskList.description
            priority.text = PriorityCacheConstants.getPriorityDescription(taskList.id)
            dueDate.text = taskList.dueDate

            if (taskList.complete) imageTask.setImageResource(R.drawable.ic_done)

            itemView.setOnClickListener {
                itemClickListener?.invoke(taskList)

            }


        }

    }
}