package com.example.poodac

// 오늘의 배변 기록 데이터 클래스
data class TimeInfo(
    val duration: Int,
    val starttime: String,
    val endtime: String
)

data class DailyStatsResponse(
    val bristol: List<Int>,
    val count: Int,
    val time_info: List<TimeInfo>
)