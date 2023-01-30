package com.example.dogtorapplication

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView



import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.internal.ViewUtils.dpToPx

class HospitalListAdapter : androidx.recyclerview.widget.ListAdapter<HospitalModel, HospitalListAdapter.ItemViewHolder>(differ) {
    inner class ItemViewHolder(val view:View) : RecyclerView.ViewHolder(view){

        fun bind(hospitalModel: HospitalModel){
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
            val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
            val tellTextView = view.findViewById<TextView>(R.id.TellTextView)
            val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)

            titleTextView.text = hospitalModel.title
            priceTextView.text = hospitalModel.location
            tellTextView.text = hospitalModel.tell

            Glide
                .with(thumbnailImageView.context)
                .load(hospitalModel.imgUrl)
                // transeform()을 통해 이미지를 변형시키고, RoundCorners를 통해 모서리를 둥글게 바꿔줌
                .transform(CenterCrop(), RoundedCorners(dpToPx(thumbnailImageView.context, 12)))
                .into(thumbnailImageView)
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_house, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<HospitalModel>() {
            override fun areItemsTheSame(oldItem: HospitalModel, newItem: HospitalModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HospitalModel, newItem: HospitalModel): Boolean {
                return oldItem == newItem
            }
        }

    }
}