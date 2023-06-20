package io.github.araujodanilo.sharedlist.model

import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    fun createTask(task: Task)
    @Query("SELECT * FROM Task WHERE title = :title")
    fun retrieveTask(title: String): Task?
    @Query("SELECT * FROM Task")
    fun retrieveTasks(): MutableList<Task>
    @Update
    fun updateTask(contact: Task): Int
    @Delete
    fun deleteTask(contact: Task): Int
}