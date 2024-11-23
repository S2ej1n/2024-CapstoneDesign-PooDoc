package com.example.poodac

// 로그인 요청 데이터 (내가 보내는거)
data class LoginRequest(
    val user_id: String,
    val user_pw: String
)