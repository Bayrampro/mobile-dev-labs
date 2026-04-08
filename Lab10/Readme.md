# Практическая работа №10 — Использование аппаратных возможностей устройства

**Тема:** Разрешения, уведомления, вибрация, камера  
**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Изучить работу с разрешениями в Android, реализовать отложенные напоминания через `AlarmManager`, отображение `Notification` и вибрацию устройства в момент срабатывания напоминания.

---

## Вариант

**Вариант 2:** приложение для создания задач с указанием времени.  
В указанное время отправляется уведомление с вибрацией.

---

## Выполненные задания

| Пункт | Реализация |
|------|------------|
| 1 | Создан экран с вводом текста задачи (`EditText`) |
| 2 | Добавлен выбор времени (`TimePicker`) |
| 3 | Кнопка `Запланировать` создает отложенное напоминание |
| 4 | Напоминание планируется через `AlarmManager` + `PendingIntent` |
| 5 | При срабатывании будильника `BroadcastReceiver` показывает уведомление |
| 6 | Для уведомления включена вибрация (паттерн) |
| 7 | Добавлен runtime-запрос разрешения `POST_NOTIFICATIONS` (Android 13+) |
| 8 | Учтена работа с точными будильниками (`SCHEDULE_EXACT_ALARM`) |

---

## Ход работы

### 1. Интерфейс

В `activity_main.xml` размещены:
- поле ввода текста задачи;
- `TimePicker` для выбора времени;
- кнопка `Запланировать`.

### 2. Планирование напоминания

В `MainActivity` по нажатию кнопки:
- проверяется, что текст задачи не пустой;
- считываются часы и минуты из `TimePicker`;
- формируется время срабатывания через `Calendar`;
- если время уже прошло, дата переносится на следующий день;
- создается `PendingIntent` на `AlarmReceiver`;
- будильник ставится через `setExactAndAllowWhileIdle(...)` (или fallback на старых API).

### 3. Разрешения

Реализована проверка и запрос `POST_NOTIFICATIONS` для Android 13+.

Для Android 12+ добавлена проверка `canScheduleExactAlarms()`.  
Если точные будильники недоступны, открывается системный экран выдачи доступа к exact alarms.

### 4. Уведомление и вибрация

В `AlarmReceiver`:
- создается `NotificationChannel` (Android 8.0+);
- собирается уведомление с заголовком и текстом задачи;
- задается высокий приоритет;
- добавляется паттерн вибрации;
- уведомление отображается через `NotificationManager`.

---

## Ответы на контрольные вопросы

**1. В чём разница между нормальными и опасными разрешениями?**  
Нормальные разрешения выдаются автоматически при установке (например, `INTERNET`, `VIBRATE`).  
Опасные требуют явного согласия пользователя во время выполнения (например, `CAMERA`, `READ_CONTACTS`, `POST_NOTIFICATIONS` на Android 13+).

**2. Как запросить опасное разрешение во время выполнения?**  
Проверить через `ContextCompat.checkSelfPermission(...)`.  
Если не выдано, вызвать `ActivityCompat.requestPermissions(...)` (или `ActivityResultContracts.RequestPermission`).  
После ответа пользователя обработать результат и выполнять функционал только при `GRANTED`.

**3. Для чего нужен NotificationChannel в Android 8.0+?**  
Канал задает категорию уведомлений (звук, вибрация, важность), которую пользователь может настраивать.  
Без канала уведомления на Android 8.0+ не показываются.

**4. Как создать простое уведомление и отобразить его?**  
Создать `NotificationCompat.Builder(channelId)`, задать `title`, `text`, `smallIcon`, затем вызвать `NotificationManager.notify(id, notification)`.

**5. Какие методы Vibrator используются для вибрации? Как задать паттерн?**  
Через `Vibrator.vibrate(...)`. На новых API обычно используют `VibrationEffect.createOneShot(...)` и `VibrationEffect.createWaveform(...)` для паттерна.  
Также вибрацию можно указать в уведомлении (`setVibrate(...)` и настройки канала).

**6. Как получить доступ к камере для предпросмотра? Какие классы используются?**  
Нужно запросить `CAMERA`, затем использовать `CameraX` (`ProcessCameraProvider`, `Preview`, `PreviewView`) или Camera2 API (`CameraManager`, `CameraDevice`, `CameraCaptureSession`).  
Для учебных проектов обычно удобнее CameraX.

**7. Что будет, если использовать опасное разрешение без runtime-запроса на Android 6.0+?**  
Операция не выполнится: API вернет ошибку/пустой результат, либо будет `SecurityException` в зависимости от вызова.

**8. Как проверить, есть ли у приложения разрешение сейчас?**  
Через `ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED`.

---

## Итог по требованиям

- Реализовано приложение с одной `Activity`.
- Пользователь задает текст задачи и время напоминания.
- Напоминание отправляется в нужное время через `AlarmManager`.
- Показывается уведомление с вибрацией.
- Учтены особенности разрешений и версий Android.

---

## Вывод

В работе закреплены практические навыки использования аппаратных и системных возможностей Android: работа с runtime-разрешениями, планирование отложенных задач, создание уведомлений и управление вибрацией.

---

## Код самостоятельной работы (Kotlin + XML)

Ниже приведен только код, относящийся к самостоятельному заданию (вариант 2).

### `MainActivity.kt`

```kotlin
package com.byprogger.lab10

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.byprogger.lab10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val requestNotificationsCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermissionIfNeeded()

        binding.btnSave.setOnClickListener {
            val text = binding.etTask.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, "Введите задачу", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)

                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            if (!isNotificationPermissionGranted()) {
                Toast.makeText(
                    this,
                    "Разрешите уведомления для работы напоминаний",
                    Toast.LENGTH_LONG
                ).show()
                requestNotificationPermissionIfNeeded()
                return@setOnClickListener
            }

            scheduleTask(this, calendar.timeInMillis, text)
            Toast.makeText(this, "Задача запланирована", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleTask(context: Context, timeInMills: Long, text: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("TASK", text)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Нужен доступ к точным будильникам", Toast.LENGTH_LONG).show()
            val exactAlarmIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = "package:$packageName".toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(exactAlarmIntent)
            return
        }

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMills,
                    pendingIntent
                )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ->
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent)
            else ->
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent)
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                requestNotificationsCode
            )
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }
}
```

### `AlarmReceiver.kt`

```kotlin
package com.byprogger.lab10

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val text = intent.getStringExtra("TASK") ?: "Напоминание"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "task_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Задачи",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Напоминание")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        notificationManager.notify(1, notification)
    }
}
```

### `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:fitsSystemWindows="true"
    android:padding="16dp">

    <EditText
        android:id="@+id/etTask"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Введите задачу" />

    <TimePicker
        android:id="@+id/timePicker"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:timePickerMode="spinner" />

    <Button
        android:id="@+id/btnSave"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Запланировать" />

</LinearLayout>
```

### `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Lab10">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver android:name=".AlarmReceiver" />
    </application>

</manifest>
```
