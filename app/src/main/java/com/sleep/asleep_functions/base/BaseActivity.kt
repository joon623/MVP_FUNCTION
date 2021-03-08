package com.sleep.asleep_functions.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.backends.pipeline.Fresco

abstract class BaseActivity<B : ViewBinding>(private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity() {

    protected lateinit var binding: B
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Fresco.hasBeenInitialized()) {
            Fresco.initialize(this)
        }
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initPresenter()
    }

    override fun onResume() {
        super.onResume()
        if (!Fresco.hasBeenInitialized()) {
            Fresco.initialize(this)
        }
    }

    abstract fun initPresenter()

    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun moveActivity(context: Context, target: Class<*>) {
        val intent = Intent(context, target)
        context.startActivity(intent)
    }
}