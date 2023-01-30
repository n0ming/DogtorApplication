package com.example.dogtorapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.dogtorapplication.databinding.ActivityWriteBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_myinformation.*
import kotlinx.android.synthetic.main.dialog_user.*
import kotlinx.android.synthetic.main.dialog_user.view.*

class myinformation : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    var Firestore: FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinformation)

        // 인증 기능 불러오기
        auth = FirebaseAuth.getInstance()
        Firestore = FirebaseFirestore.getInstance()

        var tv2: TextView? = findViewById(R.id.tv2)

        backBtn.setOnClickListener {     // 뒤로가기 클릭시 액티비티 종료
            finish()
        }


        // 입력 버튼 클릭 시
        retouch.setOnClickListener {

            // 창 띄우기
            var builder = AlertDialog.Builder(this)
            builder.setTitle("내 정보")

            var v1 = layoutInflater.inflate(R.layout.dialog_user, null) // 띄우는 창 레이아웃 연결

            // 창 받아와서 view 설정
            builder.setView(v1)

            // 창의 요소들 받아오기
            var edit1 = v1.findViewById<EditText>(R.id.editText)
            var edit2 = v1.findViewById<EditText>(R.id.editText2)
            var edit3 = v1.findViewById<EditText>(R.id.editText3)

            // users 컬랙션 가져오기
            Firestore?.collection("userplus")
                ?.document(auth.uid.toString())
                ?.get()
                ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 데이터 구조에 맞게 생성
                    var userDTO = task.result?.toObject(userplusImformation::class.java)

                    // 전화번호 칸 정보
                    if (userDTO?.phoneNumber == null){
                        edit1.setHint("전화번호를 입력해주세요")
                    }else{
                        edit1.setText(userDTO?.phoneNumber)
                    }

                    // 닉네임 칸 정보
                    if (userDTO?.userName == null){
                        edit2.setHint("닉네임을 입력해주세요")
                    }else{
                        edit2.setText(userDTO?.userName)
                    }

                    // 거주지 칸 정보
                    if (userDTO?.local == null){
                        edit3.setHint("거주지를 입력해주세요")
                    }else{
                        edit3.setText(userDTO?.local)

                    }
                }
            }

            // 입력된 데이터 저장
            var listener = DialogInterface.OnClickListener { p0, p1 ->

                var alert = p0 as AlertDialog

                var edit1: EditText? = alert.findViewById<EditText>(R.id.editText)
                var edit2: EditText? = alert.findViewById<EditText>(R.id.editText2)
                var edit3: EditText? = alert.findViewById<EditText>(R.id.editText3)

                var userInfo = userplusImformation() // 데이터 구조

                if (edit1?.text.toString() =="" || edit2?.text.toString()=="" || edit3?.text.toString()=="" ){ // 내용이 입력되지 않았다면
                    Toast.makeText(this, "내용을 전부 입력해주세요", Toast.LENGTH_LONG).show()
                }
                else{ // 내용이 입력되었다면
                    // 데이터 저장
                    userInfo.userID=auth?.uid
                    userInfo.userEmail=auth?.currentUser?.email
                    userInfo.phoneNumber="${edit1?.text}"
                    userInfo.userName="${edit2?.text}"
                    userInfo.local="${edit3?.text}"
                    Firestore?.collection("userplus")
                        ?.document(auth?.uid.toString())
                        ?.set(userInfo)

                    // 화면 전환
                    var intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }

            // 버튼 설정
            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)

            builder.show()
        }


        // 입력된 데이터로 페이지 view 변경하기
        Firestore?.collection("userplus")
            ?.document(auth.uid.toString())
            ?.get()
            ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var userDTO = task.result?.toObject(userplusImformation::class.java)

                // email 설정
                tv2?.text=auth?.currentUser?.email

                // 전화번호 설정
                if (userDTO?.phoneNumber != null){
                    tv4.text=userDTO?.phoneNumber
                }

                // 이름 설정
                if (userDTO?.userName != null){
                    tv6.text=userDTO?.userName
                }

                // 거주지 설정
                if (userDTO?.local != null){
                    tv8.text=userDTO?.local
                }
            }
        }

    }

}