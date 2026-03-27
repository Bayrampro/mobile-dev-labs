package com.byprogger.lab7

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab7.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    val tag = "RegistrationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = resources.getStringArray(R.array.items_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        binding.spinner.adapter = adapter

        val color = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                Color.GREEN,
                Color.BLUE,
            )
        )


        binding.conf.buttonTintList = color

        binding.dateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _,
                  selectedYear, selectedMonth, selectedDay ->
                    var finalMonth = (selectedMonth + 1).toString()
                    if ((selectedMonth + 1) < 10) {
                        finalMonth = "0${selectedMonth + 1}"
                    }
                    binding.dateOfBirth.setText("$selectedDay.$finalMonth.$selectedYear")
                },
                year, month, day,
            )

            datePicker.show()
        }

        binding.regButton.setOnClickListener {
            onRegisterClick()
        }

        binding.conf.setOnClickListener {
            binding.conf.buttonTintList = color
        }
    }

    private fun onRegisterClick() {
        val fio = binding.fio.text.toString().trim()
        val login = binding.login.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phone = binding.phone.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()
        val birthDay = binding.dateOfBirth.text.toString().trim()
        val isAgree = binding.conf.isChecked

        var isValid = true

        if (!fio.matches(Regex("^[А-Яа-яЁё\\s-]+$"))) {
            binding.fio.error = "Только русские буквы, пробелы и дефис"
            Log.w(tag, "Неверный формат ФИО: $fio");
            isValid = false
        }

        if (!login.matches(Regex("^[A-Za-z]+$"))) {
            binding.login.error = "Только латинские буквы"
            Log.w(tag, "Неверный формат логина: $login")
            isValid = false
        }

        if (!email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            binding.email.error = "Неверный формат email"
            Log.w(tag, "Неверный формат email: $email")
            isValid = false;
        }

        if (!phone.matches(Regex("^\\+7\\d{10}$"))) {
            binding.phone.error = "Формат: +7XXXXXXXXXX"
            Log.w(tag, "Неверный формат телефона: $phone")
            isValid = false;
        }

        if (password.length < 6) {
            binding.password.error = "Пароль должен быть не менее 6 символов"
            Log.w(tag, "Слишком короткий пароль")
            isValid = false
        }

        if (password != confirmPassword) {
            binding.password.error = "Пароли не совпадают"
            Log.w(tag, "Пароли не совпадают")
            isValid = false
        }

        if (!birthDay.matches(Regex("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19|20)\\d{2}$"))) {
            binding.dateOfBirth.error = "Формат: ДД.ММ.ГГГГ"
            Log.w(tag, "Неверный формат даты: $birthDay")
            isValid = false
        }

        if (!isAgree) {
            binding.conf.buttonTintList =
                ColorStateList.valueOf(Color.RED)
            Log.w(tag, "Согласие не отмечено");
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_LONG).show()
            val intent = Intent(this, CatsListActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Исправьте ошибки в форме", Toast.LENGTH_LONG).show()
        }
    }
}