package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TableLayout
import android.widget.TableRow

import java.util.*

class TodayActivity : AppCompatActivity() {
    // 서버에서 불러온 숫자. 가장 많이 나온 브리스톨 숫자.
    private var serverNumber: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_stats)

        // 캘린더뷰에서 선택한 날짜 가져오기
        // 전달된 날짜 가져오기
        val selectedDate = intent.getStringExtra("SELECTED_DATE")
        // TextView에 날짜 표시
        val dateTextView = findViewById<TextView>(R.id.today_date) // activity_today_stats에 있는 TextView ID
        dateTextView.text = "$selectedDate"

        // 앞에서 환자 아이디 가져오기
        val patientId = intent.getIntExtra("PATIENT_ID", -1)

        if (patientId != -1) {
            fetchDailyStats(patientId)
        } else {
            Toast.makeText(this, "환자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 제일 큰 메인 똥 정보들
//        val mainDdongImageView: ImageView = findViewById(R.id.main_ddong)
//        val mainDdongNameTextView: TextView = findViewById(R.id.main_ddong_name)
//        val mainDdongExplanTextView: TextView = findViewById(R.id.main_ddong_explan)
//
//        // 텍스트 색깔 변경 - 밑의 작은 똥 정보들
//        val poo1Text: TextView = findViewById(R.id.poo1text)
//        val poo2Text: TextView = findViewById(R.id.poo2text)
//        val poo3Text: TextView = findViewById(R.id.poo3text)
//        val poo4Text: TextView = findViewById(R.id.poo4text)
//        val poo5Text: TextView = findViewById(R.id.poo5text)
//        val poo6Text: TextView = findViewById(R.id.poo6text)
//        val poo7Text: TextView = findViewById(R.id.poo7text)
//
//        // 사진 변경 - 사진 색깔 변경임
//        val poo1ImageView: ImageView = findViewById(R.id.poo1)
//        val poo2ImageView: ImageView = findViewById(R.id.poo2)
//        val poo3ImageView: ImageView = findViewById(R.id.poo3)
//        val poo4ImageView: ImageView = findViewById(R.id.poo4)
//        val poo5ImageView: ImageView = findViewById(R.id.poo5)
//        val poo6ImageView: ImageView = findViewById(R.id.poo6)
//        val poo7ImageView: ImageView = findViewById(R.id.poo7)
//
//        // 서버에서 받은 숫자에 따라 UI 업데이트
//        updateUI(serverNumber, mainDdongImageView, mainDdongNameTextView, mainDdongExplanTextView,
//            poo1ImageView, poo2ImageView, poo3ImageView, poo4ImageView, poo5ImageView, poo6ImageView, poo7ImageView)
//        updateTextColor(serverNumber, poo1Text, poo2Text, poo3Text, poo4Text, poo5Text, poo6Text, poo7Text)
    }

    private fun fetchDailyStats(patientId: Int) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        apiService.getDailyStats(patientId).enqueue(object : Callback<DailyStatsResponse> {
            override fun onResponse(call: Call<DailyStatsResponse>, response: Response<DailyStatsResponse>) {
                if (response.isSuccessful) {
                    val stats = response.body()
                    stats?.let {
                        // 서버 응답 데이터를 처리하는 함수 호출
                        displayDailyStats(it)
                    }
                } else {
                    Toast.makeText(this@TodayActivity, "데이터 로드 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DailyStatsResponse>, t: Throwable) {
                Toast.makeText(this@TodayActivity, "통신 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayDailyStats(stats: DailyStatsResponse) {
        // 브리스톨 데이터를 처리하여 가장 큰 값을 serverNumber에 저장
        serverNumber = stats.bristol.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: -1

        // UI 업데이트
        updateUI(
            serverNumber,
            findViewById(R.id.main_ddong),
            findViewById(R.id.main_ddong_name),
            findViewById(R.id.main_ddong_explan),
            findViewById(R.id.poo1),
            findViewById(R.id.poo2),
            findViewById(R.id.poo3),
            findViewById(R.id.poo4),
            findViewById(R.id.poo5),
            findViewById(R.id.poo6),
            findViewById(R.id.poo7)
        )
        updateTextColor(
            serverNumber,
            findViewById(R.id.poo1text),
            findViewById(R.id.poo2text),
            findViewById(R.id.poo3text),
            findViewById(R.id.poo4text),
            findViewById(R.id.poo5text),
            findViewById(R.id.poo6text),
            findViewById(R.id.poo7text)
        )

        // 횟수 데이터를 표시
        val countTextView = findViewById<TextView>(R.id.how_many_poo)
        countTextView.text = "${stats.count}"

        // 시간 정보를 표시
        val tableLayout: TableLayout = findViewById(R.id.time_info_table)
        tableLayout.removeAllViews()


        stats.time_info.forEach { timeInfo ->
            val tableRow = TableRow(this)

            // 시작 시간에서 시간만 추출 (HH:mm 형식)
            val formattedStartTime = timeInfo.starttime.substring(11, 16) // "00:01"
            val formattedEndTime = timeInfo.endtime.substring(11, 16)     // "00:05"

            val startTimeTextView = TextView(this).apply {
                text = formattedStartTime
            }
            val endTimeTextView = TextView(this).apply {
                text = formattedEndTime
            }
            val durationTextView = TextView(this).apply {
                text = "${timeInfo.duration} 분"
                textSize= 12f
                setTextColor(Color.parseColor("#4E4E4E"))
            }

            // 시작시간 ~ 종료시간 형태로 결합
            val timeRangeTextView = TextView(this).apply {
                text = "$formattedStartTime ~ $formattedEndTime"
                textSize= 12f
                setTextColor(Color.parseColor("#4E4E4E"))

            }

            tableRow.addView(timeRangeTextView) // 시간 범위 추가
            tableRow.addView(durationTextView)

            tableLayout.addView(tableRow)
        }
    }


    private fun updateUI(number: Int, imageView: ImageView, nameTextView: TextView, explanTextView: TextView,
                         poo1ImageView: ImageView, poo2ImageView: ImageView, poo3ImageView: ImageView,
                         poo4ImageView: ImageView, poo5ImageView: ImageView, poo6ImageView: ImageView, poo7ImageView: ImageView) {
        when (number) {
            1 -> {
                imageView.setImageResource(R.drawable.poo1c)
                nameTextView.text = "심한 변비"
                explanTextView.text = "변비가 매우 심합니다. \n 건강 상태를 체크해주세요."
                poo1ImageView.setImageResource(R.drawable.poo1c)
            }
            2 -> {
                imageView.setImageResource(R.drawable.poo2c)
                nameTextView.text = "가벼운 변비"
                explanTextView.text = "몸에 수분이 부족해요. \n 더 심한 변비로 진행되기 전에 물을 마셔주세요."
                poo2ImageView.setImageResource(R.drawable.poo2c)
            }
            3 -> {
                imageView.setImageResource(R.drawable.poo3c)
                nameTextView.text = "정상"
                explanTextView.text = "정상이지만 표면이 울퉁불퉁합니다. \n 약간의 수분이 필요할것 같습니다."
                poo3ImageView.setImageResource(R.drawable.poo3c)
            }
            4 -> {
                imageView.setImageResource(R.drawable.poo4c)
                nameTextView.text = "정상"
                explanTextView.text = "변이 매끈하고 부드러워요. \n 정상적인 변입니다."
                poo4ImageView.setImageResource(R.drawable.poo4c)
            }
            5 -> {
                imageView.setImageResource(R.drawable.poo5c)
                nameTextView.text = "섬유질 부족"
                explanTextView.text = "변이 부드럽게 뚝뚝 끊겨 나와요\n" +
                        "채소, 과일, 콩 등 식이섬유가 풍부한 음식을 섭취해주세요."
                poo5ImageView.setImageResource(R.drawable.poo5c)
            }
            6 -> {
                imageView.setImageResource(R.drawable.poo6c)
                nameTextView.text = "가벼운 설사"
                explanTextView.text = "설사로 진행될 위험이 있어요. \n 장이 불편할 것이라 예상됩니다."
                poo6ImageView.setImageResource(R.drawable.poo6c)
            }
            7 -> {
                imageView.setImageResource(R.drawable.poo7c)
                nameTextView.text = "심한 설사"
                explanTextView.text = "장에 문제가 있어보입니다. \n 건강 상태를 체크해주세요."
                poo7ImageView.setImageResource(R.drawable.poo7c)
            }
        }
    }

    private fun updateTextColor(
        number: Int,
        poo1Text: TextView,
        poo2Text: TextView,
        poo3Text: TextView,
        poo4Text: TextView,
        poo5Text: TextView,
        poo6Text: TextView,
        poo7Text: TextView
    ) {
        // 기본 텍스트 색상
        val defaultColor = Color.parseColor("#868686")
        poo1Text.setTextColor(defaultColor)
        poo2Text.setTextColor(defaultColor)
        poo3Text.setTextColor(defaultColor)
        poo4Text.setTextColor(defaultColor)
        poo5Text.setTextColor(defaultColor)
        poo6Text.setTextColor(defaultColor)
        poo7Text.setTextColor(defaultColor)

        //텍스트 색상 변경
        val selectedColor = Color.parseColor("#5BC9D0")
        when (number) {
            1 -> poo1Text.setTextColor(selectedColor)
            2 -> poo2Text.setTextColor(selectedColor)
            3 -> poo3Text.setTextColor(selectedColor)
            4 -> poo4Text.setTextColor(selectedColor)
            5 -> poo5Text.setTextColor(selectedColor)
            6 -> poo6Text.setTextColor(selectedColor)
            7 -> poo7Text.setTextColor(selectedColor)
        }
    }
}