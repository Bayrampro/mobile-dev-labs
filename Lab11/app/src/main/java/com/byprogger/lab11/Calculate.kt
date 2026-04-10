package com.byprogger.lab11

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab11.databinding.ActivityCalculateBinding
import kotlin.math.absoluteValue

class Calculate : AppCompatActivity() {
    lateinit var binding: ActivityCalculateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calculateArray = Array(100) { (-100..100).random() }

        binding.calcProgressBar.visibility = View.VISIBLE

        Thread {
            val sum = calcSum(calculateArray)
            val multiply = calcMultiply(calculateArray)

            runOnUiThread {
                binding.calcProgressBar.visibility = View.GONE
                binding.sumValue.text = sum.toString()
                binding.multiplyValue.text = multiply.toString()
            }
        }.start()
    }

    private fun calcSum(calculateArray: Array<Int>) : Int {
        var result = 0

        if (calculateArray.isEmpty()) return 0

        for (i in calculateArray) {
            if (i > 0) {
                result += i
            }
        }

        return result
    }

    private fun calcMultiply(calculateArray: Array<Int>) : Int {
        var result = 1

        if (calculateArray.isEmpty()) return 0

        var maxAbs = 0
        var minAbs = Int.MAX_VALUE
        var maxAbsIndex = 0
        var minAbsIndex = 0

        for (i in 0 until calculateArray.size) {
            if (calculateArray[i].absoluteValue > maxAbs) {
                maxAbs = calculateArray[i].absoluteValue
                maxAbsIndex = i
            }

            if (calculateArray[i].absoluteValue < minAbs) {
                minAbs = calculateArray[i].absoluteValue
                minAbsIndex = i
            }
        }

        if ((maxAbsIndex + 1 == minAbsIndex) || (maxAbsIndex == minAbsIndex + 1)) return 0

        if (maxAbsIndex > minAbsIndex) {
            for (i in (minAbsIndex + 1) until maxAbsIndex) {
                result *= calculateArray[i]
            }
        } else {
            for (i in (maxAbsIndex + 1) until minAbsIndex) {
                result *= calculateArray[i]
            }
        }

        return result
    }

}