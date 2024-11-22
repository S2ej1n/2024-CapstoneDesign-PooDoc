package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import android.widget.LinearLayout

class ManageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)

        // 샘플 환자 리스트 데이터
        val patientList = listOf(
            mapOf("patient_id" to "1", "patient_name" to "마라탕웨이", "patient_age" to 65),
            mapOf("patient_id" to "2", "patient_name" to "김순자", "patient_age" to 70)
        )

        // TextView를 추가할 부모 레이아웃
        val parentLayout = findViewById<LinearLayout>(R.id.patientContainer)

//        val patient1: TextView = findViewById(R.id.patient1)
//        val textView2: TextView = findViewById(R.id.patient2)

        // 환자 리스트를 순회하며 TextView를 동적으로 추가
        for ((index, patient) in patientList.withIndex()) {
            val patientName = patient["patient_name"] as String // 환자 이름 가져오기

            // TextView 생성
            val textView = TextView(this).apply {
                id = index + 1 // 고유 ID 설정
                text = patientName // 환자 이름 설정
                setTextColor(resources.getColor(R.color.textcolor, null))
                setBackgroundResource(R.drawable.patient_box)
                setPadding(12, 12, 12, 12)
                textSize = 15f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 15
                }
            }

            textView.setOnClickListener {
                val intent = Intent(this, MonthActivity::class.java)
                intent.putExtra("PATIENT_NAME", patientName) // 환자 이름 전달
                startActivity(intent)
            }

            // TextView를 부모 레이아웃에 추가
            parentLayout.addView(textView)
        }

//        patient1.setOnClickListener {
//            val intent = Intent(this, MonthActivity::class.java)
//            intent.putExtra("PATIENT_NAME", patient1.text.toString()) // 텍스트를 Intent에 추가
//            startActivity(intent) // MonthActivity 시작
//        }
//
//        textView2.setOnClickListener {
//            val intent = Intent(this, TodayActivity2::class.java)
//            startActivity(intent) // TodayActivity 시작
//        }
    }
}