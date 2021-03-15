package com.sleep.asleep_functions.view.AudioRecorderSample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sleep.asleep_functions.R
import kotlinx.android.synthetic.main.activity_sound_chart.*
import me.bogerchan.niervisualizer.NierVisualizerManager
import me.bogerchan.niervisualizer.renderer.columnar.ColumnarType2Renderer

class SoundChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_chart)
        val visualizerManager = NierVisualizerManager()

        val state = visualizerManager.init(0)


        tv_media_player_start_or_stop.setOnClickListener {
            visualizerManager.start(sv_wave, arrayOf(ColumnarType2Renderer()))
        }
        tv_media_player_pause_or_resume.setOnClickListener {
            visualizerManager.stop()
        }
    }
}