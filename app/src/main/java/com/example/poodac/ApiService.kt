package com.example.poodac

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // 24.11.22.
    // 환자 목록 보기
    @GET("api/patient_list")
    fun getPatientList()
            : Call<List<Patient>>

    // 오늘의 배변 기록 조회
    @GET("api/daily_stats")
    fun getDailyStats(
        @Query("p_id") patientId: Int,
        @Query("date") date: String // 날짜 (yyyy-mm-dd 형식)
    ) : Call<DailyStatsResponse>

    // 로그인 요청
    @POST("api/login")
    fun login(@Body request: LoginRequest)
            : Call<LoginResponse>

    // 월별 배변 기록 조회
    @GET("api/monthly_stats")
    fun getMonthlyStats(
        @Query("p_id") patientId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<MonthlyStatsResponse>
}