package com.example.dogtorapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_myinformation.*

class myinformation : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var Firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinformation)

        auth = FirebaseAuth.getInstance()
        Firestore = FirebaseFirestore.getInstance()

        backBtn.setOnClickListener {     // 뒤로가기 클릭시 액티비티 종료
            finish()
        }



        retouch.setOnClickListener {

            var builder = AlertDialog.Builder(this)
            builder.setTitle("정보를 입력해주세요")


            var v1 = layoutInflater.inflate(R.layout.dialog_user, null) // 띄우는 창 레이아웃 연결
            builder.setView(v1)

            var edit1 = v1.findViewById<EditText>(R.id.editText)
            var edit2 = v1.findViewById<EditText>(R.id.editText2)
            var edit3 = v1.findViewById<EditText>(R.id.editText3)

            Firestore?.collection("users")?.document(auth.uid.toString())?.get()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var userDTO = task.result?.toObject(userInformation::class.java)

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

                var userInfo = userInformation() // 데이터 구조

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
                    Firestore?.collection("users")?.document(auth?.uid.toString())?.set(userInfo)

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
        Firestore?.collection("users")?.document(auth.uid.toString())?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var userDTO = task.result?.toObject(userInformation::class.java)

                // email 설정
                tv2.text=userDTO?.userEmail

                // 전화번호 설정
                if (userDTO?.phoneNumber == null){
                    tv4.text="전화번호를 입력해주세요"
                }else{
                    tv4.text=userDTO?.phoneNumber.toString()
                }

                // 이름 설정
                if (userDTO?.userName == null){
                    tv6.text="이름을 입력해주세요"
                }else{
                    tv6.text=userDTO?.userName
                }

                // 거주지 설정
                if (userDTO?.local == null){
                    tv8.text="거주지역을 입력해주세요"
                }else{
                    tv8.text=userDTO?.local
                }
            }
        }

    }

}