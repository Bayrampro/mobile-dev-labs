package com.byprogger.lab5

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab5.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val radioGroupColor = binding.radioGroupColor
        val  btnSave = binding.btnSave

        btnSave.setOnClickListener {
            val selectedId: Int = radioGroupColor.checkedRadioButtonId
            var selectedColor = "red"

            if (selectedId == binding.radioGreen.id) {
                selectedColor = "green"
            } else if (selectedId == binding.radioBlue.id) {
                selectedColor = "blue"
            }

            val resultIntent = Intent()
            resultIntent.putExtra("COLOR", selectedColor)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}