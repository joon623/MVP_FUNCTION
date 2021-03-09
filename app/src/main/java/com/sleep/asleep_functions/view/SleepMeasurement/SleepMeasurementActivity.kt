package com.sleep.asleep_functions.view.SleepMeasurement

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.sleep.asleep_functions.base.BaseActivity
import com.sleep.asleep_functions.databinding.ActivitySleepMeasurementBinding
import com.sleep.asleep_functions.view.SleepMeasurement.Presenter.SleepMeasurementContract
import com.sleep.asleep_functions.view.SleepMeasurement.Presenter.SleepMeasurementPresenter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class SleepMeasurementActivity : BaseActivity<ActivitySleepMeasurementBinding>(ActivitySleepMeasurementBinding::inflate), SleepMeasurementContract.View {

    private val mediaRecorder: MediaRecorder? = MediaRecorder()
    private lateinit var sleepMeasurementPresenter: SleepMeasurementPresenter

    private var timer: Timer? = null

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sleepMeasurementPresenter.createView(this)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        binding.btnSleepmeasureSleepstart.setOnClickListener {
            startRecord(this)
        }

        binding.btnSleepmeasureSleepstop.setOnClickListener {
            stopRecord()
        }
    }



    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
//        if (!permissionToRecordAccepted) finish()
    }

    override fun initPresenter() {
        Log.d("string", "asdasd")
        sleepMeasurementPresenter = SleepMeasurementPresenter()
    }

    override fun onDestroy() {
        sleepMeasurementPresenter.destroyView()
        super.onDestroy()
    }

    override fun showError(message: String) {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecord(context: Context) {

        if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this, arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 111
            )
        }
        val currentDateTime = Calendar.getInstance().time
        var dateFormat: String = SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA).format(currentDateTime) + ".wav"
        val output = File(context.cacheDir, dateFormat.toString())

        mediaRecorder?.setAudioSource((MediaRecorder.AudioSource.MIC))
        mediaRecorder?.setOutputFormat(AudioFormat.ENCODING_PCM_16BIT)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setAudioChannels(1);
        mediaRecorder?.setAudioSamplingRate(16000);
        mediaRecorder?.setOutputFile(output)

        try {
            mediaRecorder?.prepare()

        } catch (e: IllegalStateException) {
            Toast.makeText(this@SleepMeasurementActivity, "레코드가 실패했습니다.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        } catch (e: IOException) {
            Toast.makeText(this@SleepMeasurementActivity, "레코드가 실패했습니다.2", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        mediaRecorder?.start()
        startDrawing()
        Toast.makeText(this@SleepMeasurementActivity, "레코딩 시작되었습니다.", Toast.LENGTH_SHORT).show()
    }


    private fun stopRecord() {
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        mediaRecorder?.release()
        Toast.makeText(this, "중지 되었습니다.", Toast.LENGTH_SHORT).show()
        stopDrawing()
    }

    private fun startDrawing() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val currentMaxAmplitude = mediaRecorder?.maxAmplitude
                binding.audioRecordView.update(currentMaxAmplitude ?: 0) //redraw view
            }
        }, 0, 100)
    }

    private fun stopDrawing() {
        timer?.cancel()
        binding.audioRecordView.recreate()
    }

}