package com.li.almacen.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.li.almacen.R
import com.li.almacen.ui.login.Login

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lanzarMain()
    }

    private fun lanzarMain() {
        object : CountDownTimer(1000,2500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                startActivity(Intent(this@Splash, Login::class.java))
                finish()
            }
        }.start()
    }
}