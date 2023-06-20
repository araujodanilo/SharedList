package io.github.araujodanilo.sharedlist.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class TaskDaoRtDbFb: TaskDao {
    private val SHARED_TASK_LIST_ROOT = "sharedTaskList"
    private val taskRtDbFbReference = Firebase.database.getReference(SHARED_TASK_LIST_ROOT)

    private  val taskList: MutableList<Task> = mutableListOf()
    init {
        taskRtDbFbReference.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()

                task?.let { _task ->
                    if (!taskList.any{ _task.title == it.title}){
                        taskList.add(_task)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()

                task?.let { _contact ->
                    taskList[taskList.indexOfFirst { _contact.title == it.title }] = _contact
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task: Task? = snapshot.getValue<Task>()

                task?.let { _contact ->
                    taskList.remove(_contact)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // NSA
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })

        taskRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskHashMap = snapshot.getValue<HashMap<String, Task>>()
                taskList.clear()
                taskHashMap?.values?.forEach {
                    taskList.add(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })
    }

    override fun createTask(contact: Task) {
        createOrUpdateTask(contact)
    }

    override fun retrieveTask(title: String): Task? {
        return taskList[taskList.indexOfFirst { title == it.title }]
    }

    override fun retrieveTasks(): MutableList<Task> {
        return taskList
    }

    override fun updateTask(contact: Task): Int {
        createOrUpdateTask(contact)
        return 1
    }

    override fun deleteTask(contact: Task): Int {
        taskRtDbFbReference.child(contact.title).removeValue()
        return 1
    }

    private fun createOrUpdateTask(task: Task) = taskRtDbFbReference.child(task.title).setValue(task)
}