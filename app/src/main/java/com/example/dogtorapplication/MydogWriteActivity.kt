package com.example.dogtorapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dogtorapplication.databinding.ActivityMydogWriteBinding
import com.example.dogtorapplication.databinding.ActivityWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MydogWriteActivity : AppCompatActivity() {

    lateinit var binding: ActivityMydogWriteBinding

    private lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore

    var username : String? = null
    var userdogname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMydogWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        /*
        // 강아지 이름 가져오기
        db.collection("userplus")
            .whereEqualTo("userID",auth.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var userDTO = document.toObject(userplusImformation::class.java)
                    binding.tv1.text = userDTO.dogName.toString()
                }
            }

         */

        binding.done.setOnClickListener {
            if( binding.tv2.text.isNotEmpty()&& binding.tv4.text.isNotEmpty()&& binding.tv6.text.isNotEmpty()&& binding.tv8.text.isNotEmpty()){
                // 데이터를 저장
                saveStore()

                // 화면 전환
                val intent = Intent(this, MydogActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "데이터를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun saveStore(){

        db.collection("userplus")
            .whereEqualTo("userID",auth.uid.toString())
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents){
                    var userDTO = document.toObject(userplusImformation::class.java)

                    // 저장할 데이터
                    val data = mapOf(
                        "userID" to userDTO.userID.toString(), // uid
                        "dogName" to userDTO.dogName.toString(), // 강아지 이름
                        "userEmail" to userDTO.userEmail.toString(), // 사용자 이메일
                        "userName" to userDTO.userName.toString(), // 사용자 이름
                        "sickName" to binding.tv2.text.toString(), // 병명
                        "sickday" to binding.tv4.text.toString(), // 방문일
                        "hospital" to binding.tv6.text.toString(), // 병원
                        "medicine" to binding.tv8.text.toString(), // 처방 약
                        "date" to dateToString2(Date()) // 쓴 날짜

                    )

                    // 데이터 id 값과 함께 저장
                    db.collection("doginfo")
                        .add(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "처방전이 저장되었습니다.",
                                Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {// 실패
                            Log.w("kkkkk", "data save error", it)
                        }
                }
            }
            .addOnFailureListener { exception->
                Log.w("wwww","wer",exception)
            }
    }

    fun dateToString2(date: Date): String {
        val format = SimpleDateFormat("MM.dd hh:mm")
        return format.format(date)
    }
}