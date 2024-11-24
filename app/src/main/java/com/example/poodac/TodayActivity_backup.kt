package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import java.text.SimpleDateFormat
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import java.util.*

class TodayActivity_backup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_stats)

        //이거 나중에 외부 파일에 적을까 생각중. 배변시간
        data class Record(val startTime: String, val endTime: String, val duration: String)

        //캘린더뷰에서 선택한 날짜 가져오기
        // 전달된 날짜 가져오기
        val selectedDate = intent.getStringExtra("SELECTED_DATE")

        // TextView에 날짜 표시 (예제)
        val dateTextView = findViewById<TextView>(R.id.today_date) // activity_today_stats에 있는 TextView ID
        dateTextView.text = "$selectedDate"

        // 서버에서 불러온 숫자. fetchNumberFromServer() 를 추가하면 됨
        val serverNumber = fetchNumberFromServer()

        // 제일 큰 메인 똥 정보들
        val mainDdongImageView: ImageView = findViewById(R.id.main_ddong)
        val mainDdongNameTextView: TextView = findViewById(R.id.main_ddong_name)
        val mainDdongExplanTextView: TextView = findViewById(R.id.main_ddong_explan)

        // 텍스트 색깔 변경 - 밑의 작은 똥 정보들
        val poo1Text: TextView = findViewById(R.id.poo1text)
        val poo2Text: TextView = findViewById(R.id.poo2text)
        val poo3Text: TextView = findViewById(R.id.poo3text)
        val poo4Text: TextView = findViewById(R.id.poo4text)
        val poo5Text: TextView = findViewById(R.id.poo5text)
        val poo6Text: TextView = findViewById(R.id.poo6text)
        val poo7Text: TextView = findViewById(R.id.poo7text)

        // 사진 변경 - 사진 색깔 변경임
        val poo1ImageView: ImageView = findViewById(R.id.poo1)
        val poo2ImageView: ImageView = findViewById(R.id.poo2)
        val poo3ImageView: ImageView = findViewById(R.id.poo3)
        val poo4ImageView: ImageView = findViewById(R.id.poo4)
        val poo5ImageView: ImageView = findViewById(R.id.poo5)
        val poo6ImageView: ImageView = findViewById(R.id.poo6)
        val poo7ImageView: ImageView = findViewById(R.id.poo7)

        // 서버에서 받은 숫자에 따라 UI 업데이트
        updateUI(serverNumber, mainDdongImageView, mainDdongNameTextView, mainDdongExplanTextView,
            poo1ImageView, poo2ImageView, poo3ImageView, poo4ImageView, poo5ImageView, poo6ImageView, poo7ImageView)
        updateTextColor(serverNumber, poo1Text, poo2Text, poo3Text, poo4Text, poo5Text, poo6Text, poo7Text)

        //배변 시간 수정
        val tableLayout: TableLayout = findViewById(R.id.tableLayout)
        // 서버나 데이터로부터 정보를 받아오는 코드
//        val startTime: String? = "14:00" // 예: 서버에서 받아온 데이터 (null이면 정보가 없음)
//        val endTime: String? = "15:00"   // 예: 서버에서 받아온 데이터 (null이면 정보가 없음)
//        val duration: String? = "22"  // 예: 서버에서 받아온 데이터 (null이면 정보가 없음)

        val records = listOf(
            Record("14:00", "15:00", "22"),
            Record("16:00", "16:30", "15")
            // 추가 기록...
        )

        // 기존 레이아웃 초기화
        tableLayout.removeAllViews()

        val paddingInDp = 4
        val scale = resources.displayMetrics.density
        val paddingInPx = (paddingInDp * scale + 0.5f).toInt()

        if (records.isEmpty()) {
            val noRecordTableRow = TableRow(this)
            val noRecordLayout = LinearLayout(this).apply {
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
                setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)
            }
            val noRecordTextView = TextView(this).apply {
                text = "오늘의 배변 기록이 존재하지 않아요."
                textSize = 12f
                setTextColor(resources.getColor(R.color.textcolor, theme))
            }
            noRecordLayout.addView(noRecordTextView)
            noRecordTableRow.addView(noRecordLayout)
            tableLayout.addView(noRecordTableRow)
        }else {
            for (record in records) {
                val recordTableRow = TableRow(this)
                val recordLayout = LinearLayout(this).apply {
                    layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx) // 패딩 추가
                }

                val startTimeTextView = TextView(this).apply {
                    text = record.startTime ?: ""
                    textSize = 12f
                    setTextColor(resources.getColor(R.color.textcolor, theme))
                }

                val endTimeTextView = TextView(this).apply {
                    text = record.endTime ?: ""
                    textSize = 12f
                    setTextColor(resources.getColor(R.color.textcolor, theme))
                }

                val durationTextView = TextView(this).apply {
                    text = "${record.duration ?: "0"} 분"
                    textSize = 12f
                    setTextColor(resources.getColor(R.color.textcolor, theme))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(50, 0, 0, 0) // 오른쪽에 50dp 만큼의 간격을 추가
                    }
                    gravity = Gravity.END
                }

                recordLayout.addView(startTimeTextView)
                recordLayout.addView(TextView(this).apply { text = " ~ " })
                recordLayout.addView(endTimeTextView)
                recordLayout.addView(durationTextView)

                recordTableRow.addView(recordLayout)

                tableLayout.addView(recordTableRow)
            }
        }
    }

    private fun fetchNumberFromServer(): Int {
        // 서버와의 통신 코드 작성 (네트워크 통신, REST API 호출 등)
        // 서버로부터 받아온 숫자를 3으로 가정
        return 5
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