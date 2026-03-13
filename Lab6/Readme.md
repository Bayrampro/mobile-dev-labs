# Практическая работа №6 — Отладка приложений. Logcat и таймеры

**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Освоить использование таймеров в Android для периодических вычислений, научиться выводить диагностическую информацию в `Logcat` и обновлять результат на экране в реальном времени.

---

## Вариант

**Вариант 2: Последовательность Фибоначчи**  
Каждую секунду в течение минуты выводить следующий элемент последовательности, начиная с `1`.

---

## Требования задания

- Одна Activity.
- `TextView` для отображения текущего результата.
- Кнопка `Старт` для запуска таймера.
- Период выполнения: 1 секунда.
- Логирование промежуточных значений в `Logcat` с тегом `Lab6`.

---

## Выполненная реализация

| Пункт | Реализация |
|------|------------|
| Таймер | Использован `Timer`/`TimerTask` с шагом 1 секунда |
| Вычисления | Последовательный расчет чисел Фибоначчи |
| Логирование | На каждом шаге вызывается `Log.i("Lab6", ...)` |
| Отображение | Текущее значение выводится в `TextView` |
| Запуск | Вычисления запускаются по нажатию кнопки `Старт` |

---

## Ход работы

### 1. Создание интерфейса

На экране размещены:
- `TextView` для текущего значения;
- кнопка `Старт` для запуска вычислений.

### 2. Запуск периодической задачи

При нажатии `Старт` запускается таймер с периодом 1 секунда.  
Каждый тик выполняет один шаг вычисления следующего числа Фибоначчи.

### 3. Логирование и обновление UI

На каждом шаге:
- значение логируется через `Log.i("Lab6", "...")`;
- текущее число отображается в `TextView`.

### 4. Ограничение по времени

Таймер выполняется в течение 60 шагов (1 минута), после чего прекращает вычисления.

---

## Ответы на контрольные вопросы

**1. Какие уровни логирования существуют в Android? Для чего используется каждый?**  
Основные уровни: `Verbose (Log.v)`, `Debug (Log.d)`, `Info (Log.i)`, `Warn (Log.w)`, `Error (Log.e)`, `Assert (Log.wtf)`.  
`Verbose/Debug` — детальная отладка, `Info` — важные рабочие события, `Warn` — потенциальные проблемы, `Error` — ошибки, `wtf` — критические состояния.

**2. Как открыть Logcat и отфильтровать только по тегу и только по уровню Error?**  
В Android Studio: `View` -> `Tool Windows` -> `Logcat`.  
Далее выбрать устройство/процесс, в фильтре указать тег (например, `Lab6`) и уровень `Error`.

**3. Разница между Log.e() и Log.w(). Примеры.**  
`Log.w()` — предупреждение, когда проблема возможна, но приложение продолжает работу.  
`Log.e()` — реальная ошибка, из-за которой функциональность уже работает некорректно.  
Пример: `Log.w("Lab6", "Данные отсутствуют, использую значение по умолчанию")`;  
`Log.e("Lab6", "Ошибка парсинга ответа сервера")`.

**4. Что такое breakpoint? Как запустить отладку?**  
`Breakpoint` — точка останова, на которой выполнение кода приостанавливается.  
Устанавливается кликом по левому полю строки кода. Отладка запускается кнопкой `Debug` (иконка жука) или `Shift+F9`.

**5. Как выполнить код с задержкой? Не менее двух способов.**  
Способы:
- `Timer` + `TimerTask`;
- `Handler.postDelayed(...)`;
- `CountDownTimer`;
- Kotlin coroutines (`delay(...)`).

**6. В чем проблема обновления UI из TimerTask? Как решить?**  
`TimerTask` выполняется в фоновом потоке, а UI в Android можно менять только из главного потока.  
Решение: использовать `runOnUiThread { ... }`, `Handler(Looper.getMainLooper())`, `lifecycleScope`/корутины.

**7. Для чего нужен Chronometer? Основные методы.**  
`Chronometer` — виджет для отображения прошедшего времени.  
Основные методы: `setBase(...)`, `start()`, `stop()`, а также `setOnChronometerTickListener(...)`.

**8. Чем CountDownTimer отличается от Timer? Когда удобнее CountDownTimer?**  
`CountDownTimer` ориентирован на обратный отсчет с `onTick()` и `onFinish()`, проще для таймеров UI (осталось N секунд).  
`Timer` — более общий механизм планирования задач.  
Для экранов с обратным отсчетом удобнее `CountDownTimer`, для произвольных периодических фоновых задач — `Timer`.

---

## Итог по требованиям

- Реализован запуск периодических вычислений по кнопке `Старт`.
- Результат отображается в `TextView`.
- Промежуточные значения логируются в `Logcat`.
- Период вычислений составляет 1 секунду.
- Выполняется вариант 2: последовательность Фибоначчи.

---

## Вывод

В ходе практической работы закреплены навыки отладки Android-приложений через `Logcat` и работы с таймерами. Получен практический опыт организации периодических вычислений, безопасного обновления UI и контроля выполнения задач по времени.

---

## Полный код (Kotlin + XML)

Ниже приведен только код самостоятельной части (вариант 2: последовательность Фибоначчи).

### `Task.kt`

```kotlin
package com.byprogger.lab6

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab6.databinding.ActivityTaskBinding
import java.util.Timer
import java.util.TimerTask

class Task : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private var v1 = 0
    private var v2 = 1
    private var result = 0
    private var counter = 0;
    private var isRunning = false
    private val tag = "Task"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var timer = Timer()
        lateinit var timerTask: TimerTask

        binding.btnStart.setOnClickListener {
            isRunning = true
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        if (counter == 60) {
                            timer.cancel()
                            isRunning = false
                            return@runOnUiThread
                        }
                        if (counter < 1) {
                            fib()
                            return@runOnUiThread
                        }
                        fib()
                        v1 = v2
                        v2 = result
                    }
                }
            }

            timer.schedule(timerTask, 1000, 1000)
        }

        binding.btnStop.setOnClickListener {
            if (!isRunning) return@setOnClickListener
            timerTask.cancel()
            timer.cancel()
            isRunning = false
        }
    }

    private fun fib() {
        if (result != 0) {
            Log.i(tag, result.toString())
            if (result == 1) {
                binding.tvResult.text = "$result"
            } else {
                binding.tvResult.text = "${binding.tvResult.text} $result"
            }
        }
        result = v1 + v2
        counter++
    }
}
```

### `activity_task.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:padding="16dp"
    android:fitsSystemWindows="true"
    tools:context=".StopwatchActivity">

    <Button
        android:id="@+id/btnStart"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Старт" />

    <Button
        android:id="@+id/btnStop"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Стоп" />

    <TextView
        android:id="@+id/tvResult"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Здесь пока пусто"
        android:textSize="24sp"
        android:layout_marginHorizontal="20dp"
        android:layout_marginTop="16dp"/>

</LinearLayout>
```

