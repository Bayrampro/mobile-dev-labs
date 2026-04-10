# Практическая работа №11 — Многопоточность в Android

**Тема:** Асинхронная загрузка данных  
**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Изучить принципы многопоточности в Android, научиться выносить длительные вычисления и сетевые операции в фоновые потоки, а также безопасно обновлять UI из фоновых задач.

---

## Вариант

**Вариант 2:**  
Для одномерного массива вещественных чисел требуется вычислить:
- сумму положительных элементов;
- произведение элементов между максимальным по модулю и минимальным по модулю элементами.

Дополнительно требуется асинхронная загрузка изображений с отображением прогресса.

---

## Выполненные задания

| Пункт | Реализация |
|------|------------|
| 1 | Добавлены кнопки запуска длительных вычислений (в UI-потоке и в отдельном потоке) |
| 2 | Реализован фоновый поток через `Thread` |
| 3 | Реализовано обновление интерфейса из фонового потока через `runOnUiThread` |
| 4 | Добавлена загрузка изображения из интернета в фоновом потоке |
| 5 | Добавлен `ProgressBar` для отображения прогресса загрузки |
| 6 | Результат загрузки отображается в `ImageView` |

---

## Ход работы

### 1. Интерфейс

В `activity_main.xml` добавлены:
- кнопка `Вычислить (без потоков)`;
- кнопка `Вычислить (с потоком)`;
- кнопка `Загрузить изображение`;
- `ProgressBar` для общего прогресса;
- `ImageView` для результата загрузки.

### 2. Многопоточность

В `MainActivity` реализованы два сценария вычислений:
- без потоков (для демонстрации блокировки интерфейса);
- в отдельном потоке (`Thread`) без блокировки UI.

После завершения вычислений выводится `Toast`.

### 3. Асинхронная загрузка данных

По нажатию `Загрузить изображение`:
- показывается `ProgressBar`;
- в фоновом потоке выполняется сетевой запрос по URL;
- прогресс периодически обновляется;
- после загрузки bitmap отображается в `ImageView`;
- при ошибке показывается сообщение `Ошибка загрузки`.

### 4. Работа с сетью

Для загрузки изображения используется `URL.openConnection()` и `BitmapFactory.decodeStream(...)`.  
В `AndroidManifest.xml` добавлено разрешение `INTERNET`.

---

## Ответы на контрольные вопросы

**1. Что такое главный (UI) поток? Почему нельзя выполнять длительные операции в нём?**  
UI-поток обрабатывает отрисовку интерфейса и события пользователя. Если выполнять в нём долгие вычисления или сеть, интерфейс зависает и перестает реагировать.

**2. Что такое ANR (Application Not Responding)? При каких условиях возникает?**  
ANR — состояние, когда приложение слишком долго не отвечает на действия пользователя. Обычно возникает, если главный поток заблокирован длительной операцией (например, тяжелый цикл или сеть в UI-потоке).

**3. Как создать новый поток в Java? Как запустить выполнение кода в этом потоке?**  
Через `Thread { ... }.start()` или через класс `Runnable` + `Thread`. Код внутри блока выполняется в отдельном потоке после вызова `start()`.

**4. Почему нельзя обновлять UI из фонового потока напрямую? Как правильно обновить интерфейс?**  
UI-компоненты Android не потокобезопасны и должны изменяться только из главного потока.  
Правильно: `runOnUiThread { ... }`, `Handler(Looper.getMainLooper())`, `LiveData`, корутины с `Dispatchers.Main`.

**5. Для чего используется класс Handler? Как отправить сообщение в UI-поток?**  
`Handler` ставит задачи в очередь потока, к которому привязан `Looper`.  
Для UI: создать `Handler(Looper.getMainLooper())` и вызвать `post { ... }` для обновления интерфейса.

**6. Что такое ExecutorService? В чём преимущество перед созданием потоков вручную?**  
`ExecutorService` — API для пула потоков и управления задачами. Преимущества: повторное использование потоков, очередь задач, контроль завершения и отмены, меньше накладных расходов, чем частое создание `Thread`.

**7. Почему AsyncTask считается устаревшим? Какие альтернативы использовать?**  
`AsyncTask` устарел из-за проблем с жизненным циклом, утечками и ограниченной гибкостью.  
Альтернативы: Kotlin Coroutines, `ExecutorService`, `WorkManager`, RxJava (по проекту).

**8. Как отобразить прогресс длительной операции через ProgressBar?**  
Показать `ProgressBar` перед стартом задачи, периодически обновлять `progress` из UI-потока, и скрыть/остановить индикатор после завершения.

---

## Итог

- Реализована работа с фоновыми потоками в Android.
- Продемонстрирована разница между выполнением в UI-потоке и `Thread`.
- Реализована асинхронная загрузка изображения из сети.
- Настроено отображение прогресса и безопасное обновление UI.

---

## Вывод

В ходе работы закреплены практические навыки многопоточного программирования в Android: запуск фоновых задач, обработка сетевых операций, отображение прогресса и обновление интерфейса без блокировки главного потока.

---

## Код самостоятельной работы (Kotlin + XML)

Ниже приведен код самостоятельной части.

### `MainActivity.kt`

```kotlin
package com.byprogger.lab11

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab11.databinding.ActivityMainBinding
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener {
            longCalculation()
            Toast.makeText(this, "Вычисления завершены", Toast.LENGTH_SHORT).show()
        }

        binding.btnCalculateThread.setOnClickListener {
            Thread {
                longCalculation()
                runOnUiThread {
                    Toast.makeText(this, "Вычисления завершены", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }

        binding.btnLoadImage.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar.progress = 0

            Thread {
                try {
                    for (i in 0..<100 step 10) {
                        Thread.sleep(200)
                        runOnUiThread {
                            binding.progressBar.progress = i
                        }
                    }

                    val bitmap = loadImage("https://el.ncfu.ru/pluginfile.php/1/theme_moove/logo/1769692740/%D0%A1%D0%9A%D0%A4%D0%A3%20%D1%81%D0%B5%D0%B2%D0%B5%D1%80%D0%BE%D0%BA%D0%B0%D0%B2%D0%BA%D0%B0%D0%B7%D1%81%D0%BA%D0%B8%D0%B9%D1%84%D0%B5%D0%B4%D0%B5%D1%80%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9%D1%83%D0%BD%D0%B8%D0%B2%D0%B5%D1%80%D1%81%D0%B8%D1%82%D0%B5%D1%82.png")
                    runOnUiThread {
                        binding.imageView.setImageBitmap(bitmap)
                        binding.progressBar.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    private fun longCalculation() {
        var result: Long = 0
        for (i in 0..<5000000000L) {
            result += i
        }
        Log.d(tag, "Результат: $result")
    }

    private fun loadImage(urlString: String) : Bitmap {
        val url = URL(urlString)
        val connection = url.openConnection()
        connection.doInput = true
        connection.connect()
        val input = connection.getInputStream()
        val bitmap = BitmapFactory.decodeStream(input)
        input.close()
        return bitmap
    }
}
```

### `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>

    <LinearLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:fitsSystemWindows="true"
        android:padding="16dp">

        <Button
            android:id="@+id/btnCalculate"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Вычислить (без потоков)"
            android:layout_marginBottom="16dp"/>

        <Button
            android:id="@+id/btnCalculateThread"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Вычислить (с потоком)"
            android:layout_marginBottom="16dp"/>

        <Button
            android:id="@+id/btnLoadImage"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Загрузить изображение"
            android:layout_marginBottom="16dp"/>

        <ProgressBar
            android:id="@+id/progressBar"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:max="100"
            android:visibility="gone"
            android:layout_marginBottom="16dp"/>

        <ImageView
            android:id="@+id/imageView"
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:layout_gravity="center_horizontal"
            android:src="@drawable/ic_launcher_foreground"/>

    </LinearLayout>
```

### `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Lab11">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
