package com.example.dogtorapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dogtorapplication.databinding.ActivityLoginBinding
import com.example.dogtorapplication.databinding.ActivitySignup01Binding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    // 뷰 바인딩을 위한 객체 획득
    lateinit var binding: ActivityLoginBinding

    // 로그인 인증을 위한 객체 획득
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth // 객체 정의

        // 구글 인증 이메일 받아오기 위한 변수 및 함수 선언
        var email: String? = null
        fun checkAuth():Boolean{
            val currentUser = auth.currentUser
            return currentUser?.let{
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let{
                false
            }
        }

        // 로그인 버튼 클릭 시 로그인 정보 확인
        binding.loginImageButton.setOnClickListener {
            signin()
        }

        // 회원가입 버튼 클릭 시 회원 가입 화면으로 전환
        binding.membershipImageButton.setOnClickListener {
            val nextIntent = Intent(this, Signup01Activity::class.java)
            startActivity(nextIntent)
        }

        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            //구글 로그인 결과 처리
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider
                    .getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // 구글 로그인 성공
                            email = account.email
                            moveMainPage(task.result?.user) // moveMainPage 함수를 통해 메인 화면으로 넘어가기

                        } else {
                            Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()


            }
        }

        binding.googleLogin.setOnClickListener {
            //구글 로그인
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            // 구글의 인증 관리 앱 실행
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }

    }

    // 앱이 종료 후 다시 시작 시 로그인 유지 될 수 있도록
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!==null){ // 이미 로그인 되어있을 시 바로 메인 액티비티로 이동
            moveMainPage(auth.currentUser)
        }
    }

    // 로그인 시에 불러 로그인되도록 하는 함수
    fun signin(){
        // signInWithEmailAndPassword() 이용하여 로그인 가능하도록 설정
        auth?.signInWithEmailAndPassword(binding.loginIdEditText.text.toString(),binding.loginPasswordEditText.text.toString())
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){ // 성공 시
                    moveMainPage(task.result?.user) // moveMainPage 함수를 통해 메인 화면으로 넘어가기
                }else{ // 실패 시
                    // Show the error message
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()
                }

            }
    }

    // 로그인 성공 시 메인 액티비티로 넘어가주는 함수
    fun moveMainPage(user: FirebaseUser?){
        if(user !=null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }


}

