package com.example.dogtorapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_write.*
import kotlinx.android.synthetic.main.fragment_write.view.*
import java.text.SimpleDateFormat
import java.util.*

class WriteFragment : Fragment() {
    // 추가하는 사진 구별을 위한 변수 선언
    var pickImageFromAlbum = 0

    // 이미지 저장을 위한 변수 선언언
    var uriPhoto : Uri? = null
    var uriArray = arrayOfNulls<Uri>(5)
    var imgArray = arrayOfNulls<ImageView>(5)

    // 파이어 베이스에 데이터 저장을 위한 객체 획득
    private lateinit var auth: FirebaseAuth
    var Firestore : FirebaseFirestore?=null
    var fbStorage : FirebaseStorage? = null

    // 회원의 지역 정보 담을 변수
    var local : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_write, container, false)

        // 글 정보 저장을 위한 변수 선언
        auth = FirebaseAuth.getInstance()
        Firestore = FirebaseFirestore.getInstance()
        fbStorage = FirebaseStorage.getInstance()

        // 회원의 지역정보 불러오기
        Firestore?.collection("users")?.document(auth.uid.toString())?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var userDTO = task.result?.toObject(userInformation::class.java)
                local = userDTO?.local
            }
        }

        // 사진 추가 버튼 클릭 시
        view.pictureAdd.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(photoPickerIntent, pickImageFromAlbum)
            Toast.makeText(view.context, "이미지는 최대 5장까지 선택 가능합니다.", Toast.LENGTH_LONG).show()
        }

        // 카테고리 클릭 시 팝업 메뉴 나오도록
        view.et_category.setOnClickListener(View.OnClickListener { v ->
            val popupMenu = PopupMenu(context, v)

            // 만들어 놓은 카테고리 띄워질 수 있도록 설정
            popupMenu.menuInflater.inflate(R.menu.category, popupMenu.menu)

            // item 클릭 시
            popupMenu.setOnMenuItemClickListener { item ->

                var x =item.title.toString()  // 메뉴의 타이틀을 불러와서
                et_category.setText(x)    // 카테고리 텍스트뷰에 출력해줌
                false
            }
            popupMenu.show()
        })

        // 완료 버튼 클릭 시
        view.finish.setOnClickListener {

            // 데이터 구조
            var info = writeInformation()

            // 각 데이터 연결
            info.uid =auth?.uid.toString()
            info.userID =auth?.currentUser?.email
            info.imgUri =Firestore?.collection("post")?.document()?.id // imgUri 전용 id를 생성해서 넣어줌
            info.local=local                         // 회원이 등록해놓은 지역을 불러오도록 함
            info.category=view.et_category.text.toString()
            info.title=view.et_title.text.toString()
            info.content=view.et_content.text.toString()
            info.date = dateToString(Date()) // 쓴 날짜

            // 게시글 정보를 post 컬렉션에 넣어줌
            Firestore?.collection("post")?.document()?.set(info)

            if (ContextCompat.checkSelfPermission(view!!.context,android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED){
                for (i in 0..4){
                    if (uriArray[i]==null){  // 작성자가 선택한 이미지의 갯수
                        break                // 이상으로 for문이 돌면 에러가 발생하므로
                    }                        // if문을 사용하여 null값이면 정지해준다
                    else{
                        ImageUpload(view!!,i,info.imgUri.toString()) //스토리지 이미지 업로드 함수를 사용해줌
                    }
                }
            }

            //프래그먼트 새로고침
            val ft =
                fragmentManager!!.beginTransaction()
            ft.detach(this).attach(this).commit()
        }

        return view
    }

    // 이미지 뷰에 이미지 할당
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        // 이미지뷰 배열에 이미지뷰를 하나씩 할당
        imgArray[0]=pictureAdd
        imgArray[1]=picture1
        imgArray[2]=picture2
        imgArray[3]=picture3
        imgArray[4]=picture4
        if(requestCode == pickImageFromAlbum){
            if (resultCode == Activity.RESULT_OK){
                if (data?.clipData !=null) {
                    val count = data.clipData!!.itemCount  // 사진을 총 몇장 선택했는지 갯수를 count 변수로 만듬
                    if (count > 5) {        //이미지를 5장 이상 선택했을시 다시 선택
                        Toast.makeText(view?.context, "이미지는 최대 5장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                            .show()
                    } else if (count > 1 && count <= 5) {  // 두장에서 다섯장을 선택했다면
                        for (i in 1..count) {
                            uriPhoto = data.clipData?.getItemAt(i - 1)?.uri

                            imgArray[i - 1]?.setImageURI(uriPhoto)  //이미지뷰에 이미지를 띄우기
                            uriArray[i - 1] = uriPhoto          // uri값을 uri 배열에 넣어주기
                        }
                    } else {                                 // 한장만 선택했다면
                        uriPhoto = data?.data
                        pictureAdd.setImageURI(uriPhoto)
                        uriArray[0] = uriPhoto
                    }
                }
            } }

    }

    // 스토리지에 이미지 업로드하는 함수
    private fun ImageUpload(view : View,i : Int,id : String){
        var imgFileName =i.toString() + ".png"
        var storageRef = fbStorage?.reference?.child(id)?.child(imgFileName)

        storageRef?.putFile(uriArray[i]!!)?.addOnSuccessListener {
            Toast.makeText(view.context,"이미지 업로드 성공",Toast.LENGTH_LONG).show()
        }

    }

    // 날짜 받아오기 위한 함수
    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("MM.dd hh:mm")
        return format.format(date)
    }



}