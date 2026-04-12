package com.byprogger.lab13

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab13.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var selectedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerForContextMenu(binding.topChip1)
        registerForContextMenu(binding.topChip2)
        registerForContextMenu(binding.topChip3)
        registerForContextMenu(binding.topChip4)

        registerForContextMenu(binding.bottomChip1)
        registerForContextMenu(binding.bottomChip2)
        registerForContextMenu(binding.bottomChip3)
        registerForContextMenu(binding.bottomChip4)

        attachSwipeToChip(binding.topChip1)
        attachSwipeToChip(binding.topChip2)
        attachSwipeToChip(binding.topChip3)
        attachSwipeToChip(binding.topChip4)

        attachSwipeToChip(binding.bottomChip1)
        attachSwipeToChip(binding.bottomChip2)
        attachSwipeToChip(binding.bottomChip3)
        attachSwipeToChip(binding.bottomChip4)
    }

    private fun attachSwipeToChip(chip: View) {
        chip.setOnTouchListener(
            object : OnSwipeTouchListener(this) {
                override fun onSwipeRight() {
                    moveWithFlash(chip, 100f, 0f)
                }

                override fun onSwipeLeft() {
                    moveWithFlash(chip, -100f, 0f)
                }

                override fun onSwipeTop() {
                    moveWithFlash(chip, 0f, -100f)
                }

                override fun onSwipeBottom() {
                    moveWithFlash(chip, 0f, 100f)
                }

                override fun onDoubleTap(): Boolean {
                    chip.scaleX = 1.5f
                    chip.scaleY = 1.5f
                    return true
                }

                override fun onLongPress() {
                    selectedView = chip
                    chip.showContextMenu()
                }

                override fun onScroll(distanceX: Float, distanceY: Float): Boolean {
                    moveChip(chip, -distanceX, -distanceY)
                    return true
                }
            }
        )
    }

    private fun moveWithFlash(chip: View, deltaX: Float, deltaY: Float) {
        val originalDrawable = getDefaultChipDrawable(chip.id)
        chip.setBackgroundResource(R.drawable.chip_green)
        moveChip(chip, deltaX, deltaY)
        chip.postDelayed(
            { chip.setBackgroundResource(originalDrawable) },
            250
        )
    }

    private fun getDefaultChipDrawable(viewId: Int): Int {
        return when (viewId) {
            R.id.topChip1, R.id.topChip2, R.id.topChip3, R.id.topChip4 -> R.drawable.chip_white
            else -> R.drawable.chip_black
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu, menu)
        selectedView = v
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                selectedView?.scaleX = 1f
                selectedView?.scaleY = 1f
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun moveChip(chip: View, deltaX: Float, deltaY: Float) {
        chip.translationX += deltaX
        chip.translationY += deltaY
    }
}
