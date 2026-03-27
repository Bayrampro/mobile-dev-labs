package com.byprogger.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byprogger.lab7.databinding.ActivityCatsListBinding

class CatsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCatsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = resources.getStringArray(R.array.items_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val item = items[position]
            print(item)
        }
    }
}