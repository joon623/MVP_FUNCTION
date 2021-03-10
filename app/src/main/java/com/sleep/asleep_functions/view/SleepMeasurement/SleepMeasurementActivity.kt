package com.sleep.asleep_functions.view.SleepMeasurement

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sleep.asleep_functions.base.BaseActivity
import com.sleep.asleep_functions.databinding.ActivitySleepMeasurementBinding
import com.sleep.asleep_functions.view.SleepMeasurement.Presenter.SleepMeasurementContract
import com.sleep.asleep_functions.view.SleepMeasurement.Presenter.SleepMeasurementPresenter
import java.io.File
import java.io.IOException
import java.lang.System.getProperty
import java.security.Security.getProperty
import java.text.SimpleDateFormat
import java.util.*


private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class SleepMeasurementActivity : BaseActivity<ActivitySleepMeasurementBinding>(ActivitySleepMeasurementBinding::inflate), SleepMeasurementContract.View {

    private lateinit var sleepMeasurementPresenter: SleepMeasurementPresenter

    private val requiredPermissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    )

    private var timer: Timer? = null
    private var audioFile: File? = null
    private var recorder: MediaRecorder? = null


    // Requesting permission to RECORD_AUDIO
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sleepMeasurementPresenter.createView(this)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        binding.btnSleepmeasureSleepstart.setOnClickListener {
            startRecord()
        }

        binding.btnSleepmeasureSleepstop.setOnClickListener {
            stopRecord()
        }

        binding.btnSleepmeasureLongClick.setOnLongClickListener {
            Toast.makeText(this@SleepMeasurementActivity, "롱클릭 성공", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun initPresenter() {
        sleepMeasurementPresenter = SleepMeasurementPresenter()
    }

    override fun onDestroy() {
        sleepMeasurementPresenter.destroyView()
        super.onDestroy()
    }

    override fun showError(message: String) {
        TODO("Not yet implemented")
    }

    private fun startRecord() {
        if (!permissionsIsGranted(requiredPermissions)) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 200)
            return
        }

        binding.btnSleepmeasureSleepstart.isEnabled = false
        binding.btnSleepmeasureSleepstop.isEnabled = true

        try {
            val currentDateTime = Calendar.getInstance().time
            val dateFormat: String = SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime) + ".wav"
            audioFile = File(cacheDir, dateFormat)

        } catch (e: IOException) {
            Log.e(SleepMeasurementActivity::class.simpleName, e.message ?: e.toString())
            return
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = MediaRecorder()
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile?.absolutePath)
            setAudioSamplingRate(48000)
            setAudioEncodingBitRate(48000)
            prepare()
            start()
        }
        startDrawing()

        Toast.makeText(this@SleepMeasurementActivity, "레코딩 시작되었습니다.", Toast.LENGTH_SHORT).show()

    }

    private fun stopRecord() {
        binding.btnSleepmeasureSleepstart.isEnabled = true
        binding.btnSleepmeasureSleepstop.isEnabled = false

        recorder?.apply {
            stop()
            release()
        }
        stopDrawing()
    }

    private fun startDrawing() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val currentMaxAmplitude = recorder?.maxAmplitude
                binding.audioRecordView.update(currentMaxAmplitude ?: 0) //redraw view
            }
        }, 0, 100)
    }

    private fun stopDrawing() {
        timer?.cancel()
        binding.audioRecordView.recreate()
    }

    private fun permissionsIsGranted(perms: Array<String>): Boolean {
        for (perm in perms) {
            val checkVal: Int = checkCallingOrSelfPermission(perm)
            if (checkVal != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}
