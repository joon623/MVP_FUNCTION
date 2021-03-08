package com.sleep.asleep_functions.view.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sleep.asleep_functions.R
import com.sleep.asleep_functions.base.BaseActivity
import com.sleep.asleep_functions.databinding.ActivityMainBinding
import com.sleep.asleep_functions.view.Main.Presenter.MainContract
import com.sleep.asleep_functions.view.Main.Presenter.MainPresenter

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate), MainContract.View {
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPresenter.createView(this)
        setClickListener()
    }

    override fun initPresenter() {
        mainPresenter = MainPresenter()
    }

    override fun setClickListener() {
        binding.tvMainHead.setOnClickListener {
            binding.tvMainHead.text = "good"
        }
    }

    override fun onDestroy() {
        mainPresenter.destroyView()
        super.onDestroy()
    }

    override fun showError(message: String) {
        showCustomToast(message)
    }
}