package com.byprogger.lab5

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab5.databinding.ActivityAboutBinding
import com.byprogger.lab5.databinding.ActivitySettingsBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnBack = binding.btnBack

        btnBack.setOnClickListener {
            finish()
        }
    }
}