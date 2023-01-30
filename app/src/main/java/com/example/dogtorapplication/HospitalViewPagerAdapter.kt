package com.example.dogtorapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HospitalViewPagerAdapter(val itemClicked: (HospitalModel) -> Unit):
ListAdapter<HospitalModel, HospitalViewPagerAdapter.ItemViewHolder>(differ){

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(hospitalModel: HospitalModel) {
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
            val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
            val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)
            val tellTextView = view.findViewById<TextView>(R.id.TellTextView)

            titleTextView.text = hospitalModel.title
            priceTextView.text = hospitalModel.location
            tellTextView.text = hospitalModel.tell

            view.setOnClickListener{
                itemClicked(hospitalModel)
            }

            Glide
                .with(thumbnailImageView.context)
                .load(hospitalModel.imgUrl)
                .into(thumbnailImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_house_detail_viewpager, parent, false))
    }

    override fun onBindViewHolder(holder:ItemViewHolder, position: Int){
        holder.bind(currentList[position])
    }

    companion object{
        val differ = object : DiffUtil.ItemCallback<HospitalModel>(){
            override fun areItemsTheSame(oldItem: HospitalModel, newItem: HospitalModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HospitalModel,
                newItem: HospitalModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}