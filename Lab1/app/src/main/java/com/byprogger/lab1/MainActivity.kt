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
        // Задание 3
        taskThree(canvas)

        // Задание 4
        taskFour(canvas)
    }

    fun taskThree(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        val screenArea = w * h
        val circleArea = screenArea / 2
        val radius = kotlin.math.sqrt((circleArea / PI)).toFloat()

        val centerX = w / 2
        val centerY = h / 2

        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL

        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    fun taskFour(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val carWidth = 350f
        val carHeight = 120f
        val tyreOffsetX = 40
        val tyreRadius = 30f
        val cabinOffset = 60f
        val cabinHeight = 120f
        val handlerWidth = 40f
        val handlerHeight = 20f
        paint.color = Color.GREEN

        val centerX = w / 2
        val centerY = h / 2

        val left = centerX - carWidth / 2f
        val top = centerY - carHeight / 2f
        val right = centerX + carWidth / 2f
        val bottom = centerY + carHeight / 2f

        canvas.drawRect(left, top, right, bottom, paint)

        paint.color = Color.BLACK

        val leftTyreX = (centerX - carWidth / 2f) + tyreOffsetX
        val rightTyreX = (centerX + carWidth / 2f) - tyreOffsetX
        val tyreY = (centerY + carHeight / 2f)

        canvas.drawCircle(leftTyreX, tyreY, tyreRadius, paint)
        canvas.drawCircle(rightTyreX, tyreY, tyreRadius, paint)

        val cabinLeft = centerX - (carWidth / 2) + cabinOffset
        val cabinBottom = centerY
        val cabinRight = centerX + (carWidth / 2) - cabinOffset
        val cabinTop = centerY - cabinHeight

        canvas.drawArc(cabinLeft, cabinTop, cabinRight, cabinBottom, 180f, 180f, false, paint)

        val startYLine = centerY + carHeight / 2
        val stopYLine = centerY - carHeight / 2
        val line2X = centerX + (carWidth / 2) - cabinOffset

        canvas.drawLine(centerX, startYLine, centerX, stopYLine, paint)
        canvas.drawLine(line2X, startYLine, line2X, stopYLine, paint)

        val handlerLeft = centerX + 20f
        val handlerRight = handlerLeft + handlerWidth
        val handlerBottom = centerY - 20f
        val handlerTop = handlerBottom - handlerHeight

        canvas.drawArc(handlerLeft, handlerTop, handlerRight, handlerBottom, 180f, 360f, false, paint)
    }
}
