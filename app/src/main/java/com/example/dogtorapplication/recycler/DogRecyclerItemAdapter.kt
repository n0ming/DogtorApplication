package com.example.dogtorapplication.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogtorapplication.*
import com.example.dogtorapplication.databinding.ListItemBinding
import com.example.dogtorapplication.databinding.MydogItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.mydog_item.view.*

class DogRecyclerItemViewHolder(val binding: MydogItemBinding) : RecyclerView.ViewHolder(binding.root)

class DogRecyclerItemAdapter (val context: Context, val itemList: MutableList<mydogIndformation>) : RecyclerView.Adapter<DogRecyclerItemViewHolder>() {
    lateinit var db : FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogRecyclerItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        db = FirebaseFirestore.getInstance()

        return DogRecyclerItemViewHolder(MydogItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: DogRecyclerItemViewHolder, position: Int) {
        val data = itemList.get(position)

        // view랑 data 연결
        holder.binding.run {
            sickname.text=data.sickName
            datename.text=data.sickday
            hospitalname.text=data.hospital
            medicinename.text=data.medicine
        }    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}