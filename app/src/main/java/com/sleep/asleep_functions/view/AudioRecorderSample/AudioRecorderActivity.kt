package com.sleep.asleep_functions.view.AudioRecorderSample

import android.media.*
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sleep.asleep_functions.R
import java.io.*

class AudioRecorderActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val mAudioSource = MediaRecorder.AudioSource.MIC
    private val mSampleRate = 44100
    private val mChannelCount = AudioFormat.CHANNEL_IN_STEREO
    private val mAudioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat)

    var mAudioRecord: AudioRecord? = null

    var mRecordThread: Thread? = null
    var isRecording = false

    var mAudioTrack: AudioTrack? = null
    var mPlayThread: Thread? = null
    var isPlaying = false

    var mBtRecord: Button? = null
    var mBtPlay: Button? = null

    var mFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_recorder)
        mBtRecord = findViewById<View>(R.id.bt_record) as Button
        mBtPlay = findViewById<View>(R.id.bt_play) as Button
        mAudioRecord =
            AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize)
        mAudioRecord!!.startRecording()
        mAudioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            mSampleRate,
            mChannelCount,
            mAudioFormat,
            mBufferSize,
            AudioTrack.MODE_STREAM
        )
        mRecordThread = Thread {
            val readData = ByteArray(mBufferSize)
            mFilePath =
                Environment.getExternalStorageDirectory().absolutePath + "/record.pcm"
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(mFilePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            while (isRecording) {
                val ret = mAudioRecord!!.read(readData, 0, mBufferSize)
                Log.d(TAG, "read bytes is $ret")
                try {
                    fos!!.write(readData, 0, mBufferSize)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            mAudioRecord!!.stop()
            mAudioRecord!!.release()
            mAudioRecord = null
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        mPlayThread = Thread {
            val writeData = ByteArray(mBufferSize)
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(mFilePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val dis = DataInputStream(fis)
            mAudioTrack!!.play()
            while (isPlaying) {
                try {
                    val ret = dis.read(writeData, 0, mBufferSize)
                    if (ret <= 0) {
                        this@AudioRecorderActivity.runOnUiThread(Runnable {
                            isPlaying = false
                            mBtPlay!!.text = "Play"
                        })
                        break
                    }
                    mAudioTrack!!.write(writeData, 0, ret)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            mAudioTrack!!.stop()
            mAudioTrack!!.release()
            mAudioTrack = null
            try {
                dis.close()
                fis!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun onRecord(view: View?) {
        if (isRecording == true) {
            isRecording = false
            mBtRecord!!.text = "Record"
        } else {
            isRecording = true
            mBtRecord!!.text = "Stop"
            if (mAudioRecord == null) {
                mAudioRecord =
                    AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize)
                mAudioRecord!!.startRecording()
            }
            mRecordThread!!.start()
        }
    }

    fun onPlay(view: View?) {
        if (isPlaying == true) {
            isPlaying = false
            mBtPlay!!.text = "Play"
        } else {
            isPlaying = true
            mBtPlay!!.text = "Stop"
            if (mAudioTrack == null) {
                mAudioTrack = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    mSampleRate,
                    mChannelCount,
                    mAudioFormat,
                    mBufferSize,
                    AudioTrack.MODE_STREAM
                )
            }
            mPlayThread!!.start()
        }
    }
}