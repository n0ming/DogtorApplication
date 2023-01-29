package com.example.dogtorapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogtorapplication.databinding.ListItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val context: Context, val itemList: MutableList<writeInformation>): RecyclerView.Adapter<MyViewHolder>() {
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        return MyViewHolder(ListItemBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        Log.w("lee", itemList.size.toString())
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.itemView.setOnClickListener{
            Intent(context, PostActivity::class.java).apply{
                putExtra("local",  holder.binding.localTextView.text.toString())
                putExtra("category",  holder.binding.categoryTextView.text.toString())
                putExtra("title",  holder.binding.titleTextView.text.toString())
                putExtra("date",  holder.binding.dateTextView.text.toString())

                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run{context.startActivity(this)}
        }

        holder.binding.run {
            localTextView.text=data.local
            categoryTextView.text=data.category
            titleTextView.text=data.title
            dateTextView.text=data.date

        }

        //스토리지 이미지 다운로드
        val imgRef= storage
            .reference
            .child("images/${data.docId}.jpg")

        imgRef.getDownloadUrl().addOnCompleteListener { task ->
            if (task.isSuccessful) { // 성공
                Glide.with(context ) // Glide 사용하여 이미지 핸들링
                    .load(task.result)
                    .into(holder.binding.image)

            }
        }
    }
}

