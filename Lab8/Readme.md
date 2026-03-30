# Практическая работа №8 — Ресурсы. Работа с медиа-элементами

**Дисциплина:** Программирование мобильных устройств  
**Студент:** Аннагурбанов Байрам  
**Группа:** ИНС-б-о-24-2  
**Дата рождения:** 12.10.2002  
**СКФУ, 2026**

---

## Цель работы

Изучить способы добавления и отображения графических ресурсов, работу с аудио- и видеофайлами в Android-приложении, а также управление медиа-воспроизведением.

---

## Выполненные задания

| Пункт | Описание |
|------|----------|
| 1 | Реализована галерея изображений с переключением по кнопкам `Назад` / `Вперёд` |
| 2 | Добавлена кнопка `Слайд-шоу` для автоматической смены изображений |
| 3 | Реализован экран видеоплеера (`VideoActivity`) с `VideoView`, `MediaController` и `SeekBar` громкости |
| 4 | Добавлена навигация с главного экрана на экран видео |
| 5 | Добавлено фоновое аудио в `MainActivity` с зацикливанием и паузой/возобновлением |

---

## Ход работы

### 1. Галерея изображений

В `drawable` добавлены изображения (`image1`, `image2`, `image3`).  
На главном экране реализованы:
- кнопка `Предыдущее`;
- кнопка `Следующее`;
- кнопка `Слайд-шоу` (повторное нажатие останавливает автосмену).

Автосмена реализована через `Timer` и `TimerTask`.

### 2. Видеоплеер

Для видео использован файл `res/raw/video1.mp4`.  
На экране `VideoActivity` реализованы:
- `VideoView` для воспроизведения;
- `MediaController` со стандартными элементами управления;
- `SeekBar` для управления громкостью потока `STREAM_MUSIC` через `AudioManager`;
- кнопка `Воспроизвести`.

### 3. Фоновое аудио

На главном экране запускается `MediaPlayer` с `audio1.mp3` в режиме `isLooping = true`.  
Также реализована пауза и последующее возобновление воспроизведения через `Handler` с задержкой `1.5` секунды.

### 4. Навигация между экранами

С `MainActivity` добавлен переход на `VideoActivity` по кнопке `Видео`.

---

## Ответы на контрольные вопросы

**1. Какие типы ресурсов существуют в Android? Для чего папки `drawable`, `raw`, `values`?**  
Ресурсы: строки, цвета, стили, изображения, аудио/видео, layout и т.д.  
`drawable` — графика (png, xml drawables), `raw` — «сырые» файлы (mp3, mp4 и др.) без обработки, `values` — строковые и числовые ресурсы (`strings`, `colors`, `dimens`, `styles`).

**2. Как добавить изображение и показать в `ImageView` двумя способами?**  
1) Из ресурсов: положить файл в `res/drawable` и задать `imageView.setImageResource(R.drawable.image1)` или `android:src="@drawable/image1"`.  
2) Из файловой системы: получить путь/`Uri` и использовать `imageView.setImageURI(uri)` (или загрузчик изображений).

**3. Жизненный цикл `MediaPlayer` и какие методы нужны для аудио из ресурсов?**  
Обычно: создание (`MediaPlayer.create(...)` или `setDataSource` + `prepare`), `start()`, `pause()`, `stop()`, `release()`.  
Для ресурса достаточно `MediaPlayer.create(context, R.raw.audio1)`, затем `start()` и в конце `release()`.

**4. Для чего `AudioManager`? Как получить и изменить громкость?**  
`AudioManager` управляет аудиопотоками устройства.  
Получение: `getSystemService(AudioManager::class.java)` или через `Context.AUDIO_SERVICE`.  
Изменение громкости: `setStreamVolume(AudioManager.STREAM_MUSIC, value, 0)`.

**5. Что такое `VideoView` и `MediaController`?**  
`VideoView` — простой компонент для воспроизведения видео.  
`MediaController` — стандартная панель управления (play/pause/seek), подключается через `videoView.setMediaController(...)`.

**6. Почему при обновлении UI из `TimerTask` нужен `runOnUiThread()`?**  
`TimerTask` выполняется в фоновом потоке, а менять UI можно только из главного потока Android.  
`runOnUiThread()` переносит код обновления интерфейса в UI-поток.

**7. Как сделать бесконечное воспроизведение аудио?**  
Установить `mediaPlayer.isLooping = true` перед `start()`.

**8. Какие разрешения нужны для доступа к медиа на внешнем хранилище?**  
Для файлов внутри `res/raw` и `res/drawable` разрешения не нужны.  
Для внешнего хранилища:
- Android 12 и ниже: `READ_EXTERNAL_STORAGE`;
- Android 13+: `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`, `READ_MEDIA_AUDIO` (по типу контента).

---

## Итог

В работе реализованы основные элементы по теме медиа-ресурсов: переключение изображений, слайд-шоу, воспроизведение видео с управлением, регулировка громкости и фоновое аудио.

---

## Вывод

Практическая работа закрепила навыки работы с ресурсами Android и медиа-компонентами (`ImageView`, `MediaPlayer`, `VideoView`, `AudioManager`, `SeekBar`, `MediaController`), а также навыки навигации между активностями и управления воспроизведением контента.

---

## Код самостоятельной работы (Kotlin + XML)

Ниже приведены файлы, относящиеся к самостоятельной части Lab8.

### `MainActivity.kt`

```kotlin
package com.byprogger.lab8

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab8.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var imageView: ImageView? = null
    private val images = intArrayOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3
    )
    private var currentIndex = 0
    private var slideshowTimer: Timer? = null
    private var isSlideshowRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imageView

        binding.btnPrev.setOnClickListener {
            showPreviousImage()
        }

        binding.btnNext.setOnClickListener {
            showNextImage()
        }

        binding.btnSlideshow.setOnClickListener {
            toggleSlideshow()
        }

        binding.btnGoToVideo.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }

        val mediaPlayer = MediaPlayer.create(this, R.raw.audio1)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        if (mediaPlayer.isPlaying) mediaPlayer.pause()

        Handler().postDelayed(Runnable {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }, 1500)
    }

    private fun showImage(index: Int) {
        if (index >= 0 && index < images.size) {
            imageView?.setImageResource(images[index])
            currentIndex = index
        }
    }

    private fun showNextImage() {
        currentIndex = (currentIndex + 1) % images.size
        showImage(currentIndex)
    }

    private fun showPreviousImage() {
        currentIndex = (currentIndex - 1 + images.size) % images.size
        showImage(currentIndex)
    }

    private fun toggleSlideshow() {
        if (isSlideshowRunning) {
            if (slideshowTimer != null) {
                slideshowTimer?.cancel()
            }
            isSlideshowRunning = false
        } else {
            slideshowTimer = Timer()
            slideshowTimer!!.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread(Runnable { showNextImage() })
                }
            }, 0, 2000)
            isSlideshowRunning = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (slideshowTimer != null) {
            slideshowTimer?.cancel()
        }
    }
}
```

### `VideoActivity.kt`

```kotlin
package com.byprogger.lab8

import android.media.AudioManager
import android.os.Bundle
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab8.databinding.ActivityVideoBinding
import androidx.core.net.toUri


class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding
    private var videoView: VideoView? = null
    private var volumeSeekBar: SeekBar? = null
    private var audioManager: AudioManager? = null
    private var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = binding.videoView
        volumeSeekBar = binding.volumeSeekBar

        audioManager = getSystemService(AudioManager::class.java)

        val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar?.max = maxVolume!!
        volumeSeekBar?.progress = currentVolume!!

        volumeSeekBar!!.setOnSeekBarChangeListener(
            object: OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            }
        )

        mediaController = MediaController(this)
        mediaController?.setAnchorView(videoView)
        videoView?.setMediaController(mediaController)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.video1

        videoView?.setVideoURI(videoPath.toUri())

        binding.btnPlayVideo.setOnClickListener { videoView?.start() }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.stopPlayback()
    }
}
```

### `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:padding="16dp">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:src="@drawable/image1"
        android:layout_marginBottom="32dp"/>

    <Button
        android:id="@+id/btnPrev"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Предыдущее"/>

    <Button
        android:id="@+id/btnNext"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Следующее"/>

    <Button
        android:id="@+id/btnSlideshow"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Слайд-шоу"/>

    <Button
        android:id="@+id/btnGoToVideo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Видео"
        android:layout_marginTop="8dp"/>

</LinearLayout>
```

### `activity_video.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <VideoView
        android:id="@+id/videoView"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Громкость"/>

    <SeekBar
        android:id="@+id/volumeSeekBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="100"
        android:progress="50"
        android:layout_marginBottom="16dp"/>

    <Button
        android:id="@+id/btnPlayVideo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Воспроизвести"/>

</LinearLayout>
```
