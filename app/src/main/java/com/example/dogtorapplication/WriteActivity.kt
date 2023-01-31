package com.example.dogtorapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dogtorapplication.databinding.ActivityWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityWriteBinding
    lateinit var filePath: String

    private lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage
    lateinit var local:String
    lateinit var name:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        binding.camera.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            requestLauncher.launch(intent)
        }

        binding.backBtn2.setOnClickListener{
            finish()
        }

        binding.finish.setOnClickListener {
            if (binding.imageView2.drawable !== null && binding.etCategory.text.isNotEmpty() && binding.etTitle.text.isNotEmpty() && binding.etContent.text.isNotEmpty()) {
                //store 에 먼저 데이터를 저장후 document id 값으로 업로드 파일 이름 지정
                saveStore()

            } else if (binding.imageView2.drawable == null) {
                Toast.makeText(this, "사진을 업로드 해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "데이터를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            // 회원의 정보 가져오기
            db.collection("userplus")
                .whereEqualTo("userID", auth.uid.toString())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        var userDTO = document.toObject(userplusImformation::class.java)
                        local = userDTO.local.toString()
                        name = userDTO.userName.toString()

                    }

                    if (local==null || name == null) {
                        Toast.makeText(this, "내정보에서 정보를 입력 후 사용해주세요!.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.etCategory.setOnClickListener(View.OnClickListener { v ->
            val popupMenu = PopupMenu(this, v)
            val inflater = popupMenu.menuInflater
            val menu = popupMenu.menu
            inflater.inflate(R.menu.category, menu)
            popupMenu.setOnMenuItemClickListener { item ->

                var x =item.title.toString()  // 메뉴의 타이틀을 불러와서

                binding.etCategory.setText(x)    // 카테고리 텍스트뷰에 출력해줌

                false
            }
            popupMenu.show()
        })
    }


    val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {
        if(it.resultCode === android.app.Activity.RESULT_OK){
            Glide
                .with(getApplicationContext())
                .load(it.data?.data)
                .apply(RequestOptions().override(250, 200))
                .centerCrop()
                .into(binding.imageView2)

            val cursor = contentResolver.query(it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
            cursor?.moveToFirst().let {
                filePath=cursor?.getString(0) as String
            }
        }
    }

    private fun saveStore(){
        // 파이어 베이스에 글을 저장하기 위해 호출되는 함수

        // 회원의 정보 가져오기
        db.collection("userplus")
            .whereEqualTo("userID",auth.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    var userDTO = document.toObject(userplusImformation::class.java)

                    // 저장할 데이터
                    val data = mapOf(
                        "userID" to userDTO.userID.toString(),
                        "local" to userDTO.local.toString(), // 지역
                        "name" to userDTO.userName.toString(), // 이름
                        "title" to binding.etTitle.text.toString(), // 글 제목
                        "content" to binding.etContent.text.toString(), // 글 내용
                        "category" to binding.etCategory.text.toString(), // 카테고리
                        "date" to dateToString(Date()), // 쓴 날짜
                        "imgUrl" to Uri.fromFile(File(filePath)).toString() // imgUri
                    )

                    // 데이터 id 값과 함께 저장
                    db.collection("post")
                        .add(data)
                        .addOnSuccessListener {
                            // 스토리지에 데이터 저장 후 id값으로 스토리지에 이미지 업로드
                            uploadImage(it.id)
                        }
                        .addOnFailureListener {// 실패
                            Log.w("lee", "data save error", it)
                        }
                }
            }


    }

    private fun uploadImage(docId: String){

        // 스토리지를 참조하는 StorageReference 생성
        val storageRef: StorageReference = storage.reference

        // 실제 업로드하는 파일을 참조하는 StorageReference 생성
        val imgRef: StorageReference = storageRef.child("images/${docId}.jpg")

        // 파일 업로드
        var file = Uri.fromFile(File(filePath))
        imgRef.putFile(file)
            .addOnFailureListener { // 실패
                Log.d("lee"," failure."+it)
            }.addOnSuccessListener { // 성공
                Toast.makeText(this, "데이터가 저장되었습니다.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("MM.dd hh:mm")
        return format.format(date)
    }
}

