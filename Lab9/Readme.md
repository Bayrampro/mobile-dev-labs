# Практическая работа №9 — Создание меню

**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Изучить создание и обработку событий двух типов меню в Android:
- главного меню (`OptionsMenu`);
- контекстного меню (`ContextMenu`);

и научиться изменять интерфейс приложения через пункты меню.

---

## Вариант

**Вариант 2:**  
Главное меню — изменение размеров круга (маленький, средний, большой).  
Контекстное меню на круге — перемещение влево/вправо.

---

## Выполненные задания

| Пункт | Описание |
|------|----------|
| 1 | Реализовано `OptionsMenu` из XML (`main_menu.xml`) |
| 2 | Через пункты меню меняется размер круга (`ImageView`) |
| 3 | Реализовано `ContextMenu` из XML (`context_menu.xml`) |
| 4 | Контекстные пункты двигают круг влево/вправо |
| 5 | Контекстное меню привязано к `ImageView` через `registerForContextMenu(...)` |

---

## Ход работы

### 1. Главное меню (OptionsMenu)

В `MainActivity` переопределены:
- `onCreateOptionsMenu(...)` — инфлейт меню `R.menu.main_menu`;
- `onOptionsItemSelected(...)` — обработка выбора пунктов.

Пункты `Маленький`, `Средний`, `Большой` меняют `width/height` у `imageView` через `layoutParams`.

### 2. Контекстное меню (ContextMenu)

В `onCreate(...)` зарегистрирован элемент:

```kotlin
registerForContextMenu(binding.imageView)
```

Переопределены:
- `onCreateContextMenu(...)` — создание контекстного меню из `R.menu.context_menu`;
- `onContextItemSelected(...)` — обработка пунктов.

Перемещение выполняется по оси X через `translationX`:
- `переместить влево` -> `translationX -= step`;
- `переместить вправо` -> `translationX += step`.

### 3. Разметка

В `activity_main.xml` размещены:
- `TextView` с заголовком;
- `ImageView` с кругом (`@drawable/circle`), над которым вызывается контекстное меню.

---

## Ответы на контрольные вопросы

**1. Какие типы меню существуют в Android?**  
Основные:
- `OptionsMenu` — главное меню Activity (в ActionBar/Toolbar и overflow);
- `ContextMenu` — контекстное меню для конкретного View (обычно по долгому нажатию);
- `PopupMenu` — всплывающее меню, привязанное к View.

**2. Как создать главное меню (OptionsMenu)? Какие методы переопределить?**  
Создать XML в `res/menu`, затем переопределить:
- `onCreateOptionsMenu(menu: Menu?)` — инфлейт меню;
- `onOptionsItemSelected(item: MenuItem)` — обработка выбранного пункта.

**3. Для чего `app:showAsAction`? Какие значения?**  
Определяет, как пункт меню отображается в ActionBar/Toolbar.  
Частые значения:
- `always` — всегда как кнопка;
- `ifRoom` — как кнопка, если есть место;
- `never` — только в overflow;
- `withText` — показывать текст вместе с иконкой;
- `collapseActionView` — сворачиваемый action view.

**4. Как зарегистрировать View для контекстного меню? Где обычно?**  
Через `registerForContextMenu(view)`, обычно в `onCreate()` Activity/Fragment после `setContentView`.

**5. Разница между `onCreateContextMenu` и `onContextItemSelected`?**  
`onCreateContextMenu(...)` — создание/наполнение меню.  
`onContextItemSelected(...)` — обработка выбранного пользователем пункта.

**6. Как создать контекстное меню программно, без XML?**  
В `onCreateContextMenu(...)` можно добавлять пункты через API:
`menu?.add(groupId, itemId, order, "Название")`.

**7. Что возвращают `onOptionsItemSelected` и `onContextItemSelected`? Что означает `true`?**  
Возвращают `Boolean`.  
`true` — событие обработано текущим обработчиком;  
`false`/вызов `super` — передать обработку дальше.

**8. Как определить, для какого View вызвано контекстное меню, если их несколько?**  
Проверять параметр `v` в `onCreateContextMenu(...)` (`v?.id`) и/или хранить id текущего элемента; при обработке сверяться с этим id.

---

## Итог

- Реализованы оба требуемых типа меню.
- Через `OptionsMenu` меняется размер круга.
- Через `ContextMenu` круг перемещается влево/вправо.
- Вся логика работает в рамках одного экрана, как требовалось по варианту 2.

---

## Вывод

В ходе работы закреплены навыки создания меню в Android, обработки пользовательских действий через пункты меню и динамического изменения параметров View в рантайме.

---

## Код самостоятельной работы (Kotlin + XML)

### `MainActivity.kt`

```kotlin
package com.byprogger.lab9

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerForContextMenu(binding.imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val params = binding.imageView.layoutParams
        val small = dpToPx(50)
        val medium = dpToPx(150)
        val big = dpToPx(250)

        when (item.itemId) {
            R.id.action_settings -> {
                params.width = small
                params.height = small
            }
            R.id.action_home -> {
                params.width = medium
                params.height = medium
            }
            R.id.action_exit -> {
                params.width = big
                params.height = big
            }
            else -> return super.onOptionsItemSelected(item)
        }

        binding.imageView.layoutParams = params
        return true
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
        menu?.setHeaderTitle("Влево/Вправо")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val step = dpToPx(30).toFloat()

        when (item.itemId) {
            R.id.context_red -> {
                binding.imageView.translationX -= step
            }

            R.id.context_green -> {
                binding.imageView.translationX += step
            }
            else -> return super.onContextItemSelected(item)
        }

        return true
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

}
```

### `main_menu.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item android:id="@+id/action_settings"
        android:title="Маленький"/>

    <item android:id="@+id/action_home"
        android:title="Средний"/>

    <item android:id="@+id/action_exit"
        android:title="Большой" />
</menu>
```

### `context_menu.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/context_red"
        android:title="переместить влево" />

    <item
        android:id="@+id/context_green"
        android:title="переместить вправо" />
</menu>
```

### `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/mainLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Самостоятельная работа"
        android:textSize="24sp"
        android:layout_marginBottom="32dp" />

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="150dp"
        android:layout_height="150dp"
        android:src="@drawable/circle"
        android:layout_marginBottom="32dp" />

</LinearLayout>
```
