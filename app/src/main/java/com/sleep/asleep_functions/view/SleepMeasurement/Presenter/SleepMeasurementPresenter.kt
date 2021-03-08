package com.sleep.asleep_functions.view.SleepMeasurement.Presenter

import com.sleep.asleep_functions.view.Main.Presenter.MainContract

class SleepMeasurementPresenter: SleepMeasurementContract.Presenter {

    private var SleepMeasurementView: SleepMeasurementContract.View? = null

    override fun createView(view: SleepMeasurementContract.View) {
        SleepMeasurementView = view
    }

    override fun destroyView() {
        SleepMeasurementView = null
    }

    fun changeText() {
    }
}