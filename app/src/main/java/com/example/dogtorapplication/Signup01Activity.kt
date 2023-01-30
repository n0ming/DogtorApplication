package com.example.dogtorapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dogtorapplication.databinding.ActivitySignup01Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Signup01Activity : AppCompatActivity() {

    // 뷰 바인딩을 위한 객체 획득
    lateinit var binding: ActivitySignup01Binding

    // 파이어 베이스 회원가입을 위한 객체 획득
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignup01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth // 객체 정의

        // 가입 버튼 클릭 시 가입 진행될 수 있도록
        binding.loginSubmitButton.setOnClickListener {
            signinEmail()
        }

        // 취소 버튼 클릭 시 뒤로갈 수 있도록
        binding.loginCancleButton.setOnClickListener {
            finish()
        }

    }

    fun signinEmail(){
        // createUserWithEmailAndPassword() 이용하여 사용자 생성
        auth.createUserWithEmailAndPassword(binding.emailEditText.text.toString(),binding.numberEditText.text.toString())
            .addOnCompleteListener(this) {
                    task ->
                if(task.isSuccessful){ // 생성이 되었다면
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { sendTask ->
                            if(sendTask.isSuccessful){
                                Toast.makeText(this,"가입이 완료 되었습니다. My에서 내정보를 설정해주세요", Toast.LENGTH_LONG).show() // 토스트 메세지 띄우기
                            }
                            else{
                                Toast.makeText(this,"메일 전송에 실패하였습니다. 유효한 이메일로 다시 가입해주세요", Toast.LENGTH_LONG).show() // 토스트 메세지 띄우기

                            }

                        }

                    val nextIntent = Intent(this, LoginActivity::class.java)
                    startActivity(nextIntent)
                }
                else{ // 생성을 못했다면
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show() // 토스트 메세지 띄우기
                    //Show the error message
                }
            }
    }
}