package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.main_but)
        button.setOnClickListener {
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent) // 액티비티 시작
        }

        val email: TextView = findViewById(R.id.sendemail)
        email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "plain/text"
                val address = arrayOf("poodac@address.com")
                putExtra(Intent.EXTRA_EMAIL, address)
                putExtra(Intent.EXTRA_SUBJECT, "<푸닥 문의사항>")
                putExtra(Intent.EXTRA_TEXT, "문의 내용을 입력해주세요.")
            }
            startActivity(emailIntent)
        }
    }
}
