package com.sleep.asleep_functions.view.SleepMeasurement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sleep.asleep_functions.R
import com.sleep.asleep_functions.base.BaseActivity
import com.sleep.asleep_functions.databinding.ActivitySleepMeasurementBinding
import com.sleep.asleep_functions.view.SleepMeasurement.Presenter.SleepMeasurementContract
import com.sleep.asleep_functions.view.SleepMeasurement.Presenter.SleepMeasurementPresenter

class SleepMeasurementActivity : BaseActivity<ActivitySleepMeasurementBinding>(ActivitySleepMeasurementBinding::inflate), SleepMeasurementContract.View {


    private lateinit var sleepMeasurementPresenter: SleepMeasurementPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sleepMeasurementPresenter.createView(this)
        binding.tvSleepmeasureHead.setOnClickListener {
            binding.tvSleepmeasureHead.text = "changed"
        }
    }

    override fun initPresenter() {
//        sleepMeasurementPresenter = SleepMeasurementPresenter()
    }

    override fun setClickListener() {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        TODO("Not yet implemented")
    }
}