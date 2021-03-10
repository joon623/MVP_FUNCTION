package com.sleep.asleep_functions.view.CustomWave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.squti.androidwaverecorder.WaveRecorder
import com.sleep.asleep_functions.R
import com.sleep.asleep_functions.databinding.ActivityCustomWaveBinding
import com.sleep.asleep_functions.view.SleepMeasurement.SleepMeasurementActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CustomWaveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomWaveBinding
    private lateinit var audioFile: String
    private lateinit var recorder: WaveRecorder
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_wave)
        binding = ActivityCustomWaveBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSleepmeasureSleepstart.setOnClickListener {
            startRecord()
        }

        binding.btnSleepmeasureSleepstop.setOnClickListener {
            stopRecord()
        }
    }

    private fun startRecord() {

        binding.btnSleepmeasureSleepstart.isEnabled = false
        binding.btnSleepmeasureSleepstop.isEnabled = true

        try {
            val currentDateTime = Calendar.getInstance().time
            val dateFormat: String = SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime) + ".wav"
            audioFile = File(cacheDir, dateFormat).toString()

        } catch (e: IOException) {
            Log.e(SleepMeasurementActivity::class.simpleName, e.message ?: e.toString())
            return
        }

        recorder = WaveRecorder(audioFile)

        recorder.startRecording()
        recorder.onAmplitudeListener = {
            Log.i("TAG", "Amplitude : $it")
//            recorder.calculateAmplitudeMax()
        }
//        startDrawing()

    }

    private fun stopRecord() {
        binding.btnSleepmeasureSleepstart.isEnabled = true
        binding.btnSleepmeasureSleepstop.isEnabled = false
        recorder?.apply {
            stopRecording()
//            release()
        }
        stopDrawing()
    }

    private fun startDrawing(amplitude: Int) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
//                val currentMaxAmplitude = recorder?.maxAmplitude
                binding.audioRecordView.update(amplitude ?: 0) //redraw view
            }
        }, 0, 100)
    }

    private fun stopDrawing() {
        timer?.cancel()
        binding.audioRecordView.recreate()
    }
}