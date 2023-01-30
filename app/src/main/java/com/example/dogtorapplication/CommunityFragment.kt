package com.example.dogtorapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogtorapplication.databinding.FragmentCommunityBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class CommunityFragment : Fragment() {


    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage

    private lateinit var mBinding : FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCommunityBinding.inflate(inflater, container, false)

        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        // 메인 액티비티의 함수 사용하기 위해 메인 액티비티 받아오기
        val mActivity = activity as MainActivity

        // 액티비티에서 정의해둔 transaction으로 이동 구현
        mBinding.logo.setOnClickListener {
            mActivity.transaction(1)
        }

        mBinding.mytransaction.setOnClickListener {
            val intent = Intent(activity, MydogActivity::class.java)
            startActivity(intent)
        }

        // 글 쓰는 버튼 클릭 시 화면 전환
        mBinding.addwrite.setOnClickListener {
            val intent = Intent(activity, WriteActivity::class.java)
            startActivity(intent)
        }

        // 처음에 뜨는 화면은 전체 카테고리라서 버튼 클릭 상태로 설정
        mBinding.categorybutton0?.isSelected = true

        // 카테고리 선택 시
        mBinding.categorybutton0.setOnClickListener{
            // 버튼 클릭 효과를 위한 설정
            mBinding.categorybutton0?.isSelected = true
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = false
            mBinding.categorybutton3?.isSelected = false
            mBinding.categorybutton4?.isSelected = false
            mBinding.categorybutton5?.isSelected = false
            mBinding.categorybutton6?.isSelected = false
            mBinding.categorybutton7?.isSelected = false

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .orderBy("date",Query.Direction.DESCENDING) // 날짜 기준 내림차순으로
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        mBinding.categorybutton1.setOnClickListener{
            mBinding.categorybutton0?.isSelected = false
            mBinding.categorybutton1?.isSelected = true
            mBinding.categorybutton2?.isSelected = false
            mBinding.categorybutton3?.isSelected = false
            mBinding.categorybutton4?.isSelected = false
            mBinding.categorybutton5?.isSelected = false
            mBinding.categorybutton6?.isSelected = false
            mBinding.categorybutton7?.isSelected = false

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .whereEqualTo("category",mBinding.categorybutton1.text.toString())
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        mBinding.categorybutton2.setOnClickListener{

            mBinding.categorybutton0?.isSelected = false
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = true
            mBinding.categorybutton3?.isSelected = false
            mBinding.categorybutton4?.isSelected = false
            mBinding.categorybutton5?.isSelected = false
            mBinding.categorybutton6?.isSelected = false
            mBinding.categorybutton7?.isSelected = false

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .whereEqualTo("category",mBinding.categorybutton2.text.toString())
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        mBinding.categorybutton3.setOnClickListener{

            mBinding.categorybutton0?.isSelected = false
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = false
            mBinding.categorybutton3?.isSelected = true
            mBinding.categorybutton4?.isSelected = false
            mBinding.categorybutton5?.isSelected = false
            mBinding.categorybutton6?.isSelected = false
            mBinding.categorybutton7?.isSelected = false

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .whereEqualTo("category",mBinding.categorybutton3.text.toString())
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        mBinding.categorybutton4.setOnClickListener{

            mBinding.categorybutton0?.isSelected = false
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = false
            mBinding.categorybutton3?.isSelected = false
            mBinding.categorybutton4?.isSelected = true
            mBinding.categorybutton5?.isSelected = false
            mBinding.categorybutton6?.isSelected = false
            mBinding.categorybutton7?.isSelected = false

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .whereEqualTo("category",mBinding.categorybutton4.text.toString())
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        mBinding.categorybutton5.setOnClickListener{

            mBinding.categorybutton0?.isSelected = false
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = false
            mBinding.categorybutton3?.isSelected = false
            mBinding.categorybutton4?.isSelected = false
            mBinding.categorybutton5?.isSelected = true
            mBinding.categorybutton6?.isSelected = false
            mBinding.categorybutton7?.isSelected = false

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .whereEqualTo("category",mBinding.categorybutton5.text.toString())
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        mBinding.categorybutton6.setOnClickListener{

            mBinding.categorybutton0?.isSelected = false
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = false
            mBinding.categorybutton3?.isSelected = false
            mBinding.categorybutton4?.isSelected = false
            mBinding.categorybutton5?.isSelected = false
            mBinding.categorybutton6?.isSelected = true
            mBinding.categorybutton7?.isSelected = false

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .whereEqualTo("category",mBinding.categorybutton6.text.toString())
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }
        mBinding.categorybutton7.setOnClickListener{

            mBinding.categorybutton0?.isSelected = false
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = false
            mBinding.categorybutton3?.isSelected = false
            mBinding.categorybutton4?.isSelected = false
            mBinding.categorybutton5?.isSelected = false
            mBinding.categorybutton6?.isSelected = false
            mBinding.categorybutton7?.isSelected = true

            // 버튼에 맞는 데이터 불러오기
            // 컬랙션 가져오기
            db.collection("post")
                .whereEqualTo("category",mBinding.categorybutton7.text.toString())
                .get()
                .addOnSuccessListener { result -> // 성공
                    val itemList = mutableListOf<writeInformation>()
                    for (document in result) {
                        val item = document.toObject(writeInformation::class.java)
                        item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                        itemList.add(item)
                    }

                    mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                    mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
                }
                .addOnFailureListener { exception -> // 실패
                    Log.d("lee", "Error getting documents: ", exception)
                }
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 컬랙션 가져오기
        db.collection("post")
            .orderBy("date",Query.Direction.DESCENDING) // 날짜 기준 내림차순으로
            .get()
            .addOnSuccessListener { result -> // 성공
                val itemList = mutableListOf<writeInformation>()
                for (document in result) {
                    val item = document.toObject(writeInformation::class.java)
                    item.docId=document.id // 내부적으로 식별할 수 있는 게시물 식별자
                    itemList.add(item)
                }

                mBinding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
                mBinding.recyclerview.adapter= MyAdapter(requireActivity(),itemList)
            }
            .addOnFailureListener { exception -> // 실패
                Log.d("lee", "Error getting documents: ", exception)
            }
    }
}
