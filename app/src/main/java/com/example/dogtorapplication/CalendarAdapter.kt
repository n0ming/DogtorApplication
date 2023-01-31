
package com.example.dogtorapplication


import TodoAdapter
import android.graphics.Color
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
import kotlinx.android.synthetic.main.item_calendar_body.*
import java.util.*
import kotlin.collections.ArrayList


var buf_view: View? = null
var buf_view1 : View? = null
class Calendar(){
    var buf_view2: View? = buf_view
    var buf_view22 : View? = buf_view1
    var bb : Boolean = false
    fun change(){
        bb = true
    }
}

class CalendarAdapter( var dayList: ArrayList<String>, private val onItemListener: OnItemListener) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    var check = 0
    var text: String? = null

    var buff2 = 0
    var a = 0
    var dot : Boolean = true
    var viewsList= ArrayList<View>()
    fun a(): View? {
        return buf_view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        viewsList.add(view)
        CareFragment().setid(3)
        //buf = view
        return CalendarViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = dayList[position]
        val yearmothday = yearmothday()
        yearmothday.setDay(day)
        buf_view1 = viewsList.get(position)
        //토일 색상 변경
        holder.dayText.text = day
        if (position == 11) {
        }
        if ((position + 1) % 7 == 0) {
            holder.dayText.setTextColor(Color.parseColor("#FF6E6E"))
        } else if (position == 0 || position % 7 == 0) {
            holder.dayText.setTextColor(Color.parseColor("#FF6E6E"))
        }
        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener {
            onItemListener.onItemClick(day)
            //색깔 변경 추가
            if (check == 0) {
                holder.parentView.setBackgroundResource(R.drawable.edge_blue)
                check = 1
                buff2 = position;
                buf_view = viewsList.get(buff2)
            } else if (check == 1) {
                viewsList[buff2].setBackgroundResource(R.drawable.edge_white)
                holder.parentView.setBackgroundResource(R.drawable.edge_blue)
                check = 1
                buff2 = position;
                buf_view = viewsList.get(buff2)
            }
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }
    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dayText: TextView
        var parentView: View
        init {
            dayText = itemView.findViewById(R.id.dayText)
            parentView = itemView.findViewById(R.id.parentView)
        }
    }
    inner class Todo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var recy2 : RecyclerView
        init {
            recy2 = itemView.findViewById(R.id.recycleView2)
        }
    }
}
