package com.byprogger.lab6

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab6.databinding.ActivityStopwatchBinding

class StopwatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStopwatchBinding
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chronometer.base = SystemClock.elapsedRealtime()

        binding.btnStart.setOnClickListener {
            if (!isRunning) {
                binding.chronometer.start()
                isRunning = true
            }
        }

        binding.btnStop.setOnClickListener {
            if (isRunning) {
                binding.chronometer.stop()
                isRunning = false
            }
        }

        binding.btnReset.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime()
        }
    }
}