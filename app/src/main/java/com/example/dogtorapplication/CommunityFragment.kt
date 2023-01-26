package com.example.dogtorapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_community.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class CommunityFragment : Fragment() {
    // 파이어 베이스에 데이터 저장을 위한 객체 획득
    var Firestore : FirebaseFirestore?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_community, container, false)

        // 메인 액티비티의 함수 사용하기 위해 메인 액티비티 받아오기
        val mActivity = activity as MainActivity

        // 액티비티에서 정의해둔 changeFragment로 화면 전환
        view.addwrite.setOnClickListener {
            mActivity.changeFragment(2)
        }

        return view
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Person 클래스 ArrayList 생성
        var post : ArrayList<writeInformation> = arrayListOf()

        init {  // post의 문서를 불러온 뒤 writeInformation 데이터 클라스로 변환해 ArrayList에 담음
            Firestore?.collection("post")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                post.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(writeInformation::class.java)
                    post.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)        }

        override fun getItemCount(): Int {
            return post.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            val storage : FirebaseStorage = FirebaseStorage.getInstance("gs://carrotmarket-aa104.appspot.com")
            val storageRef = storage.reference

            storageRef.child(post[position].imgUri+"/0.png").getDownloadUrl()
                .addOnCompleteListener(object : OnCompleteListener<Uri?> {
                    override fun onComplete(task: Task<Uri?>) {
                        if (task.isSuccessful()) {

                            // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(holder.itemView.context)
                                .load(task.getResult())
                                .into(viewHolder.image)
                        } else {
                            // URL을 가져오지 못하면 토스트 메세지
                            Toast.makeText(
                                viewHolder.context,
                                task.getException()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })


            viewHolder.titleTextView.text = post[position].title
            viewHolder.categoryTextView.text = post[position].category
            viewHolder.dateTextView.text = post[position].date

        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

}