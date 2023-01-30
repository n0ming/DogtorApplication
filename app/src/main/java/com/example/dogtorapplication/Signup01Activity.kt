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
    var email: String? = null

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
    fun checkAuth():Boolean{
        val currentUser = auth.currentUser
        return currentUser?.let{
            email = currentUser.email
            currentUser.isEmailVerified
        } ?: let{
            false
        }
    }
    fun signinEmail(){
        // createUserWithEmailAndPassword() 이용하여 사용자 생성
        auth.createUserWithEmailAndPassword(binding.emailEditText.text.toString(),binding.numberEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){ // 생성이 되었다면
                    val nextIntent = Intent(this, LoginActivity::class.java)
                    startActivity(nextIntent)
                    /*
                    Toast.makeText(baseContext,
                        "인증 메일이 전송되었습니다. 메일을 통해 인증해주세요!",
                        Toast.LENGTH_SHORT).show()


                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener {sendTask ->
                            if(sendTask.isSuccessful){
                                if (checkAuth()) {
                                    // 로그인 성공
                                    Toast.makeText(baseContext,
                                        "가입을 환영합니다! 마이페이지에서 내정보를 입력해주세요.",
                                        Toast.LENGTH_SHORT).show()
                                    val nextIntent = Intent(this, LoginActivity::class.java)
                                    startActivity(nextIntent)

                                } else {
                                    // 발송된 메일로 인증 확인을 안 한 경우
                                    Toast.makeText(baseContext,
                                        "전송된 메일로 이메일 인증이 되지 않았습니다.",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Toast.makeText(this,"메일 전송에 실패하였습니다. 유효한 이메일로 다시 가입해주세요", Toast.LENGTH_LONG).show() // 토스트 메세지 띄우기

                            }


                        }
*/
                }
                else{ // 생성을 못했다면
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show() // 토스트 메세지 띄우기
                    //Show the error message
                }
            }
    }
}