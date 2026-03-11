package com.example.shizukushell

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shizukushell.databinding.ActivityMainBinding
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        requestShizukuPermission()

        bind.runBtn.setOnClickListener {
            val cmd = bind.commandInput.text.toString()
            val result = shizukuExec(cmd)
            bind.outputText.text = result
        }
    }

    private fun requestShizukuPermission() {
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(0)
        }
    }

    private fun shizukuExec(cmd: String): String {
        val process = Shizuku.newProcess(
            arrayOf("sh", "-c", cmd),
            null,
            null
        )

        val output = process.inputStream.bufferedReader().readText()
        val error = process.errorStream.bufferedReader().readText()

        process.waitFor()

        return if (error.isNotEmpty()) error else output
    }
}
