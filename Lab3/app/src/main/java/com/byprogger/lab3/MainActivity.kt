package com.byprogger.lab3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var showSurname = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener {
            Toast.makeText(this, "Аннагурбанов Б.Б.", Toast.LENGTH_SHORT).show()
        }
        binding.button2.setOnClickListener { view ->
            val button = view as Button
            button.text = "Пока"
        }

        binding.button3.setOnClickListener {
            Toast.makeText(this, "Аннагурбанов Кнопка 1", Toast.LENGTH_LONG).show()
        }
        binding.button4.setOnClickListener {
            Toast.makeText(this, "Аннагурбанов Кнопка 2", Toast.LENGTH_LONG).show()
        }
        binding.button4.setOnClickListener {
            Toast.makeText(this, "Аннагурбанов Кнопка 3", Toast.LENGTH_LONG).show()
        }

        val commonListener = View.OnClickListener {
            v -> when(v.id) {
                R.id.button6 -> {
                    Toast.makeText(this, "Аннагурбанов Кнопка 4", Toast.LENGTH_LONG).show()
                }
                R.id.button7 -> {
                    Toast.makeText(this, "Аннагурбанов Кнопка 5", Toast.LENGTH_LONG).show()
                }
                R.id.button8 -> {
                    Toast.makeText(this, "Аннагурбанов Кнопка 6", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.button6.setOnClickListener(commonListener)
        binding.button7.setOnClickListener(commonListener)
        binding.button8.setOnClickListener(commonListener)

        binding.button9.setOnClickListener {
            if (showSurname) {
                Toast.makeText(this, "Аннагурбанов", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "ИНС-б-о-24-2", Toast.LENGTH_SHORT).show()
            }
            showSurname = true
        }

        binding.button10.setOnClickListener {
            showSurname = false
        }
    }
}