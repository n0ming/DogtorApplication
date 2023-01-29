package com.example.dogtorapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.dogtorapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener   {

    // 뷰 바인딩을 위한 객체 획득
    lateinit var binding: ActivityMainBinding

    // 파이어 베이스에 데이터 저장을 위한 객체 획득
    private lateinit var auth: FirebaseAuth
    var Firestore : FirebaseFirestore?=null

    // 프래그먼트 제어를 위한 객체 받아오기
    val fragmentmanger: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 기본 작업
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원 정보 저장을 위한 변수 선언
        auth = FirebaseAuth.getInstance()
        Firestore = FirebaseFirestore.getInstance()

        // 제일 처음 띄울 프래그먼트
        val carefragment = CareFragment()
        // 프래그먼트 제어를 위한 객체 받아오기
        val transaction = fragmentmanger.beginTransaction()

        // 프래그먼트
        transaction.add(R.id.main_content, carefragment)
        transaction.commit()

        // 정보 저장
        if(true)
        {
            var userInfo = userInformation()

            userInfo.userID=auth?.uid
            userInfo.userEmail=auth?.currentUser?.email
            Firestore?.collection("users")?.document(auth?.uid.toString())?.set(userInfo)

        }

        // 갤러리 접근 권한 물어보기 및 얻어오기
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

        // 바텀 네비게이션과 바인딩하여
        binding.bottomNavigation.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(p0 : MenuItem): Boolean { // 바텀 네비게이션 클릭 시 이동
        when(p0.itemId){
            R.id.action_care->{
                var careFragment = CareFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,careFragment).commit()
                return true
            }
            R.id.action_explore->{
                var exploreFragment = ExploreFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,exploreFragment).commit()
                return true
            }

            R.id.action_community->{
                var communityFragment = CommunityFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,communityFragment).commit()
                return true
            }
            R.id.action_user->{
                var myFragment = MyFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,myFragment).commit()
                return true
            }
        }
        return false
    }


}
