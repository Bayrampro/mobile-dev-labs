package com.byprogger.lab13

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab13.databinding.ActivityMainBinding

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