package io.github.araujodanilo.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import io.github.araujodanilo.sharedlist.R
import io.github.araujodanilo.sharedlist.adapter.OnTaskClickListener
import io.github.araujodanilo.sharedlist.adapter.TaskRvAdapter
import io.github.araujodanilo.sharedlist.controller.TaskController
import io.github.araujodanilo.sharedlist.databinding.ActivityMainBinding
import io.github.araujodanilo.sharedlist.model.Task

class MainActivity : BaseActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val taskList: MutableList<Task> = mutableListOf()

    // Adapter
    private val taskAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(taskList, this)
    }

    private lateinit var tarl: ActivityResultLauncher<Intent>

    // Controller
    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = getString(R.string.task_list)

        taskController.getTasks()
        amb.taskRv.layoutManager = LinearLayoutManager(this)
        amb.taskRv.adapter = taskAdapter

        tarl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra<Task>(EXTRA_TASK)
                }
                contact?.let{_task ->
                    val position = taskList.indexOfFirst { it.title == _task.title }
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        Toast.makeText(this, "Tarefa editada!", Toast.LENGTH_SHORT).show()
                    } else {
                        taskController.insertTask(_task)
                        Toast.makeText(this, "Tarefa adicionada!", Toast.LENGTH_SHORT).show()
                    }
                    taskController.getTasks()
                    taskAdapter.notifyDataSetChanged()
                }
            }
        }

        Thread{
            Thread.sleep(3000)
            taskController.getTasks()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addContactMi -> {
                tarl.launch(Intent(this, TaskActivity::class.java))
                true
            }

            R.id.refreshContactMi -> {
                taskController.getTasks()
                true
            }

            R.id.signOutMi -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                googleSignClient.signOut()
                finish()
                true
            }

            else -> false
        }
    }

    fun updateTaskList(_taskList: MutableList<Task>) {
        taskList.clear()
        taskList.addAll(_taskList)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onTileTaskClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this@MainActivity, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        taskIntent.putExtra(EXTRA_VIEW_TASK, true)
        startActivity(taskIntent)
    }

    override fun onEditMenuItemClick(position: Int) {
        val task = taskList[position]
        val contactIntent = Intent(this, TaskActivity::class.java)
        contactIntent.putExtra(EXTRA_TASK, task)
        tarl.launch(contactIntent)
    }


    override fun onRemoveMenuItemClick(position: Int) {
        val task = taskList[position]
        taskList.removeAt(position)
        taskController.removeTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Tarefa removida!", Toast.LENGTH_SHORT).show()
    }

    override fun onFinishTask(position: Int) {
        val task = taskList[position]
        taskList.removeAt(position)
        taskController.finishTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Tarefa concluida!", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser == null){
            Toast.makeText(this, "Não há usuário autenticado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}