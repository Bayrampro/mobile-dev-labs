# Практическая работа №5 — Дополнительные Activity (настройки и «Об авторе»)

**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Научиться работать с несколькими Activity в Android-приложении, передавать данные между экранами и применять выбранные настройки на главном экране.

---

## Вариант

**Вариант 2:** изменение цвета текста на главной странице (минимум 3 цвета).

---

## Постановка задачи

Требовалось реализовать приложение с:
- главным экраном;
- экраном настроек;
- экраном «Об авторе»;
- применением выбранной настройки на главном экране.

---

## Выполненные задания

| Пункт | Описание |
|------|----------|
| 1 | Добавлены две дополнительные Activity: `SettingsActivity` и `AboutActivity` |
| 2 | На экране настроек реализован выбор из 3 цветов текста (`красный`, `зелёный`, `синий`) |
| 3 | Настройка передается обратно на главный экран через `ActivityResultLauncher` |
| 4 | На главном экране применяется выбранный цвет текста к `TextView` |
| 5 | На экране «Об авторе» отображены данные студента и кнопка возврата |

---

## Ход работы

### 1. Структура приложения

В проекте используются три экрана:
- `MainActivity` — главный экран с кнопками `Настройки` и `Об авторе`;
- `SettingsActivity` — выбор цвета текста;
- `AboutActivity` — информация об авторе и кнопка `Назад`.

Для работы с элементами интерфейса используется `ViewBinding`.

---

### 2. Экран настроек (`SettingsActivity`)

В `activity_settings.xml` добавлен `RadioGroup` с тремя `RadioButton`:
- `Красный`;
- `Зелёный`;
- `Синий`.

По нажатию кнопки `Сохранить` выбранное значение записывается в `Intent`:

```kotlin
resultIntent.putExtra("COLOR", selectedColor)
setResult(RESULT_OK, resultIntent)
finish()
```

---

### 3. Возврат результата в `MainActivity`

Главный экран открывает `SettingsActivity` через `ActivityResultLauncher`:

```kotlin
private val settingsLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result -> ... }
```

После возврата читается значение `COLOR` и вызывается метод применения настройки.

---

### 4. Применение настройки на главной странице

Выбранный цвет применяется к тексту `TextView` (`tvResult`) на главном экране:

```kotlin
private fun applyBackgroundColor(color: String) {
    when (color) {
        "red" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_red_light))
        "green" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_green_light))
        "blue" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_blue_light))
    }
}
```

Тем самым требование варианта 2 (смена цвета текста, не менее 3 цветов) выполнено.

---

### 5. Экран «Об авторе» (`AboutActivity`)

Реализован отдельный экран с:
- ФИО;
- группой;
- кнопкой `Назад`, которая закрывает экран (`finish()`).

---

## Итог по требованиям

- Реализованы две дополнительные Activity.
- Настройки выбираются на отдельном экране и применяются на главном.
- Для варианта 2 реализована смена цвета текста на 3 значения.
- Экран «Об авторе» содержит данные и возврат на главный экран.

---

## Вывод

В ходе практической работы закреплены навыки навигации между Activity, передачи данных через результат Activity и динамического изменения UI по пользовательским настройкам. Практически отработан сценарий «выбор в настройках -> возврат -> применение на главном экране».

---

## Ответы на контрольные вопросы

**1. Что такое Intent? Какие существуют типы Intent (явные и неявные)? Приведите примеры.**  
`Intent` — это объект сообщения в Android для запуска компонентов (Activity, Service) и передачи данных.  
Явный (`explicit`) Intent указывает конкретный класс, например:
`Intent(this, SettingsActivity::class.java)`.  
Неявный (`implicit`) Intent описывает действие без конкретного класса, например:
`Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"))`.

**2. Как передать данные из одной Activity в другую через Intent? Какие ограничения по типам?**  
Данные передаются через `putExtra(...)`, принимаются через `get...Extra(...)`.  
Поддерживаются примитивы, `String`, `Bundle`, массивы, а также объекты `Parcelable`/`Serializable`.  
Ограничение: данные должны быть сериализуемыми между процессами и не слишком большими (не стоит передавать крупные объекты, лучше хранить их в БД/файлах).

**3. Разница между `startActivity()` и `startActivityForResult()`?**  
`startActivity()` — просто открывает экран без ожидания результата.  
`startActivityForResult()` (устаревший API) открывает экран с возвратом данных.  
Современная замена — `ActivityResultLauncher` + `registerForActivityResult`, что и используется в Lab5.

**4. Назначение `setResult()` и `finish()` при возврате данных из дочерней Activity.**  
`setResult(...)` устанавливает код результата и `Intent` с данными для родительской Activity.  
`finish()` закрывает дочернюю Activity и возвращает управление обратно, передавая ранее установленный результат.

**5. Что будет, если не зарегистрировать Activity в `AndroidManifest.xml`?**  
При попытке открыть такую Activity приложение завершится с ошибкой времени выполнения (Activity не будет найдена системой).

**6. Какие методы жизненного цикла вызываются при переходе MainActivity -> SettingsActivity и обратно?**  
При открытии `SettingsActivity`:  
`MainActivity.onPause()` -> `SettingsActivity.onCreate()` -> `SettingsActivity.onStart()` -> `SettingsActivity.onResume()` -> `MainActivity.onStop()` (если полностью перекрыта).  
При возврате назад:  
`SettingsActivity.onPause()` -> `MainActivity.onRestart()` -> `MainActivity.onStart()` -> `MainActivity.onResume()` -> `SettingsActivity.onStop()` -> `SettingsActivity.onDestroy()`.

**7. Для чего нужен `requestCode` в `startActivityForResult()`? Как обрабатывать несколько запросов?**  
`requestCode` нужен, чтобы понять, из какого именно запроса пришел результат.  
В `onActivityResult()` обычно выполняют проверку `requestCode` и `resultCode`, чтобы раздельно обработать несколько сценариев возврата данных.  
В новом API (`ActivityResultLauncher`) обычно создают отдельные launcher-обработчики под разные запросы.

---

## Полный код (Kotlin + XML)

Ниже приведен только код самостоятельной части (без дополнительных демонстрационных файлов).

### `MainActivity.kt`

```kotlin
package com.byprogger.lab5

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult

        val color = result.data?.getStringExtra("COLOR") ?: return@registerForActivityResult
        applyBackgroundColor(color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnSettings = binding.btnSettings
        val btnAbout = binding.btnAbout

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            settingsLauncher.launch(intent)
        }

        btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun applyBackgroundColor(color: String) {
        when (color) {
            "red" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_red_light))
            "green" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_green_light))
            "blue" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_blue_light))
        }
    }
}
```

### `SettingsActivity.kt`

```kotlin
package com.byprogger.lab5

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab5.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val radioGroupColor = binding.radioGroupColor
        val  btnSave = binding.btnSave

        btnSave.setOnClickListener {
            val selectedId: Int = radioGroupColor.checkedRadioButtonId
            var selectedColor = "red"

            if (selectedId == binding.radioGreen.id) {
                selectedColor = "green"
            } else if (selectedId == binding.radioBlue.id) {
                selectedColor = "blue"
            }

            val resultIntent = Intent()
            resultIntent.putExtra("COLOR", selectedColor)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
```

### `AboutActivity.kt`

```kotlin
package com.byprogger.lab5

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab5.databinding.ActivityAboutBinding
import com.byprogger.lab5.databinding.ActivitySettingsBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnBack = binding.btnBack

        btnBack.setOnClickListener {
            finish()
        }
    }
}
```

### `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:fitsSystemWindows="true"
    android:padding="16dp"
    tools:context=".MainActivity">


    <TextView
        android:id="@+id/tvMain"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Главный экран"
        android:textSize="24sp"
        android:layout_gravity="center_horizontal"
        android:layout_marginHorizontal="16dp"
        android:layout_marginBottom="32dp" />


    <Button
        android:id="@+id/btnSettings"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:text="Настройки" />


    <Button
        android:id="@+id/btnAbout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:text="Об авторе"
        android:layout_marginTop="8dp" />


    <TextView
        android:id="@+id/tvResult"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:text="Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
        android:layout_marginTop="32dp"/>

</LinearLayout>
```

### `activity_settings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:fitsSystemWindows="true"
    tools:context=".SettingsActivity">

    <RadioGroup
        android:id="@+id/radioGroupColor"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginBottom="16dp">
        
        <RadioButton
            android:id="@+id/radioRed"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:checked="true"
            android:layout_marginHorizontal="16dp"
            android:text="Красный" />

        <RadioButton
            android:id="@+id/radioGreen"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="16dp"
            android:text="Зелёный" />


        <RadioButton
            android:id="@+id/radioBlue"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="16dp"
            android:text="Синий" />

    </RadioGroup>
    
    <Button
        android:id="@+id/btnSave"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:text="Сохранить" />

</LinearLayout>
```

### `activity_about.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:fitsSystemWindows="true"
    android:padding="16dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Об авторе"
        android:textSize="24sp"
        android:layout_gravity="center_horizontal"
        android:layout_marginHorizontal="16dp"
        android:layout_marginBottom="32dp"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="ФИО: Аннагурбанов Байрам"
        android:layout_marginHorizontal="16dp"
        android:textSize="18sp"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Группа: ИНС-б-о-24-2"
        android:layout_marginHorizontal="16dp"
        android:textSize="18sp"
        android:layout_marginTop="8dp"/>

    <!-- Можно добавить ImageView для фото -->

    <Button
        android:id="@+id/btnBack"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Назад"
        android:layout_gravity="center_horizontal"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="32dp"/>

</LinearLayout>
```

