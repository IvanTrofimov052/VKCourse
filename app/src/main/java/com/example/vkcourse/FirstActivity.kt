package com.example.vkcourse

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first)
        val button = findViewById<Button>(R.id.openSecond)
        val editText = findViewById<EditText>(R.id.editText)
        button.setOnClickListener {
            val text = editText.text.toString()
            if (text.length > 20) {
                editText.error = "Текст слишком длинный"
                return@setOnClickListener
            }
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("TEXT", text)
            startActivity(intent)
        }

        val callButton = findViewById<Button>(R.id.callFriend)
        callButton.setOnClickListener {
            val tel = editText.text.toString()
            if (!Patterns.PHONE.matcher(tel).matches()) {
                editText.error = "Некорекный номер телефона"
                return@setOnClickListener
            }

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = "tel:$tel".toUri()
            try {
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                editText.error = "Нету приложения телефона на устройстве"
            }
        }

        val shareButton = findViewById<Button>(R.id.shareText)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, editText.text.toString())
            try {
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                editText.error = "Невозможно поделиться"
            }
        }
    }
}