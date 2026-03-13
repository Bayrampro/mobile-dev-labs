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