package io.github.araujodanilo.sharedlist.controller

import com.google.firebase.auth.FirebaseAuth
import io.github.araujodanilo.sharedlist.model.Task
import io.github.araujodanilo.sharedlist.model.TaskDao
import io.github.araujodanilo.sharedlist.model.TaskDaoRtDbFb
import io.github.araujodanilo.sharedlist.view.MainActivity

class TaskController(private val mainActivity: MainActivity) {
private val taskDaoImpl: TaskDao = TaskDaoRtDbFb()

    fun insertTask(task: Task){
        Thread{
            taskDaoImpl.createTask(task)
        }.start()
    }

    fun getTask(tile: String) = taskDaoImpl.retrieveTask(tile)

    fun getTasks(){
        Thread{
            val list = taskDaoImpl.retrieveTasks()
            mainActivity.runOnUiThread{
                mainActivity.updateTaskList(list)
            }
        }.start()
    }

    fun editTask(task: Task){
        Thread{
            taskDaoImpl.updateTask(task)
        }.start()
    }

    fun removeTask(task: Task){
        Thread{
            taskDaoImpl.deleteTask(task)
        }.start()
    }

    fun finishTask(task: Task) {
        task.finish = true
        task.userFinish = FirebaseAuth.getInstance().currentUser?.email.toString()
        Thread{
            taskDaoImpl.updateTask(task)
        }.start()
    }
}