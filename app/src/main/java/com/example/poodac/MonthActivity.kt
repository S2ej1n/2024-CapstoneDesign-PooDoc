//package com.example.poodac
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.graphics.Color
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.utils.ColorTemplate
//
//class MonthActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_month_state)
//
//        // BarChart 초기화 코드
//        val barChart = findViewById<BarChart>(R.id.graph_layout) // XML에서 그래프의 ID를 가져옴
//
//        // 데이터 생성
//        val entries = ArrayList<BarEntry>()
//        entries.add(BarEntry(1f, 2f)) // 설사
//        entries.add(BarEntry(2f, 5f)) // 변비
//        entries.add(BarEntry(3f, 3f)) // 변색깔이상
//        entries.add(BarEntry(4f, 6f)) // 설사
//        entries.add(BarEntry(5f, 4f)) // 변비
//
//        // BarDataSet 생성 (그래프 데이터와 라벨 설정)
//        val barDataSet = BarDataSet(entries, "이번 달 배변 위험 통계")
//        barDataSet.colors = listOf(Color.YELLOW, Color.RED, Color.BLUE, Color.YELLOW, Color.RED) // 각 데이터 색상
//        barDataSet.valueTextSize = 12f
//
//        // BarData 생성 및 BarChart에 설정
//        val barData = BarData(barDataSet)
//        barChart.data = barData
//
//        // BarChart 스타일 설정
//        barChart.description.isEnabled = false
//        barChart.axisLeft.textColor = Color.BLACK
//        barChart.xAxis.textColor = Color.BLACK
//        barChart.axisRight.isEnabled = false
//        barChart.animateY(1000) // 애니메이션 효과
//
//        // 그래프 새로고침
//        barChart.invalidate()
//    }
//}
