package com.example.dogtorapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.dogtorapplication.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class PostActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage

    /*
    var local : String? =null
    var name : String? = null
    var category : String? =null
    var title : String? =null
    var content : String? =null
    var date : String? =null
     */

    private lateinit var binding : ActivityPostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 기본 작업
        binding= ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        val intent = intent /*데이터 수신*/
        var local = intent.extras!!.getString("local")
        var name = intent.extras!!.getString("name")
        var category = intent.extras!!.getString("category")
        var title = intent.extras!!.getString("title")
        var content = intent.extras!!.getString("content")
        var date = intent.extras!!.getString("date")
        var docId = intent.extras!!.getString("docId")

        binding.localTextView.text = local
        binding.categoryTextView.text = category
        binding.tvTitle.text = title
        binding.dateTextView.text = date
        binding.tvContent.text = content
        binding.nameTextView.text = name

        binding.backBtn2.setOnClickListener {     // 뒤로가기 클릭시 액티비티 종료
            finish()
        }

        //스토리지 이미지 다운로드
        val imgRef= storage
            .reference
            .child("images/${docId}.jpg")

        imgRef.getDownloadUrl().addOnCompleteListener { task ->
            if (task.isSuccessful) { // 성공
                Glide.with(this) // Glide 사용하여 이미지 핸들링
                    .load(task.result)
                    .into(binding.imageView2)

            }
        }

        /*
        val intent = intent /*데이터 수신*/
        position = intent.extras!!.getString("position")
        postId = post[position].postID.toString()

        // 게시글 정보 가져오기
        db?.collection("post")?.document(postId.toString())?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var postDTO = task.result?.toObject(writeInformation::class.java)

                binding.categoryTextView.text = postDTO?.category
                binding.tvTitle.text = postDTO?.title
                binding.dateTextView.text = postDTO?.date
                binding.tvContent.text = postDTO?.content

                //스토리지 이미지 다운로드 및 적용
                val imgRef = storage
                    .reference
                    .child("images/${postDTO?.docId}.jpg")
                imgRef.getDownloadUrl().addOnCompleteListener { task ->
                    if (task.isSuccessful) { // 성공
                        Glide.with(this) // Glide 사용하여 이미지 핸들링
                            .load(task.result)
                            .into(binding.imageView2)
                    } else {
                        Log.w("lee", "Error getting documents")

                    }
                }

                binding.backBtn2.setOnClickListener {     // 뒤로가기 클릭시 액티비티 종료
                    finish()
                }
            }
            else{
                Log.w("lee","Error getting documents")

            }
        }



         */


}}
