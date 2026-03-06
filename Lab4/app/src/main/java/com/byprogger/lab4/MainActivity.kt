package com.byprogger.lab4

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper: SqlHelper = SqlHelper(this)
        val container = binding.container
        val btnAdd = binding.btnAdd
        val btnShow = binding.btnShow

        btnAdd.setOnClickListener {
            val id: Long = dbHelper.addStudent("Иванов Иван", "A", 11, 5f)
            if (id > 0) {
                Toast.makeText(this, "Студент добавлен с ID: $id", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
            }
        }

        btnShow.setOnClickListener {
            displayAllStudents(dbHelper, container)
        }

        displayAllStudents(dbHelper, container)
    }

    private fun displayAllStudents(dbHelper: SqlHelper, container: LinearLayout) {
        val students = dbHelper.getAllStudents()
        container.removeAllViews()

        if (students.isEmpty()) {
            val emptyView = TextView(this)
            emptyView.text = "Список студентов пуст"
            container.addView(emptyView)
            return
        }

        for (s in students) {
            val linearLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    350,
                )
            }

            val textView = TextView(this).apply {
                text = "${s.fio} : ${s.classNumber} '${s.className}', средний балл ${s.avgScore}"
                textSize = 16f
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val deleteButton = Button(this).apply {
                text = "Удалить"
                id = s.id
            }

            val updateButton = Button(this).apply {
                text = "Изменить"
                id = s.id
            }

            deleteButton.setOnClickListener {
                dbHelper.deleteStudent(it.id)
                Toast.makeText(this, "Удалено строка ${s.id}", Toast.LENGTH_SHORT).show()
            }

            updateButton.setOnClickListener {
                val result = dbHelper.updateStudent(Student(id = it.id,  fio = s.fio + " upd", className = s.className + " upd", classNumber = s.classNumber + 1, avgScore = s.avgScore / 0.4f))
                if (result > 0) {
                    Toast.makeText(this, "Обновлен студент ${s.fio}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Строка не найдена", Toast.LENGTH_SHORT).show()
                }
            }

            linearLayout.addView(textView)
            linearLayout.addView(deleteButton)
            linearLayout.addView(updateButton)

            container.addView(linearLayout)
        }
    }
}