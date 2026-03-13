package com.byprogger.lab5

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult

        val color = result.data?.getStringExtra("COLOR") ?: return@registerForActivityResult
        applyBackgroundColor(color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnSettings = binding.btnSettings
        val btnAbout = binding.btnAbout

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            settingsLauncher.launch(intent)
        }

        btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun applyBackgroundColor(color: String) {
        when (color) {
            "red" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_red_light))
            "green" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_green_light))
            "blue" -> binding.tvResult.setTextColor(getColor(android.R.color.holo_blue_light))
        }
    }
}
