# Практическая работа №13 — Обработка жестов

**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Изучить механизмы обработки сенсорных жестов в Android, реализовать распознавание свайпов и дополнительных жестов с помощью `GestureDetector`, а также встроить управление жестами в игровую часть приложения.

---

## Вариант

Игровой проект (Реверси): управление фишками жестами на экране игры.

---

## Выполненные задания

| Пункт | Реализация |
|------|------------|
| 1 | Интегрирована обработка свайпов в игровой экран (`GameActivity`) |
| 2 | Создан универсальный обработчик жестов `OnSwipeTouchListener` на базе `GestureDetector` |
| 3 | Реализованы жесты: `onFling` (свайпы), `onLongPress`, `onDoubleTap`, `onScroll` |
| 4 | Долгое нажатие открывает контекстное меню для выбранной фишки |
| 5 | Двойное касание увеличивает масштаб фишки |
| 6 | Контекстное меню позволяет сбросить масштаб фишки |
| 7 | Добавлена визуальная обратная связь: фишка кратковременно становится зелёной при движении |
| 8 | На экране размещены 8 круглых фишек (4 белые сверху, 4 чёрные снизу) |

---

## Ход работы

### 1. Интеграция жестов в игровую Activity

В `GameActivity` каждой фишке назначается `OnSwipeTouchListener`.
При распознавании жеста выполняется перемещение конкретной фишки через изменение `translationX/translationY`.

### 2. Реализация дополнительных жестов

В `OnSwipeTouchListener` добавлены обработчики:
- `onLongPress` — используется для вызова контекстного меню;
- `onDoubleTap` — используется для увеличения фишки;
- `onScroll` — используется для плавного перемещения фишки;
- `onFling` — используется для быстрых смахиваний по 4 направлениям.

### 3. Визуальная обратная связь

При срабатывании свайпа фишка временно меняет цвет на зелёный (`chip_green`) и затем возвращается в исходный (`chip_white`/`chip_black`) через короткую задержку.

### 4. Контекстное меню

Для фишек зарегистрировано `ContextMenu`.
По долгому нажатию показывается меню с действием «Сбросить размер», которое возвращает масштаб выбранной фишки к `1f`.

---

## Ответы на контрольные вопросы

**1. Что такое MotionEvent? Какие основные actions существуют?**  
`MotionEvent` — объект, описывающий касание экрана. Основные действия: `ACTION_DOWN`, `ACTION_MOVE`, `ACTION_UP`, `ACTION_CANCEL`, также для мультитача `ACTION_POINTER_DOWN/UP`.

**2. Для чего нужен GestureDetector? В чём его преимущество?**  
`GestureDetector` распознаёт составные жесты (свайп, скролл, двойной тап, long press) поверх «сырых» событий касания. Преимущество — меньше ручной логики и более надёжное распознавание.

**3. Какой метод отвечает за свайп? Какие параметры принимает?**  
За быстрое смахивание отвечает `onFling(e1, e2, velocityX, velocityY)`, где `e1/e2` — начальная и конечная точки касания, `velocityX/velocityY` — скорость движения.

**4. Зачем в onDown() возвращать true?**  
Это сообщает `GestureDetector`, что событие принято, и он должен продолжать анализ следующих событий жеста.

**5. Как отличить горизонтальный свайп от вертикального?**  
Сравнить модули смещений: `abs(diffX)` и `abs(diffY)`. Если `abs(diffX) > abs(diffY)` — горизонтальный свайп, иначе вертикальный.

**6. Что такое threshold и зачем нужен?**  
Пороговые значения (`threshold`) — минимальные дистанция/скорость для признания жеста. Они отсекают случайные мелкие движения и ложные срабатывания.

**7. Как заставить View реагировать на сенсорные события?**  
Назначить слушатель через `setOnTouchListener(...)` и обрабатывать события касаний (`MotionEvent`) или передавать их в `GestureDetector`.

**8. Какие ещё жесты распознаёт GestureDetector?**  
Например: `onLongPress`, `onDoubleTap`, `onScroll`, `onSingleTapConfirmed`, `onShowPress`.

---

## Итог

- Жесты успешно интегрированы в игровой экран.
- Реализованы как базовые свайпы, так и дополнительные жесты (`long press`, `double tap`, `scroll`).
- Добавлена визуальная обратная связь и контекстное меню для управления состоянием фишек.

---

## Вывод

В ходе работы освоена практическая обработка сенсорных жестов в Android с использованием `GestureDetector` и `OnTouchListener`. Реализация встроена в игровую логику и улучшает интерактивность интерфейса.

---

## Код самостоятельной работы (Kotlin + XML)

Ниже приведён код, относящийся к самостоятельной части (обработка жестов в игровом экране).

### `GameActivity.kt`

```kotlin
package com.byprogger.lab13

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab13.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var selectedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerForContextMenu(binding.topChip1)
        registerForContextMenu(binding.topChip2)
        registerForContextMenu(binding.topChip3)
        registerForContextMenu(binding.topChip4)

        registerForContextMenu(binding.bottomChip1)
        registerForContextMenu(binding.bottomChip2)
        registerForContextMenu(binding.bottomChip3)
        registerForContextMenu(binding.bottomChip4)

        attachSwipeToChip(binding.topChip1)
        attachSwipeToChip(binding.topChip2)
        attachSwipeToChip(binding.topChip3)
        attachSwipeToChip(binding.topChip4)

        attachSwipeToChip(binding.bottomChip1)
        attachSwipeToChip(binding.bottomChip2)
        attachSwipeToChip(binding.bottomChip3)
        attachSwipeToChip(binding.bottomChip4)
    }

    private fun attachSwipeToChip(chip: View) {
        chip.setOnTouchListener(
            object : OnSwipeTouchListener(this) {
                override fun onSwipeRight() {
                    moveWithFlash(chip, 100f, 0f)
                }

                override fun onSwipeLeft() {
                    moveWithFlash(chip, -100f, 0f)
                }

                override fun onSwipeTop() {
                    moveWithFlash(chip, 0f, -100f)
                }

                override fun onSwipeBottom() {
                    moveWithFlash(chip, 0f, 100f)
                }

                override fun onDoubleTap(): Boolean {
                    chip.scaleX = 1.5f
                    chip.scaleY = 1.5f
                    return true
                }

                override fun onLongPress() {
                    selectedView = chip
                    chip.showContextMenu()
                }

                override fun onScroll(distanceX: Float, distanceY: Float): Boolean {
                    moveChip(chip, -distanceX, -distanceY)
                    return true
                }
            }
        )
    }

    private fun moveWithFlash(chip: View, deltaX: Float, deltaY: Float) {
        val originalDrawable = getDefaultChipDrawable(chip.id)
        chip.setBackgroundResource(R.drawable.chip_green)
        moveChip(chip, deltaX, deltaY)
        chip.postDelayed(
            { chip.setBackgroundResource(originalDrawable) },
            250
        )
    }

    private fun getDefaultChipDrawable(viewId: Int): Int {
        return when (viewId) {
            R.id.topChip1, R.id.topChip2, R.id.topChip3, R.id.topChip4 -> R.drawable.chip_white
            else -> R.drawable.chip_black
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu, menu)
        selectedView = v
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                selectedView?.scaleX = 1f
                selectedView?.scaleY = 1f
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun moveChip(chip: View, deltaX: Float, deltaY: Float) {
        chip.translationX += deltaX
        chip.translationY += deltaY
    }
}
```

### `OnSwipeTouchListener.kt`

```kotlin
package com.byprogger.lab13

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {
    private var gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            onLongPress()
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return onDoubleTap()
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return onScroll(distanceX, distanceY)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false

            if (e1 == null) return false

            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x

                if (abs(diffX) > abs(diffY)) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }

                    result = true
                } else {
                    if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }

                        result = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }
    }

    open fun onLongPress() {}
    open fun onDoubleTap() : Boolean {
        return false
    }
    open fun onScroll(distanceX: Float, distanceY: Float): Boolean {
        return false
    }
    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeTop() {}
    open fun onSwipeBottom() {}
}
```

### `activity_game.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?attr/fullscreenBackgroundColor"
    android:clipChildren="false"
    android:clipToPadding="false"
    android:theme="@style/ThemeOverlay.Lab13.FullscreenContainer"
    tools:context=".GameActivity">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true"
        android:orientation="vertical"
        android:clipChildren="false"
        android:clipToPadding="false"
        android:paddingHorizontal="16dp"
        android:paddingTop="24dp"
        android:paddingBottom="24dp">

        <LinearLayout
            android:id="@+id/topChipsRow"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:clipChildren="false"
            android:clipToPadding="false"
            android:gravity="center"
            android:orientation="horizontal">

            <View
                android:id="@+id/topChip1"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_white" />

            <View
                android:id="@+id/topChip2"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_white" />

            <View
                android:id="@+id/topChip3"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_white" />

            <View
                android:id="@+id/topChip4"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_white" />
        </LinearLayout>

        <LinearLayout
            android:id="@+id/bottomChipsRow"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:clipChildren="false"
            android:clipToPadding="false"
            android:gravity="center"
            android:orientation="horizontal">

            <View
                android:id="@+id/bottomChip1"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_black" />

            <View
                android:id="@+id/bottomChip2"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_black" />

            <View
                android:id="@+id/bottomChip3"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_black" />

            <View
                android:id="@+id/bottomChip4"
                android:layout_width="52dp"
                android:layout_height="52dp"
                android:layout_margin="8dp"
                android:background="@drawable/chip_black" />
        </LinearLayout>
    </LinearLayout>

</FrameLayout>
```

### `context_menu.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/delete"
        android:title="Сбросить размер"/>
</menu>
```

### `chip_white.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#FFFFFF" />
    <stroke
        android:width="2dp"
        android:color="#B0BEC5" />
</shape>
```

### `chip_black.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#111111" />
    <stroke
        android:width="2dp"
        android:color="#ECEFF1" />
</shape>
```

### `chip_green.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#4CAF50" />
    <stroke
        android:width="2dp"
        android:color="#1B5E20" />
</shape>
```
