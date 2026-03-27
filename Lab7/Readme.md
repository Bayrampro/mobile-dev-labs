# Практическая работа №7 — Локализация и списки. Формы ввода и валидация данных

**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Изучить локализацию Android-приложений, работу со списками (`ListView`, `Spinner`), формы ввода и валидацию пользовательских данных с помощью регулярных выражений.

---

## Вариант

**Вариант:** список имен котов.

---

## Выполненные задания

| Пункт | Описание |
|------|----------|
| 1 | Реализована локализация строковых ресурсов для русского и английского языков |
| 2 | Список из ресурсов отображается в `ListView` через `ArrayAdapter` |
| 3 | Реализована форма регистрации с проверкой полей и подсветкой ошибок (`setError`) |
| 4 | Реализован выбор даты рождения через `DatePickerDialog` |
| 5 | Реализован `Spinner`, заполненный из массива ресурсов |
| 6 | Реализована проверка согласия на обработку данных |

---

## Ход работы

### 1. Локализация

Для поддержки двух языков использованы ресурсы:
- `res/values/strings.xml` — русский язык;
- `res/values-en/strings.xml` — английский язык.

Массив `items_array` вынесен в строки ресурсов, поэтому при смене языка устройства список автоматически берется из соответствующей локали.

### 2. Локализованный список (ListView)

В `CatsListActivity` список загружается так:
- чтение массива `resources.getStringArray(R.array.items_array)`;
- создание `ArrayAdapter`;
- установка адаптера в `ListView`.

### 3. Форма регистрации и валидация

В `RegistrationActivity` реализованы поля:
- ФИО;
- логин;
- email;
- телефон;
- пароль;
- повтор пароля;
- дата рождения;
- `Spinner`;
- `CheckBox` согласия.

Используются регулярные выражения и проверки:
- ФИО: `^[А-Яа-яЁё\\s-]+$`;
- логин: `^[A-Za-z]+$`;
- email: `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$`;
- телефон: `^\\+7\\d{10}$`;
- дата: `^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19|20)\\d{2}$`.

При ошибках:
- на поле ставится `setError(...)`;
- в Logcat пишется сообщение через `Log.w(...)`;
- регистрация не выполняется.

### 4. Выбор даты через DatePickerDialog

Дата рождения выбирается по нажатию на поле `dateOfBirth` через `DatePickerDialog`.
Выбранная дата подставляется в формате `ДД.ММ.ГГГГ`.

### 5. Spinner и согласие на обработку данных

`Spinner` заполняется из `items_array`, а `CheckBox` согласия обязательно должен быть отмечен для успешной регистрации.

---

## Ответы на контрольные вопросы

**1. Как в Android реализуется локализация?**  
Локализация делается через отдельные папки ресурсов: `values` (базовая), `values-en`, `values-ru` и т.д. Android автоматически выбирает ресурсы по текущей локали устройства.

**2. Для чего нужны адаптеры (`ArrayAdapter`) в `ListView`/`Spinner`?**  
Адаптер связывает источник данных (массив/список) с визуальным компонентом и отвечает за отображение каждого элемента в списке.

**3. Какие атрибуты `EditText` ограничивают тип ввода?**  
Основной атрибут — `android:inputType`, например: `textEmailAddress`, `phone`, `textPassword`, `number`, `date`.

**4. Что такое регулярные выражения и как проверить email?**  
Регулярные выражения — шаблоны для проверки строк. Для email можно использовать шаблон вида `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$`.

**5. Как программно показать ошибку в `EditText`?**  
Через метод `setError("Текст ошибки")`, например: `binding.email.error = "Неверный формат"`.

**6. Разница между `CheckBox` и `RadioGroup`?**  
`CheckBox` — независимый флаг (можно выбрать/снять любой).  
`RadioGroup` — взаимоисключающий выбор одного варианта из нескольких `RadioButton`.

**7. Как показать `DatePickerDialog` и получить дату?**  
Создать `DatePickerDialog(context, listener, year, month, day)`, вызвать `show()`, а выбранные значения получить в `listener` (`selectedYear`, `selectedMonth`, `selectedDay`).

**8. Для чего используется `String.matches()`? Что возвращает?**  
Проверяет, соответствует ли вся строка регулярному выражению. Возвращает `true` при полном совпадении, иначе `false`.

---

## Итог по требованиям

- Реализована локализация двух языков через ресурсы.
- Реализован локализованный список в `ListView`.
- Реализована форма регистрации с валидацией и выводом ошибок.
- Реализован выбор даты через `DatePickerDialog`.
- Реализован `Spinner` и проверка согласия на обработку данных.

---

## Вывод

В ходе работы закреплены навыки локализации Android-приложений, заполнения списков через адаптеры и проверки пользовательского ввода с помощью регулярных выражений. Также отработано использование `DatePickerDialog` и отображение ошибок в форме регистрации.

---

## Код самостоятельной работы (Kotlin + XML)

Ниже приведены файлы, относящиеся к самостоятельной части Lab7.

### `MainActivity.kt`

```kotlin
package com.byprogger.lab7

import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBtn.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
```

### `RegistrationActivity.kt`

```kotlin
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
```

### `CatsListActivity.kt`

```kotlin
package com.byprogger.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab7.databinding.ActivityCatsListBinding

class CatsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCatsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = resources.getStringArray(R.array.items_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val item = items[position]
            print(item)
        }
    }
}
```

### `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/navBtn"
        android:text="@string/nav"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</LinearLayout>
```

### `activity_registration.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:fitsSystemWindows="true"
    android:layout_height="match_parent">

    <LinearLayout
        android:id="@+id/main"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:gravity="center_horizontal"
        android:padding="16dp"
        tools:context=".RegistrationActivity">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/registrationText"
            android:textAlignment="center"
            android:textSize="40sp"
            android:textStyle="bold"/>

        <EditText
            android:id="@+id/fio"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:hint="@string/name_and_surname"/>

        <EditText
            android:id="@+id/login"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:hint="@string/login"/>


        <EditText
            android:id="@+id/email"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:inputType="textEmailAddress"
            android:hint="@string/email"/>


        <EditText
            android:id="@+id/phone"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:inputType="phone"
            android:hint="@string/phone_number"/>


        <EditText
            android:id="@+id/password"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:inputType="textPassword"
            android:hint="@string/password"/>


        <EditText
            android:id="@+id/confirmPassword"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:inputType="textPassword"
            android:hint="@string/confirm_password"/>


        <EditText
            android:id="@+id/dateOfBirth"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:focusable="false"
            android:clickable="true"
            android:drawableEnd="@android:drawable/ic_menu_my_calendar"
            android:hint="@string/date_of_birth"/>

        <Spinner
            android:id="@+id/spinner"
            android:layout_marginTop="16dp"
            android:layout_width="300dp"
            android:layout_height="60dp"/>

        <LinearLayout
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:orientation="horizontal">

            <CheckBox
                android:id="@+id/conf"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"/>

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/conf"/>

        </LinearLayout>

        <Button
            android:id="@+id/regButton"
            android:layout_width="300dp"
            android:layout_height="60dp"
            android:text="@string/registrationText" />

    </LinearLayout>

</ScrollView>
```

### `activity_cats_list.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:fitsSystemWindows="true"
    android:padding="16dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/list_title"
        android:textAlignment="center"
        android:textSize="24sp"
        android:textStyle="bold"/>

    <ListView
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

### `strings.xml (ru)`

```xml
<resources>
    <string name="app_name">Мое приложение</string>
    <string name="list_title">Мой список</string>
    <string-array name="items_array">
        <item>Джош</item>
        <item>Стивен</item>
        <item>Брюс</item>
    </string-array>
    <string name="name_and_surname">ФИО</string>
    <string name="login">Логин</string>
    <string name="email">Почта</string>
    <string name="phone_number">Номер телефона</string>
    <string name="password">Пароль</string>
    <string name="confirm_password">Повтор пароля</string>
    <string name="date_of_birth">Дата рождения</string>
    <string name="registrationText">Регистрация</string>
    <string name="conf">Согласие на обработку данных</string>
    <string name="nav">Перейти к регистрации</string>
</resources>
```

### `strings.xml (en)`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools">
    <string name="app_name">My Application</string>
    <string name="list_title">Мy List</string>
    <string-array name="items_array">
        <item>Josh</item>
        <item>Steven</item>
        <item>Bruce</item>
    </string-array>
    <string name="name_and_surname">Name</string>
    <string name="login">Login</string>
    <string name="email">Email</string>
    <string name="phone_number">Phone Number</string>
    <string name="password">Password</string>
    <string name="confirm_password">Confirm Password</string>
    <string name="date_of_birth">Date of birth</string>
    <string name="registrationText">Registration</string>
    <string name="conf">Consent to data processing</string>
    <string name="nav">Get started</string>
</resources>
```

