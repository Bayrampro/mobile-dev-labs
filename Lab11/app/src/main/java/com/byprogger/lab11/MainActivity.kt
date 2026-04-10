package com.byprogger.lab11

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab11.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTask1.setOnClickListener {
            val intent = Intent(this, Calculate::class.java)
            startActivity(intent)
        }

        binding.btnTask2.setOnClickListener {
            val intent = Intent(this, LoadImages::class.java)
            startActivity(intent)
        }
    }
}
