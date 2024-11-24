package com.example.poodac

data class MonthlyStatsResponse(
    val most_frequent_bristol: Int,
    val average_duration: Float,
    val bristol_1_2: Int,
    val bristol_6_7: Int,
    val most_frequent_color: Int
)