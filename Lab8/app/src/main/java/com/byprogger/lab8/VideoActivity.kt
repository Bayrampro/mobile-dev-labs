package com.byprogger.lab8

import android.media.AudioManager
import android.os.Bundle
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.byprogger.lab8.databinding.ActivityVideoBinding
import androidx.core.net.toUri


class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding
    private var videoView: VideoView? = null
    private var volumeSeekBar: SeekBar? = null
    private var audioManager: AudioManager? = null
    private var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = binding.videoView
        volumeSeekBar = binding.volumeSeekBar

        audioManager = getSystemService(AudioManager::class.java)

        val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar?.max = maxVolume!!
        volumeSeekBar?.progress = currentVolume!!

        volumeSeekBar!!.setOnSeekBarChangeListener(
            object: OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            }
        )

        mediaController = MediaController(this)
        mediaController?.setAnchorView(videoView)
        videoView?.setMediaController(mediaController)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.video1

        videoView?.setVideoURI(videoPath.toUri())

        binding.btnPlayVideo.setOnClickListener { videoView?.start() }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.stopPlayback()
    }
}