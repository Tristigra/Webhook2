package com.example.discordwebhook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val webhookUrl = "https://discord.com/api/webhooks/1398611604861489232/p-Wy5NRFSsNr7TPvZLUezc_bs730vPUR6tdeu6FCqd1BD5Y9uUhJr70VeJ3WBbYP2wTv"
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_App_Dark)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edit = findViewById<EditText>(R.id.messageInput)
        val btn = findViewById<Button>(R.id.sendButton)

        btn.setOnClickListener {
            val msg = edit.text.toString().trim()
            if (msg.isNotEmpty()) sendMessage(msg)
            else Toast.makeText(this, "Typ eerst iets!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage(message: String) {
        val json = """{"content":"$message"}"""
        val body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json)
        val req = Request.Builder().url(webhookUrl).post(body).build()
        client.newCall(req).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(this@MainActivity, "Error bij versturen", Toast.LENGTH_SHORT).show() }
            }
            override fun onResponse(call: Call, response: Response) {
                val txt = if (response.isSuccessful) "Bericht verzonden!" else "Fout code: ${response.code}"
                runOnUiThread { Toast.makeText(this@MainActivity, txt, Toast.LENGTH_SHORT).show() }
            }
        })
    }
}