package com.example.dogtorapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_my.view.*

class MyFragment : Fragment() {


    // 파이어 베이스 인증 및 데이터 사용 위한 객체
    private lateinit var auth: FirebaseAuth
    var Firestore : FirebaseFirestore?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_my,container,false)

        auth = FirebaseAuth.getInstance()
        Firestore = FirebaseFirestore.getInstance()

        val myInf = view.myInformation
        val tv = view.tv_userEmail  // email 입력할 텍스트 뷰 지정
        val userEmail= auth?.currentUser?.email.toString() // 로그인한 이메일을 불러오기
        val logOut = view.logout // 로그아웃을 위한 변수 선언

        tv.text = userEmail // 받아온 이메일 정보로 text 설정


        myInf.setOnClickListener {// 내정보 버튼 클릭 시 이동
            val intent = Intent(activity, com.example.dogtorapplication.myinformation::class.java)
            startActivity(intent)
        }

        logOut.setOnClickListener {// 로그아웃 버튼 클릭 시 로그아웃 및 로그인 화면으로 전환
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}