package io.github.araujodanilo.sharedlist.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.github.araujodanilo.sharedlist.databinding.TileTaskBinding
import io.github.araujodanilo.sharedlist.model.Task

class TaskRvAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
): RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(tileTaskBinding: TileTaskBinding):
        RecyclerView.ViewHolder(tileTaskBinding.root), OnCreateContextMenuListener {
        val title: TextView = tileTaskBinding.titleTv
        var taskPosition = -1

        init {
            tileTaskBinding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 0, 0, "Visualizar")?.setOnMenuItemClickListener {
                if (taskPosition != -1)
                    onTaskClickListener.onTileTaskClick(taskPosition)
                true
            }
            if (!taskList[taskPosition].finish){
                menu?.add(Menu.NONE, 0, 0, "Concluir")?.setOnMenuItemClickListener {
                    if (taskPosition != -1)
                        onTaskClickListener.onFinishTask(taskPosition)
                    true
                }
                menu?.add(Menu.NONE, 0, 0, "Editar")?.setOnMenuItemClickListener {
                    if (taskPosition != -1)
                        onTaskClickListener.onEditMenuItemClick(taskPosition)
                    true
                }
                menu?.add(Menu.NONE, 1, 1, "Remover")?.setOnMenuItemClickListener {
                    if (taskPosition != -1) {
                        onTaskClickListener.onRemoveMenuItemClick(taskPosition)
                    }
                    true
                }
            }
        }
    }


    override fun getItemCount(): Int = taskList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))
        val taskViewHolder = TaskViewHolder(tileTaskBinding)
        return taskViewHolder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.title.text = task.title
        holder.taskPosition = position

        holder.itemView.setOnClickListener{
            onTaskClickListener.onTileTaskClick(position)
        }
    }
}