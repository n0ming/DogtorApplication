
package com.example.dogtorapplication


import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.check_round.view.*
import kotlinx.android.synthetic.main.item_calendar_body.*
import kotlinx.android.synthetic.main.layout_todo.view.*
import java.util.*

var buf_view4: View? = null
class Dot(){
    var buf_view5: View? = buf_view4
    var bb : Boolean = false
    fun change(){
        bb = true
    }
}

class DotAdapter(var dayList: ArrayList<String>) :
    RecyclerView.Adapter<DotAdapter.DotViewHolder>() {
    val colorjava = yearmothday()

    var check = 0
    var text: String? = null
    var viewsList= ArrayList<View>()

    var buff2 = 0
    var a = 0
    var dot : Boolean = true

    fun a(): View? {
            return buf_view4
        }

    //필요
    var listdot = mutableListOf<CheckMemo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DotViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.check_round, parent, false)
        viewsList.add(view)
        return DotViewHolder(view)
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DotViewHolder, position: Int) {
        val checkmemo = listdot.get(position)
        holder.setCheckMemo(checkmemo)
    }

    //viewsList를 공유하던가 추가 버튼후에 아래 명령어가 실행되게 신호를 주는 방법을 알아봐야함!!!!!!!!



    override fun getItemCount(): Int {
        return listdot.size
    }

    inner class DotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tododot : ImageView
        var color : Int? = null
        var itemplus: Int? = null

        fun setCheckMemo(memo: CheckMemo){
            color = "${memo.Color}".toInt()
            itemplus =  "${memo.ItemPlus}".toInt()
            itemView.todoDot.visibility=View.VISIBLE
            Log.d("dot","${itemView.todoDot.visibility}")
            val mGradientDrawable = itemView.todoDot.background as GradientDrawable
            mGradientDrawable.setColor(colorjava.getColor(color)!!) //Color
        }
        init {
            tododot = itemView.findViewById(R.id.todoDot)
        }
    }


}

