package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import android.widget.LinearLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import android.util.Log

class ManageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)

        // 샘플 환자 리스트 데이터 ----------------------------------------------
//        val patientList = listOf(
//            mapOf("patient_id" to "1", "patient_name" to "마라탕웨이", "patient_age" to 65),
//            mapOf("patient_id" to "2", "patient_name" to "김순자", "patient_age" to 70)
//        )
//        // --------------------------------------------------------------

        // TextView를 추가할 부모 레이아웃
        val parentLayout = findViewById<LinearLayout>(R.id.patientContainer)
        // 환자 데이터를 서버에서 불러오기
        fetchPatientData(parentLayout)

        // 환자 리스트를 순회하며 TextView를 동적으로 추가(테스트용)--------------------
//        for ((index, patient) in patientList.withIndex()) {
//            val patientName = patient["patient_name"] as String // 환자 이름 가져오기
//            val patientAge = patient["patient_age"] as Int
//
//            val patientNameWithAge = "$patientName ($patientAge)"
//
//            // TextView 생성
//            val textView = TextView(this).apply {
//                id = index + 1 // 고유 ID 설정
//                text = patientNameWithAge // 환자 이름 설정
//                setTextColor(resources.getColor(R.color.textcolor, null))
//                setBackgroundResource(R.drawable.patient_box)
//                setPadding(12, 20, 15, 20)
//                textSize = 15f
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    topMargin = 15
//                }
//            }
//
//            textView.setOnClickListener {
//                val intent = Intent(this, MonthActivity::class.java)
//                intent.putExtra("PATIENT_NAME", patientName) // 환자 이름 전달
//                intent.putExtra("PATIENT_AGE", patientAge)
//                startActivity(intent)
//            }
//
//            // TextView를 부모 레이아웃에 추가
//            parentLayout.addView(textView)
//        }
        //-------------------------------------------------------------------------
    }

    // 환자 데이터를 서버에서 불러오기
    private fun fetchPatientData(parentLayout: LinearLayout) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        apiService.getPatientList().enqueue(object : Callback<List<Patient>> {
            override fun onResponse(call: Call<List<Patient>>, response: Response<List<Patient>>) {
                if (response.isSuccessful) {
                    val patientList = response.body()
                    if (!patientList.isNullOrEmpty()) {
                        displayPatientList(parentLayout, patientList)
                    } else {
                        Toast.makeText(this@ManageActivity, "환자 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ManageActivity, "데이터 로드 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Patient>>, t: Throwable) {
                Toast.makeText(this@ManageActivity, "통신 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ManageActivity", "통신 실패: ${t.message}")
            }
        })
    }

    // 레이아웃에 추가하는 함수
    private fun displayPatientList(parentLayout: LinearLayout, patientList: List<Patient>) {
        // 환자 리스트를 순회하며 TextView를 동적으로 추가
        for ((index, patient) in patientList.withIndex()) {
            val patientNameWithAge = "${patient.patient_name} (${patient.patient_age})"

            // TextView 생성
            val textView = TextView(this).apply {
                id = index + 1 // 고유 ID 설정
                text = patientNameWithAge // 환자 이름 설정
                setTextColor(resources.getColor(R.color.textcolor, null))
                setBackgroundResource(R.drawable.patient_box)
                setPadding(12, 20, 15, 20)
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
                intent.putExtra("PATIENT_NAME", patient.patient_name) // 환자 이름 전달
                intent.putExtra("PATIENT_AGE", patient.patient_age)
//                intent.putExtra("PATIENT_ID", patient.p_id as String) // 환자 ID 전달
                startActivity(intent)
            }

            // TextView를 부모 레이아웃에 추가
            parentLayout.addView(textView)
        }
    }
}