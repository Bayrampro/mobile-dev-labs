package com.byprogger.lab13

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.byprogger.lab13.databinding.ActivityWinnerBinding
import androidx.core.content.edit
import org.json.JSONArray

class WinnerActivity : AppCompatActivity() {

    lateinit var binding: ActivityWinnerBinding
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Экран c победителями"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val rawJson = """
        [
          {"name":"Alice","score":120},
          {"name":"Bob","score":95},
          {"name":"Charlie","score":80}
        ]
        """.trimIndent()

        prefs.edit { putString("winners", rawJson) }

        val json = prefs.getString("winners", "[]")
        val list = mutableListOf<Pair<String, Int>>()

        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val score = obj.getInt("score")

            list.add(name to score)
        }

        for ((name, score) in list) {
            val tv = TextView(this)
            tv.text = "$name: $score очка"
            tv.textSize = 18f
            binding.container.addView(tv)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}
