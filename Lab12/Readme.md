# Практическая работа №12 — Типы активностей и SharedPreferences

**Тема:** Типы активностей. Шаблоны Android Studio. Сохранение настроек с `SharedPreferences`  
**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Изучить шаблоны активностей Android Studio, реализовать многоэкранное приложение и освоить хранение пользовательских данных в `SharedPreferences`.

---

## Вариант

**Вариант 2: Реверси**  
Требуемые экраны:
- главный экран;
- экран игры;
- экран добавления игроков;
- экран победителей.

---

## Выполненные задания

| Пункт | Реализация |
|------|------------|
| 1 | Создан главный экран с навигацией по 3 кнопкам (`MainActivity`) |
| 2 | Подключен экран игры на шаблоне `Fullscreen Activity` (`GameActivity`) |
| 3 | Реализован экран добавления игроков с формой ввода имени (`AddPlayerActivity`) |
| 4 | Имена игроков сохраняются в `SharedPreferences` (`users`) |
| 5 | Реализован экран победителей (`WinnerActivity`) |
| 6 | Список победителей хранится/читается из `SharedPreferences` (`winners`) |
| 7 | Настроены переходы между активностями через `Intent` |
| 8 | Добавлена навигация "назад" через `Toolbar` на вторичных экранах |

---

## Ход работы

### 1. Каркас приложения

Создано приложение из 4 экранов:
- `MainActivity` — центральная точка навигации;
- `GameActivity` — игровой экран на полноэкранном шаблоне;
- `AddPlayerActivity` — добавление и отображение игроков;
- `WinnerActivity` — вывод победителей и очков.

### 2. Навигация между экранами

С главного экрана переходы выполняются через `Intent`:
- `btnGame` -> `GameActivity`;
- `btnAddPlayer` -> `AddPlayerActivity`;
- `btnWinner` -> `WinnerActivity`.

### 3. SharedPreferences для игроков

В `AddPlayerActivity`:
- используется `PreferenceManager.getDefaultSharedPreferences(this)`;
- имена игроков сохраняются как `StringSet` по ключу `users`;
- после добавления список на экране обновляется;
- при пустом списке выводится сообщение `Нет пользователей`.

### 4. SharedPreferences для победителей

В `WinnerActivity`:
- победители хранятся строкой JSON по ключу `winners`;
- JSON парсится через `JSONArray`;
- данные выводятся динамически в контейнер (`name: score`).

### 5. Использованные шаблоны

В проекте использованы шаблоны Android Studio:
- обычная `Empty Views Activity` (главный экран и вспомогательные экраны);
- `Fullscreen Activity` для экрана игры.

---

## Ответы на контрольные вопросы

**1. Какие шаблоны активностей предоставляет Android Studio? Кратко 3-4 примера.**  
Часто используются:
- `Empty Views Activity` — базовая Activity с пустым layout;
- `Fullscreen Activity` — экран в полноэкранном режиме;
- `Navigation Drawer Activity` — экран с боковым меню;
- `Settings Activity` — готовый экран настроек на `PreferenceFragmentCompat`.

**2. Для чего нужен SharedPreferences? Какие типы данных можно хранить?**  
`SharedPreferences` хранит небольшие пары ключ-значение (настройки/состояния).  
Поддерживаются `Boolean`, `Int`, `Long`, `Float`, `String`, `Set<String>`.

**3. Разница между getPreferences(), getSharedPreferences() и PreferenceManager.getDefaultSharedPreferences()?**  
- `getPreferences()` — preferences только текущей Activity;
- `getSharedPreferences(name, mode)` — доступ к именованному файлу preferences;
- `PreferenceManager.getDefaultSharedPreferences(context)` — стандартный файл настроек приложения.

**4. Как записать данные? Разница apply() и commit().**  
Запись: `prefs.edit().putString(...).apply()` или `commit()`.  
`apply()` — асинхронно, без immediate-результата, обычно предпочтительнее.  
`commit()` — синхронно, возвращает `Boolean`, но может блокировать поток.

**5. Как читать данные? Зачем значение по умолчанию?**  
Чтение: `prefs.getString("key", "default")`, `prefs.getBoolean(...)` и т.д.  
Значение по умолчанию нужно, если ключ еще не записан.

**6. Как создать экран настроек через Settings Activity? Где описываются элементы?**  
Создается `SettingsActivity` + `PreferenceFragmentCompat`.  
Пункты настроек описываются в XML в `res/xml` (например, `root_preferences.xml`).

**7. Как организовать переход между активностями с Intent?**  
Создать `Intent(this, TargetActivity::class.java)` и вызвать `startActivity(intent)`.

**8. Что такое FloatingActionButton и где встречается?**  
`FloatingActionButton` — плавающая круглая кнопка для основного действия экрана.  
Часто присутствует в шаблонах `Basic Activity`, `Navigation Drawer Activity`, `Tabbed Activity`.

---

## Итог

- Реализован многоэкранный каркас приложения по варианту 2.
- Настроена навигация между экранами.
- Реализовано сохранение и чтение данных через `SharedPreferences`.
- Выполнена индивидуальная часть: добавление игроков и экран победителей.

---

## Вывод

В ходе работы закреплены навыки создания разных типов активностей, переходов между экранами и хранения пользовательских данных с помощью `SharedPreferences` в Android-приложении.

---

## Код самостоятельной работы (Kotlin + XML)

Ниже приведен код самостоятельной части по варианту 2.

### `MainActivity.kt`

```kotlin
package com.byprogger.lab12

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddPlayer.setOnClickListener {
            val intent = Intent(this, AddPlayerActivity::class.java)
            startActivity(intent)
        }

        binding.btnWinner.setOnClickListener {
            val intent = Intent(this, WinnerActivity::class.java)
            startActivity(intent)
        }
    }
}
```

### `AddPlayerActivity.kt`

```kotlin
package com.byprogger.lab12

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.byprogger.lab12.databinding.ActivityAddPlayerBinding
import androidx.core.content.edit

class AddPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddPlayerBinding
    lateinit var prefs: SharedPreferences

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        if (key == "users") {
            val users = prefs.getStringSet("users", mutableSetOf())

            buildUserList(users)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Добавление игроков"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        binding.addPlayerBtn.setOnClickListener {
            showInputDialog()
        }

        val users = prefs.getStringSet("users", mutableSetOf())

        buildUserList(users)
    }

    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showInputDialog() {
        val editText = EditText(this)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Имя игрока")
            .setView(editText)
            .setPositiveButton("OK", null)
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val text = editText.text.toString()

            if (text.isBlank()) {
                editText.error = "Поле не может быть пустым"
            } else {
                val users = prefs.getStringSet("users", mutableSetOf())?.toMutableSet()

                users?.add(text)

                prefs.edit { putStringSet("users", users) }
                dialog.dismiss()
            }
        }
    }

    private fun buildUserList(users: MutableSet<String>?) {
        if (users == null) return
        binding.container.removeAllViews()

        if (users.isEmpty()) {
            binding.container.gravity = Gravity.CENTER
            val tv = TextView(this)
            tv.text = "Нет пользователей"
            tv.textSize = 18f
            tv.gravity = Gravity.CENTER
            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            binding.container.addView(tv)
            return
        }

        binding.container.gravity = Gravity.START or Gravity.TOP

        for (user in users) {
            val tv = TextView(this)
            tv.text = user
            tv.textSize = 18f
            binding.container.addView(tv)
        }
    }
}
```

### `WinnerActivity.kt`

```kotlin
package com.byprogger.lab12

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.byprogger.lab12.databinding.ActivityWinnerBinding
import androidx.core.content.edit
import org.json.JSONArray

class WinnerActivity : AppCompatActivity() {

    lateinit var binding: ActivityWinnerBinding
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Экран c победителями"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val rawJson = """
        [
          {"name":"Alice","score":120},
          {"name":"Bob","score":95},
          {"name":"Charlie","score":80}
        ]
        """.trimIndent()

        prefs.edit { putString("winners", rawJson) }

        val json = prefs.getString("winners", "[]")
        val list = mutableListOf<Pair<String, Int>>()

        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val score = obj.getInt("score")

            list.add(name to score)
        }

        for ((name, score) in list) {
            val tv = TextView(this)
            tv.text = "$name: $score очка"
            tv.textSize = 18f
            binding.container.addView(tv)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}
```

### `GameActivity.kt`

```kotlin
package com.byprogger.lab12

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import com.byprogger.lab12.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler(Looper.myLooper()!!)

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(
                WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            )
        } else {
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
    private val showPart2Runnable = Runnable {
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }

            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true
        fullscreenContent = binding.fullscreenContent
        fullscreenContent.setOnClickListener { toggle() }
        fullscreenContentControls = binding.fullscreenContentControls
        binding.dummyButton.setOnTouchListener(delayHideTouchListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.show(
                WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            )
        } else {
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        isFullscreen = true
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        private const val UI_ANIMATION_DELAY = 300
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
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:fitsSystemWindows="true"
    android:gravity="center"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/btnGame"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Экран игры" />

    <Button
        android:id="@+id/btnAddPlayer"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Экран добавления игроков" />

    <Button
        android:id="@+id/btnWinner"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Экран с победителями" />

</LinearLayout>
```

### `activity_add_player.xml`

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    android:background="@android:color/white"
    android:orientation="vertical">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"/>

    <Button
        android:id="@+id/addPlayerBtn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:text="Добавить игрока"/>

    <LinearLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_margin="16dp"
        android:layout_marginTop="30dp"
        android:orientation="vertical"/>

</LinearLayout>
```

### `activity_winner.xml`

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    android:background="@android:color/white"
    android:orientation="vertical">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"/>

    <LinearLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_margin="16dp"
        android:layout_marginTop="30dp"
        android:orientation="vertical"/>

</LinearLayout>
```

### `activity_game.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?attr/fullscreenBackgroundColor"
    android:theme="@style/ThemeOverlay.Lab12.FullscreenContainer"
    tools:context=".GameActivity">

    <TextView
        android:id="@+id/fullscreen_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:keepScreenOn="true"
        android:text="@string/dummy_content"
        android:textColor="?attr/fullscreenTextColor"
        android:textSize="50sp"
        android:textStyle="bold" />

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true">

        <LinearLayout
            android:id="@+id/fullscreen_content_controls"
            style="@style/Widget.Theme.Lab12.ButtonBar.Fullscreen"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|center_horizontal"
            android:orientation="horizontal"
            tools:ignore="UselessParent">

            <Button
                android:id="@+id/dummy_button"
                style="?android:attr/buttonBarButtonStyle"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="@string/dummy_button" />
        </LinearLayout>
    </FrameLayout>

</FrameLayout>
```

### `root_preferences.xml`

```xml
<PreferenceScreen xmlns:app="http://schemas.android.com/apk/res-auto">

    <PreferenceCategory app:title="@string/messages_header">

        <EditTextPreference
            app:key="signature"
            app:title="@string/signature_title"
            app:useSimpleSummaryProvider="true" />

        <ListPreference
            app:defaultValue="reply"
            app:entries="@array/reply_entries"
            app:entryValues="@array/reply_values"
            app:key="reply"
            app:title="@string/reply_title"
            app:useSimpleSummaryProvider="true" />

    </PreferenceCategory>

    <PreferenceCategory app:title="@string/sync_header">

        <SwitchPreferenceCompat
            app:key="sync"
            app:title="@string/sync_title" />

        <SwitchPreferenceCompat
            app:dependency="sync"
            app:key="attachment"
            app:summaryOff="@string/attachment_summary_off"
            app:summaryOn="@string/attachment_summary_on"
            app:title="@string/attachment_title" />

    </PreferenceCategory>

</PreferenceScreen>
```

### `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Lab12">
        <activity
            android:name=".WinnerActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:exported="false"
            android:label="@string/title_activity_winner"
            android:theme="@style/Theme.Lab12.Fullscreen" />
        <activity
            android:name=".AddPlayerActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:exported="false"
            android:label="@string/title_activity_add_player"
            android:theme="@style/Theme.Lab12.Fullscreen" />
        <activity
            android:name=".GameActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:exported="false"
            android:label="@string/title_activity_game"
            android:theme="@style/Theme.Lab12.Fullscreen" />
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
