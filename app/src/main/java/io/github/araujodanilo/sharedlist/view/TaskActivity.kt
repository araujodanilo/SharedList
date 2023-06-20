package io.github.araujodanilo.sharedlist.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import io.github.araujodanilo.sharedlist.R
import io.github.araujodanilo.sharedlist.databinding.ActivityTaskBinding
import io.github.araujodanilo.sharedlist.model.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class TaskActivity : BaseActivity() {
    private val atb: ActivityTaskBinding by lazy{
        ActivityTaskBinding.inflate(layoutInflater)
    }
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)

        supportActionBar?.subtitle = getString(R.string.task_info)

        atb.createdDateEt.isEnabled = false
        atb.userEt.isEnabled = false
        atb.userFinishEt.isEnabled = false
        atb.createdDateEt.setText(LocalDate.now().format(dateFormatter))
        atb.userEt.setText(FirebaseAuth.getInstance().currentUser?.email)
        setClickOnDateInput()


        val receivedContact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }
        receivedContact?.let{ _receivedTask ->
            with(atb) {
                with(_receivedTask) {
                    titleEt.setText(title)
                    titleEt.isEnabled = false
                    descriptionEt.setText(description)
                    expectedDateEt.setText(expectedDate)
                    createdDateEt.setText(createdDate)
                    userEt.setText(user)
                    userFinishEt.setText(userFinish)
                }
            }
            val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
            with(atb) {
                titleEt.isEnabled = false
                descriptionEt.isEnabled = !viewTask
                expectedDateEt.isEnabled = !viewTask
                userFinishTv.visibility = if (userFinishEt.text.toString() == "") View.GONE else View.VISIBLE
                userFinishEt.visibility = if (userFinishEt.text.toString() == "") View.GONE else View.VISIBLE
                saveBt.visibility = if (viewTask) View.GONE else View.VISIBLE
            }
        }

        atb.saveBt.setOnClickListener {
            val task: Task = Task(
                id =  receivedContact?.id,
                title = atb.titleEt.text.toString(),
                description = atb.descriptionEt.text.toString(),
                expectedDate = atb.expectedDateEt.text.toString(),
                createdDate = atb.createdDateEt.text.toString(),
                user = atb.userEt.text.toString()
            )

            val errors = checkValues(task)
            if (errors.size == 0){
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_TASK, task)
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            Toast.makeText(this, "$errors estão ausentes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setClickOnDateInput(){
        atb.expectedDateEt.showSoftInputOnFocus = false

        atb.expectedDateEt.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, _year, monthOfYear, dayOfMonth ->
                    val date = ("$dayOfMonth/" +
                            if ((monthOfYear + 1) < 10) { "0" + (monthOfYear + 1)}
                            else { (monthOfYear + 1) }
                            + "/$_year")

                    if (checkDates(LocalDate.of(_year, monthOfYear + 1, dayOfMonth)))
                        atb.expectedDateEt.setText(date)

                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    private fun checkDates(date: LocalDate): Boolean {
        if (LocalDate.now().isAfter(date)) {
            Toast.makeText(this, "Data não pode ser menor que atual!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkValues(task: Task): MutableList<String> {
        val errors: MutableList<String> = mutableListOf()

        if (task.title == "")
            errors.add("Titulo")
        if (task.description == "")
            errors.add("Descrição")
        if (task.expectedDate == "")
            errors.add("Data esperada")
        if (task.createdDate == "")
            errors.add("Data de criação")
        if (task.user == "")
            errors.add("Usuário")

        return errors
    }
}