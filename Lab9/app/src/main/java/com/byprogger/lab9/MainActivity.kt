package com.byprogger.lab9

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerForContextMenu(binding.imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val params = binding.imageView.layoutParams
        val small = dpToPx(50)
        val medium = dpToPx(150)
        val big = dpToPx(250)

        when (item.itemId) {
            R.id.action_settings -> {
                params.width = small
                params.height = small
            }
            R.id.action_home -> {
                params.width = medium
                params.height = medium
            }
            R.id.action_exit -> {
                params.width = big
                params.height = big
            }
            else -> return super.onOptionsItemSelected(item)
        }

        binding.imageView.layoutParams = params
        return true
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
        menu?.setHeaderTitle("Влево/Вправо")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val step = dpToPx(30).toFloat()

        when (item.itemId) {
            R.id.context_red -> {
                binding.imageView.translationX -= step
            }

            R.id.context_green -> {
                binding.imageView.translationX += step
            }
            else -> return super.onContextItemSelected(item)
        }

        return true
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

}
