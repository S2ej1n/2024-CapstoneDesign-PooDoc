package com.example.poodac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.text.Editable
import android.widget.Toast
import android.widget.TextView

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
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 입력이 완료되었으면 다음 화면으로 이동
                val intent = Intent(this, ManageActivity::class.java)
                startActivity(intent)
            }
        }
//        val button: Button = findViewById(R.id.login_but)
//        button.setOnClickListener {
//            val intent = Intent(this, ManageActivity::class.java)
//            startActivity(intent) // 액티비티 시작
    }

}