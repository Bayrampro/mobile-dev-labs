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
