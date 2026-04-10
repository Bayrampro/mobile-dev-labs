package com.byprogger.lab11

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab11.databinding.ActivityLoadImagesBinding
import java.net.URL

class LoadImages : AppCompatActivity() {
    lateinit var binding: ActivityLoadImagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoadImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val images = List(3) {"https://el.ncfu.ru/pluginfile.php/1/theme_moove/logo/1769692740/%D0%A1%D0%9A%D0%A4%D0%A3%20%D1%81%D0%B5%D0%B2%D0%B5%D1%80%D0%BE%D0%BA%D0%B0%D0%B2%D0%BA%D0%B0%D0%B7%D1%81%D0%BA%D0%B8%D0%B9%D1%84%D0%B5%D0%B4%D0%B5%D1%80%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9%D1%83%D0%BD%D0%B8%D0%B2%D0%B5%D1%80%D1%81%D0%B8%D1%82%D0%B5%D1%82.png"}
        binding.imagesProgressBar.visibility = View.VISIBLE

        Thread {
            try {
                var progress = 0
                val oneOfImageSize = 100 / images.size

                for (i in 0 until images.size) {
                    val bitmap = loadImage(images[i])

                    runOnUiThread {
                        when (i) {
                            0 -> binding.imageView1.setImageBitmap(bitmap)
                            1 -> binding.imageView2.setImageBitmap(bitmap)
                            2 -> binding.imageView3.setImageBitmap(bitmap)
                        }

                        binding.loadingText.text = "Загружено ${i + 1} из ${images.size}"
                        progress += oneOfImageSize
                        binding.imagesProgressBar.progress = progress
                    }
                }

                runOnUiThread {
                    binding.imagesProgressBar.visibility = View.GONE
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    binding.imagesProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }


    private fun loadImage(urlString: String) : Bitmap {
        val url = URL(urlString)
        val connection = url.openConnection()
        connection.doInput = true
        connection.connect()
        val input = connection.getInputStream()
        val bitmap = BitmapFactory.decodeStream(input)
        input.close()
        return bitmap
    }
}