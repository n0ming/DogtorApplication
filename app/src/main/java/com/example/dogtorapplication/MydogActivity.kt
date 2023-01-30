package com.example.dogtorapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogtorapplication.databinding.ActivityMydogBinding
import com.example.dogtorapplication.databinding.ActivityMydogWriteBinding
import com.example.dogtorapplication.recycler.DogRecyclerItemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_my.view.*


class MydogActivity : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityMydogBinding
    private lateinit var binding2 : ActivityMydogWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMydogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding2= ActivityMydogWriteBinding.inflate(layoutInflater)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()




        binding.retouch.setOnClickListener{
            val intent = Intent(this,MydogWriteActivity::class.java)
            startActivity(intent)
        }
        binding2.done.setOnClickListener{
            // 회원의 정보 가져오기
            db.collection("doginfo")
                .orderBy("date",Query.Direction.DESCENDING) // 날짜 기준 내림차순으로
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        val item_user = document.toObject(mydogIndformation::class.java)

                        // 컬랙션 가져오기
                        db.collection("doginfo")
                            .whereEqualTo("userID",item_user.userID.toString())
                            .get()
                            .addOnSuccessListener { result -> // 성공
                                val itemList2 = mutableListOf<mydogIndformation>()
                                for (document in result) {
                                    val item = document.toObject(mydogIndformation::class.java)
                                    item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                                    itemList2.add(item)
                                }

                                binding.recyclerviewDog.layoutManager = LinearLayoutManager(this)
                                binding.recyclerviewDog.adapter= DogRecyclerItemAdapter(this,itemList2)
                            }
                            .addOnFailureListener { exception -> // 실패
                                Log.d("lee", "Error getting documents: ", exception)
                            }
                    }
                }
        }

        // 회원의 정보 가져오기
        db.collection("doginfo")
            .orderBy("date",Query.Direction.DESCENDING) // 날짜 기준 내림차순으로
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val item_user = document.toObject(mydogIndformation::class.java)

                    // 컬랙션 가져오기
                    db.collection("doginfo")
                        .whereEqualTo("userID",item_user.userID.toString())
                        .get()
                        .addOnSuccessListener { result -> // 성공
                            val itemList2 = mutableListOf<mydogIndformation>()
                            for (document in result) {
                                val item = document.toObject(mydogIndformation::class.java)
                                item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                                itemList2.add(item)
                            }

                            binding.recyclerviewDog.layoutManager = LinearLayoutManager(this)
                            binding.recyclerviewDog.adapter= DogRecyclerItemAdapter(this,itemList2)
                        }
                        .addOnFailureListener { exception -> // 실패
                            Log.d("lee", "Error getting documents: ", exception)
                        }
                }
            }

    }
}