package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.text.Editable
import android.widget.Toast
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editID = findViewById<EditText>(R.id.editID)
        val editPW = findViewById<EditText>(R.id.editPW)
        val loginButton = findViewById<Button>(R.id.login_but)

        // 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            val idInput = editID.text.toString().trim()
            val pwInput = editPW.text.toString().trim()

            if (idInput.isEmpty() || pwInput.isEmpty()) {
                // 입력 칸 중 하나라도 비어 있으면 경고 메시지 표시
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 서버에 로그인 요청
//                loginToServer(idInput, pwInput)

                //(테스트용)
                val intent = Intent(this, ManageActivity::class.java)
                startActivity(intent)
                // ------------------------------------------------
            }
        }
    }

    //로그인 요청 함수
    private fun loginToServer(userId: String, userPw: String) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val loginRequest = LoginRequest(user_id = userId, user_pw = userPw)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val server_login_response = response.body()?.message ?: "UnknownError"


                    when (server_login_response) {
                        "successful" -> {
                            // 로그인 성공 시 다음 화면으로 이동
                            val intent = Intent(this@LoginActivity, ManageActivity::class.java)
                            startActivity(intent)
                        }
                        "PWDoesntMatch" -> {
                            // 비밀번호 틀렸을 경우
                            Toast.makeText(this@LoginActivity, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                        }
                        "IDisNOT_exist" -> {
                            // 아이디 틀렸을 경우
                            Toast.makeText(this@LoginActivity, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // 기타 오류 처리
                            Toast.makeText(this@LoginActivity, "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    when (response.code()) {
                        404 -> {
                            // 존재하지 않는 아이디
                            Toast.makeText(this@LoginActivity, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                        }
                        401 -> {
                            // 비밀번호 확인 필요
                            Toast.makeText(this@LoginActivity, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // 기타 오류 처리
                            Toast.makeText(this@LoginActivity, "로그인 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Toast.makeText(this@LoginActivity, "통신 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}