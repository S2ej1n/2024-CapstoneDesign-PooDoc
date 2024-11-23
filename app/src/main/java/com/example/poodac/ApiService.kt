package com.example.poodac

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // 24.11.22.
    // 환자 목록 보기
    @GET("api/patient_list")
    fun getPatientList()
            : Call<List<Patient>>

    // 오늘의 배변 기록 조회
    @GET("api/daily_stats")
    fun getDailyStats(@Query("p_id") patientId: Int)
            : Call<DailyStatsResponse>

    // 로그인 요청
    @POST("api/login")
    fun login(@Body request: LoginRequest)
            : Call<LoginResponse>
}