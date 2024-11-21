package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView

class ManageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)

        val patient1: TextView = findViewById(R.id.patient1)
        val textView2: TextView = findViewById(R.id.patient2)

        patient1.setOnClickListener {
            val intent = Intent(this, MonthActivity::class.java)
            intent.putExtra("PATIENT_NAME", patient1.text.toString()) // 텍스트를 Intent에 추가
            startActivity(intent) // MonthActivity 시작
        }

        textView2.setOnClickListener {
            val intent = Intent(this, TodayActivity2::class.java)
            startActivity(intent) // TodayActivity 시작
        }
    }
}