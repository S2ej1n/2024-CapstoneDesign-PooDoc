package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

import android.content.Intent
import android.widget.CalendarView
import android.widget.Toast
import android.widget.TextView

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_state)

        val barChart = findViewById<BarChart>(R.id.graph_layout) // XML에서 그래프의 ID를 가져옴

        // 이전 화면에서 전달된 patient_id를 수신 -- 쿼리 파라미터가 될것임.
        val patientId = intent.getIntExtra("PATIENT_ID", -1) // 기본값 -1

        // 서버 연결
        if (patientId != -1) {
            val year = 2024 // 조회 연도
            val month = 11  // 조회 월
            fetchMonthlyStats(patientId, year, month, barChart)
//            fetchMonthlyStats(patientId, year, month) // 서버 요청
        } else {
            Toast.makeText(this, "환자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }


        //환자 이름 표시
        // Intent에서 텍스트 가져오기
        val patientName = intent.getStringExtra("PATIENT_NAME")

        // TextView에 텍스트 설정
        val tvPatientName = findViewById<TextView>(R.id.tv_patient_name)
        tvPatientName.text = patientName

        // ---------------------------------그래프그리기---------------------------------------------
        // BarChart 초기화 코드
//        val barChart = findViewById<BarChart>(R.id.graph_layout) // XML에서 그래프의 ID를 가져옴

        // 그래프에 사용할 색상 지정
//        val yellow_diarrhea_Color = Color.parseColor("#E8C300")
//        val red_constipation_Color = Color.parseColor("#FF5656")
//        val blue_strange_Color = Color.parseColor("#6893FF")
//
//        // 데이터 생성
//        val diarrheaEntries = ArrayList<BarEntry>()
//        val constipationEntries = ArrayList<BarEntry>()
//        val colorChangeEntries = ArrayList<BarEntry>()
//
//        // 데이터 추가
//        diarrheaEntries.add(BarEntry(1f, 2f)) // 설사
//        constipationEntries.add(BarEntry(2f, 5f)) // 변비
//        colorChangeEntries.add(BarEntry(3f, 3f)) // 변색깔 이상
//
//        // 각각의 BarDataSet 생성
//        val diarrheaDataSet = BarDataSet(diarrheaEntries, "설사")
//        diarrheaDataSet.color = yellow_diarrhea_Color // 노란색
//        diarrheaDataSet.setDrawValues(true)
//        diarrheaDataSet.valueTextSize = 12f
//
//
//        val constipationDataSet = BarDataSet(constipationEntries, "변비")
//        constipationDataSet.color = red_constipation_Color // 빨간색
//        constipationDataSet.setDrawValues(true)
//        constipationDataSet.valueTextSize = 12f
//
//        val colorChangeDataSet = BarDataSet(colorChangeEntries, "변색깔 이상")
//        colorChangeDataSet.color = blue_strange_Color // 파란색
//        colorChangeDataSet.setDrawValues(true) // 값 숨기기 (선택)
//        colorChangeDataSet.valueTextSize = 12f
//
//        // BarData 생성
//        val barData = BarData(diarrheaDataSet, constipationDataSet, colorChangeDataSet)
//        // 막대 두께 설정 (0.5f는 기본값, 더 작게/크게 설정 가능)
//        barData.barWidth = 0.4f // 두께를 0.8로 설정 (값 조정 가능)
//        barChart.data = barData
//
//        // BarChart 스타일 설정
//        barChart.description.isEnabled = false
//        barChart.axisLeft.textColor = Color.BLACK
//        barChart.axisRight.isEnabled = false
//        barChart.xAxis.isEnabled = false
//        barChart.animateY(1000) // 애니메이션 효과
//
//        // 그래프 새로고침
//        barChart.invalidate()


        //여기는 캘린더 코드
        // CalendarView 객체 가져오기
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        // 날짜 선택 이벤트 처리
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // 선택된 날짜를 Toast로 표시 (테스트 용도)
            val selectedDate = "$year. ${month + 1}. $dayOfMonth."
            Toast.makeText(this, " $selectedDate 의 정보로 이동합니다.", Toast.LENGTH_SHORT).show()

            // activity_today_stats로 이동
            val intent = Intent(this, TodayActivity::class.java)

            // 선택된 날짜를 전달
            intent.putExtra("SELECTED_DATE", selectedDate)
            startActivity(intent)
        }
    }

    // 서버 연결 함수 추가
    private fun fetchMonthlyStats(patientId: Int, year: Int, month: Int, barChart: BarChart) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        apiService.getMonthlyStats(patientId, year, month).enqueue(object : Callback<MonthlyStatsResponse> {
            override fun onResponse(call: Call<MonthlyStatsResponse>, response: Response<MonthlyStatsResponse>) {
                if (response.isSuccessful) {
                    val stats = response.body()
                    stats?.let {
                        displayGraph(it, barChart) // 데이터를 그래프에 반영
                    }
                } else {
                    Toast.makeText(this@MonthActivity, "데이터 로드 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MonthlyStatsResponse>, t: Throwable) {
                Toast.makeText(this@MonthActivity, "통신 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //그래프 그리기 함수 추가
    private fun displayGraph(stats: MonthlyStatsResponse, barChart: BarChart) {
        // 그래프에 사용할 색상 지정
        val yellowDiarrheaColor = Color.parseColor("#E8C300")
        val redConstipationColor = Color.parseColor("#FF5656")
//        val blue_strange_Color = Color.parseColor("#6893FF")

        // 데이터 생성
        val diarrheaEntries = ArrayList<BarEntry>()
        val constipationEntries = ArrayList<BarEntry>()

        // 서버 응답 데이터를 그래프 데이터로 변환
        constipationEntries.add(BarEntry(1f, stats.bristol_1_2.toFloat())) // 변비 데이터
        diarrheaEntries.add(BarEntry(2f, stats.bristol_6_7.toFloat())) // 설사 데이터

        // 각각의 BarDataSet 생성
        val constipationDataSet = BarDataSet(constipationEntries, "변비")
        constipationDataSet.color = redConstipationColor // 빨간색
        constipationDataSet.setDrawValues(true)
        constipationDataSet.valueTextSize = 12f

        val diarrheaDataSet = BarDataSet(diarrheaEntries, "설사")
        diarrheaDataSet.color = yellowDiarrheaColor // 노란색
        diarrheaDataSet.setDrawValues(true)
        diarrheaDataSet.valueTextSize = 12f

        // BarData 생성
        val barData = BarData(constipationDataSet, diarrheaDataSet)
        barData.barWidth = 0.4f // 막대 두께 설정
        barChart.data = barData

        // BarChart 스타일 설정
        barChart.description.isEnabled = false
        barChart.axisLeft.textColor = Color.BLACK
        barChart.xAxis.textColor = Color.BLACK
        barChart.axisRight.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.animateY(1000) // 애니메이션 효과

        // 그래프 새로고침
        barChart.invalidate()
    }
}
