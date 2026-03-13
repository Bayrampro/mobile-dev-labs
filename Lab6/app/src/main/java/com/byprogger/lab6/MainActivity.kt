package com.byprogger.lab6

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab6.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private val tag: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartTimer.setOnClickListener {
            startDelayTask()
        }

        binding.btnGoToChronometer.setOnClickListener {
            val intent = Intent(this, StopwatchActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToTask.setOnClickListener {
            val intent = Intent(this, Task::class.java)
            startActivity(intent)
        }
    }

    fun startDelayTask() {
        binding.tvTimer.text = "Таймер запущен, ждём 5 секунд..."
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    binding.tvTimer.text = "Прошло 5 секунд!"
                    Log.i(tag, "Таймер сработал")
                }
            }
        }

        timer.schedule(timerTask, 5000)
    }
}