
/*
package com.example.dogtorapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.dogtorapplication.R
import java.text.SimpleDateFormat

class TodoAdapter : RecyclerView.Adapter<Holder>(){
    var lisData = mutableListOf<Memo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_todo, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return lisData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = lisData.get(position)
        holder.setMemo(memo)
    }

}
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setMemo(memo: Memo){
        itemView.findViewById<TextView>(R.id.todoList).text="${memo.no}"
        itemView.findViewById<TextView>(R.id.textNo).text = "${memo.content}"
        itemView.findViewById<TextView>(R.id.color).text = "${memo.color}"
    }
}
*/



//import android.R

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogtorapplication.*
import com.example.dogtorapplication.CareFragment.CustomDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.item_calendar_body.*
import kotlinx.android.synthetic.main.layout_todo.view.*
import kotlinx.android.synthetic.main.todo_add.*
import kotlinx.android.synthetic.main.todo_fix.view.*
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

var buf_view3: View? = null
var positionNum : Int? =0
var positionString :String =""
var buff_holder : TodoAdapter.TodoViewHolder? = null
class Todoupdate(){
    var num : Int? = positionNum
    var string : String? = positionString
}

class TodoAdapter(careFragment: CareFragment) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    val careFragment = careFragment
    val colorjava = yearmothday()
    var listDate = mutableListOf<Memo>()
    var viewsList2= ArrayList<View>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_todo, parent, false)
        viewsList2.add(view)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDate.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val memo = listDate.get(position)
        holder.setRec()
        holder.setMemo(memo)
        holder.rectangle.setOnClickListener {
            buff_holder?.retRec()
            holder.setRec()
            buf_view3 = viewsList2.get(position)
            positionNum = Integer.parseInt(holder.position.text.toString())
            buff_holder = holder
            holder.updateBtn.setOnClickListener {
                var id =1
                careFragment.setid(id)
            }
            holder.deleteBtn.setOnClickListener {
                var id =2
                careFragment.setid(id)
            }
        }
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var color: Int? = null
        var itemplus: Int? = null

        //데이터 베이스
        fun setMemo(memo: Memo) {
            itemplus = "${memo.ItemPlus}".toInt()
            itemView.todoList.text="${memo.Text}"
            itemView.position.text = "${memo.ItemPlus}"
            color = "${memo.Color}".toInt()
            val mGradientDrawable = itemView.rectangle.background as GradientDrawable
            mGradientDrawable.setStroke(8, colorjava.getColor(color)!!)
        }
        fun setRec(){
            val mGradientDrawable = itemView.rectangle.background as GradientDrawable
            mGradientDrawable.setColor(colorjava.getColor(color)!!)
        }
        fun retRec(){
            val mGradientDrawable = itemView.rectangle.background as GradientDrawable
            mGradientDrawable.setColor(colorjava.getColor(10)!!)
        }

        //id
        var rectangle: ImageView
        var parentTodo: View
        var todoList: TextView
        var updateBtn : ImageButton
        var deleteBtn : ImageButton
        var position : TextView

        init {

            rectangle = itemView.findViewById(R.id.rectangle)
            todoList = itemView.findViewById(R.id.todoList)
            parentTodo = itemView.findViewById(R.id.parentTodo)
            updateBtn = itemView.findViewById(R.id.updatebtn)
            deleteBtn = itemView.findViewById(R.id.deletebtn)
            position = itemView.findViewById(R.id.position)
        }
    }

}