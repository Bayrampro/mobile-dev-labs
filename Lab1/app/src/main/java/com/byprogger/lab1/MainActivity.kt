package com.byprogger.lab1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.PI
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class DrawView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        taskThree(canvas)
        taskFour(canvas)
    }

    private fun taskThree(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        val screenArea = w * h
        val circleArea = screenArea / 2f
        val radius = sqrt(circleArea / PI).toFloat()

        val centerX = w / 2f
        val centerY = h / 2f

        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    private fun taskFour(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        val carWidth = 350f
        val carHeight = 120f
        val tireOffsetX = 40f
        val tireRadius = 30f
        val cabinOffset = 60f
        val cabinHeight = 120f
        val handleWidth = 40f
        val handleHeight = 20f

        val centerX = w / 2f
        val centerY = h / 2f

        val left = centerX - carWidth / 2f
        val top = centerY - carHeight / 2f
        val right = centerX + carWidth / 2f
        val bottom = centerY + carHeight / 2f

        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        canvas.drawRect(left, top, right, bottom, paint)

        paint.color = Color.BLACK
        val leftTireX = centerX - carWidth / 2f + tireOffsetX
        val rightTireX = centerX + carWidth / 2f - tireOffsetX
        val tireY = centerY + carHeight / 2f
        canvas.drawCircle(leftTireX, tireY, tireRadius, paint)
        canvas.drawCircle(rightTireX, tireY, tireRadius, paint)

        val cabinLeft = centerX - carWidth / 2f + cabinOffset
        val cabinBottom = centerY
        val cabinRight = centerX + carWidth / 2f - cabinOffset
        val cabinTop = centerY - cabinHeight
        canvas.drawArc(cabinLeft, cabinTop, cabinRight, cabinBottom, 180f, 180f, false, paint)

        val startYLine = centerY + carHeight / 2f
        val stopYLine = centerY - carHeight / 2f
        val line2X = centerX + carWidth / 2f - cabinOffset
        canvas.drawLine(centerX, startYLine, centerX, stopYLine, paint)
        canvas.drawLine(line2X, startYLine, line2X, stopYLine, paint)

        val handleLeft = centerX + 20f
        val handleRight = handleLeft + handleWidth
        val handleBottom = centerY - 20f
        val handleTop = handleBottom - handleHeight
        canvas.drawArc(handleLeft, handleTop, handleRight, handleBottom, 180f, 360f, false, paint)
    }
}
