package com.byprogger.lab13

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.byprogger.lab13.databinding.ActivityAddPlayerBinding
import androidx.core.content.edit

class AddPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddPlayerBinding
    lateinit var prefs: SharedPreferences

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        if (key == "users") {
            val users = prefs.getStringSet("users", mutableSetOf())

            buildUserList(users)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Добавление игроков"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        binding.addPlayerBtn.setOnClickListener {
            showInputDialog()
        }

        val users = prefs.getStringSet("users", mutableSetOf())

        buildUserList(users)
    }

    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showInputDialog() {
        val editText = EditText(this)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Имя игрока")
            .setView(editText)
            .setPositiveButton("OK", null)
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val text = editText.text.toString()

            if (text.isBlank()) {
                editText.error = "Поле не может быть пустым"
            } else {
                val users = prefs.getStringSet("users", mutableSetOf())?.toMutableSet()

                users?.add(text)

                prefs.edit { putStringSet("users", users) }
                dialog.dismiss()
            }
        }
    }

    private fun buildUserList(users: MutableSet<String>?) {
        if (users == null) return
        binding.container.removeAllViews()

        if (users.isEmpty()) {
            binding.container.gravity = Gravity.CENTER
            val tv = TextView(this)
            tv.text = "Нет пользователей"
            tv.textSize = 18f
            tv.gravity = Gravity.CENTER
            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            binding.container.addView(tv)
            return
        }

        binding.container.gravity = Gravity.START or Gravity.TOP

        for (user in users) {
            val tv = TextView(this)
            tv.text = user
            tv.textSize = 18f
            binding.container.addView(tv)
        }
    }
}
