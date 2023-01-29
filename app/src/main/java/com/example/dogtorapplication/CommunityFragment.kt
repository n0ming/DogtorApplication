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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_community.view.*

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

        mBinding.addwrite.setOnClickListener {
            val intent = Intent(activity, WriteActivity::class.java)
            startActivity(intent)
        }

        // 카테고리 선택 시 다르게 가져올 수 있도록
        mBinding.categorybutton1.setOnClickListener{
            mBinding.categorybutton1?.isSelected = true
            mBinding.categorybutton2?.isSelected = false

            // 컬렉션을 모두 가져오기
            db.collection("post")
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
            mBinding.categorybutton1?.isSelected = false
            mBinding.categorybutton2?.isSelected = true

            // 컬렉션을 중 구토만 가져오기
            db.collection("post")
                .whereEqualTo("category","구토")
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

        // 컬렉션을 모두 가져오기
        db.collection("post")
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
