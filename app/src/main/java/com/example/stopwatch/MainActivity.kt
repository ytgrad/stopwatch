package com.example.stopwatch

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private var startTime = 0L
    private var updateTime = 0L
    private var timeBuff = 0L
    private var elapsedTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            updateTime = elapsedTime + timeBuff
            val hours = updateTime /3600000
            val minutes = ( updateTime % 3600000 ) / 60000
            val seconds = (updateTime % 60000) / 1000
            val ms = updateTime % 1000

            binding.tvHours.text = String.format("%02d", hours)
            binding.tvMinutes.text = String.format("%02d", minutes)
            binding.tvSeconds.text = String.format("%02d", seconds)
            binding.tvMilliseconds.text = String.format("%03d", ms)

            handler.postDelayed(this, 1)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnPlayPause.setOnClickListener{
            if (!isRunning){
                startTime = SystemClock.elapsedRealtime()
                handler.postDelayed(runnable, 1)
                isRunning = true
                binding.btnPlayPause.setImageResource(R.drawable.btn_background_pause)
            }else{
                timeBuff += elapsedTime
                handler.removeCallbacks(runnable)
                isRunning = false
                binding.btnPlayPause.setImageResource(R.drawable.btn_background)
            }
        }
        binding.btnReset.setOnClickListener {
            handler.removeCallbacks(runnable)
            isRunning = false
            timeBuff = 0
            binding.tvHours.text = getString(R.string._00)
            binding.tvMinutes.text = getString(R.string._00)
            binding.tvSeconds.text = getString(R.string._00)
            binding.tvMilliseconds.text = getString(R.string._00)
            binding.btnPlayPause.setImageResource(R.drawable.btn_background)
        }

    }
}