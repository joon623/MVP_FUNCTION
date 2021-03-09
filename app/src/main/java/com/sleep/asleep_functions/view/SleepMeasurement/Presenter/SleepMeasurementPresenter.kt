package com.sleep.asleep_functions.view.SleepMeasurement.Presenter

import android.media.AudioFormat
import android.media.MediaRecorder
import android.widget.Toast
import com.sleep.asleep_functions.view.Main.Presenter.MainContract
import com.sleep.asleep_functions.view.SleepMeasurement.SleepMeasurementActivity
import java.io.File
import java.io.IOException
import java.util.*

class SleepMeasurementPresenter: SleepMeasurementContract.Presenter {

    private var SleepMeasurementView: SleepMeasurementContract.View? = null

    override fun createView(view: SleepMeasurementContract.View) {
        SleepMeasurementView = view
    }

    override fun destroyView() {
        SleepMeasurementView = null
    }

    override fun handleRecord() {
        TODO("Not yet implemented")
    }

    override fun sendRecord() {
        TODO("Not yet implemented")
    }
}