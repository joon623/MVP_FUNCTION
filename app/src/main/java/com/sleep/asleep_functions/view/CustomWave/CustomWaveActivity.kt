package com.sleep.asleep_functions.view.CustomWave

import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.squti.androidwaverecorder.WaveConfig
import com.github.squti.androidwaverecorder.WaveRecorder
import com.sleep.asleep_functions.R
import com.sleep.asleep_functions.databinding.ActivityCustomWaveBinding
import com.sleep.asleep_functions.view.SleepMeasurement.SleepMeasurementActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
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
        Log.d("TAG", "start")

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
            GlobalScope.launch(Dispatchers.Default) {
                GlobalScope.launch(Dispatchers.Main) {
                    startDrawing(it)
                }
            }
        }

    }

    private fun stopRecord() {
        binding.btnSleepmeasureSleepstart.isEnabled = true
        binding.btnSleepmeasureSleepstop.isEnabled = false
        recorder?.apply {
            stopRecording()
        }
        stopDrawing()
    }

    private suspend fun startDrawing(amp: Int) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
//                val currentMaxAmpli9tude = recorder?.maxAmplitude
//                val currentMaxAmplitude = recorder?.onAmplitudeListener
//                val currentMaxAmplitude = calculateAmplitudeMax(data)
                Log.d("TAG1", "1111111111          $amp")
                binding.audioRecordView.update(((amp ?: 0) as Int)) //redraw view
            }
        }, 0, 10000000)
    }

    private fun stopDrawing() {
        timer?.cancel()
        binding.audioRecordView.recreate()
    }


    // test

    var waveConfig: WaveConfig = WaveConfig()

    val bufferSize = AudioRecord.getMinBufferSize(
        waveConfig.sampleRate,
        waveConfig.channels,
        waveConfig.audioEncoding
    )
    val data = ByteArray(bufferSize)


    private fun calculateAmplitudeMax(data: ByteArray): Int {
        val shortData = ShortArray(data.size / 2)
        ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
            .get(shortData)
        return shortData.max()?.toInt() ?: 0
    }
}