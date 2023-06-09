package com.example.dogtorapplication

import TodoAdapter
import Todoupdate
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogtorapplication.CalendarUtil.selectedDate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_myinformation.*
import kotlinx.android.synthetic.main.fragment_community.view.*
import kotlinx.android.synthetic.main.item_calendar_body.*
import kotlinx.android.synthetic.main.item_calendar_body.view.*
import kotlinx.android.synthetic.main.layout_todo.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.todo_add.*
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates

val java = yearmothday()
var input : String? = ""
var dbHelper : MyDBHelper ?=null
var listsize : Int? =0

class CareFragment : DialogFragment(), OnItemListener {
    val fragment : Fragment = this
    var recyclerView: RecyclerView? = null
    var red : ImageButton? =null
    var orange : ImageButton? =null
    var yello : ImageButton? =null
    var green : ImageButton? =null
    var blue : ImageButton? =null
    var bluepurple : ImageButton? =null
    var purple : ImageButton? =null
    var pink : ImageButton? =null
    var recyclerView2: RecyclerView? = null
    var monthYearText: TextView? = null
    var nolist : TextView? =null

    // 파이어 베이스 인증 및 데이터 사용 위한 객체
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    var ct: Context? = null
    var updateBtn : FloatingActionButton? = null
    lateinit var todoList : TextView
    lateinit var rectangle : ImageView

    class Main() {
        var input2: String? = input
        var size : Int? = listsize
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ct = container?.getContext();
        val v2 = inflater.inflate(R.layout.item_calendar_body, container, false)
        val layout_todo = inflater.inflate(R.layout.layout_todo, container, false)
        monthYearText = v2?.findViewById<TextView>(R.id.monthYearText) as TextView
        recyclerView = v2?.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView

        todoList = layout_todo.findViewById(R.id.todoList)
        rectangle = layout_todo.findViewById(R.id.rectangle)
        red = layout_todo.findViewById(R.id.color_red)
        orange = layout_todo.findViewById(R.id.color_red)
        yello = layout_todo.findViewById(R.id.color_red)
        green = layout_todo.findViewById(R.id.color_red)
        blue = layout_todo.findViewById(R.id.color_red)
        bluepurple = layout_todo.findViewById(R.id.color_red)
        purple = layout_todo.findViewById(R.id.color_red)
        pink = layout_todo.findViewById(R.id.color_red)


        return inflater.inflate(R.layout.item_calendar_body, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nolist = view.findViewById<TextView>(R.id.nolist)
        monthYearText = view.findViewById(R.id.monthYearText)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView2 = view.findViewById<RecyclerView>(R.id.recycleView2)

        val button = view.findViewById<ImageButton>(R.id.todo_out_btn)
        var listscrollBtn = view.findViewById<ImageButton>(R.id.listscroll)
        var plusBtn = view.findViewById<ImageButton>(R.id.plus_btn)
        var prevBtn = view.findViewById<ImageButton>(R.id.prevBtn)
        var nextBtn = view.findViewById<ImageButton>(R.id.nextBtn)
        selectedDate = LocalDate.now()

        // 띄우는 창 레이아웃 연결
        setMonthView()
        Log.d("메모", "set here7")

        listscrollBtn.setOnClickListener {
            dbHelper = MyDBHelper(ct, Main().input2)
            setTodoView() }
        plusBtn.setOnClickListener {
            val dialog = CustomDialog(ct!!)
            dialog.showDialog(false,0,0) }
        nextBtn.setOnClickListener {
            selectedDate = selectedDate?.plusMonths(1)
            setMonthView() }
        prevBtn.setOnClickListener {
            selectedDate = selectedDate?.minusMonths(1)
            setMonthView() }
    }
    fun visi(){
        updateBtn?.setVisibility(View.VISIBLE)
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MM월 yyyy")
        return date!!.format(formatter)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate?): ArrayList<String> {
        val dayList = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val lastDay = yearMonth.lengthOfMonth()
        val firstDay = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstDay.dayOfWeek.value
        if ((7 - dayOfWeek) == 0) {
            for (i in 1..42) {
                if (i <= dayOfWeek || i > lastDay + dayOfWeek && (i - dayOfWeek) <= 0) { continue }
                else if (i <= dayOfWeek || i > lastDay + dayOfWeek) { dayList.add("") }
                else { dayList.add((i - dayOfWeek).toString()) }
            }
        } else {
            for (i in 1..42) {
                if (i <= dayOfWeek || i > lastDay + dayOfWeek) { dayList.add("") }
                else { dayList.add((i - dayOfWeek).toString()) }
            }
        }
        return dayList
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun setMonthView() {
        monthYearText!!.text = monthYearFromDate(selectedDate)
        val dayList = daysInMonthArray(selectedDate)
        val adapter = CalendarAdapter(dayList, this)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(ct, 7)
        recyclerView!!.layoutManager = manager
        recyclerView!!.adapter = adapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setid(id : Int){
        if(id==1) {
            val dialog = CustomDialog(ct!!)
            dialog.showDialog(true, Todoupdate().num!! , 1)
        } else if(id==2){
            dbHelper = MyDBHelper(ct, Main().input2)
            dbHelper!!.deleteMemo(Todoupdate().num!! )
            var adapter = TodoAdapter(this@CareFragment)
            var memos = dbHelper!!.selectMemo()
            adapter.listDate.addAll(memos)
            recycleView2.adapter = adapter
            recycleView2.layoutManager = GridLayoutManager(ct,1)
            adapter.listDate.clear()
            adapter.listDate.addAll(dbHelper!!.selectMemo())
            adapter.notifyDataSetChanged()
        } else if(id==3){
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun setTodoView() {
        var adapter = TodoAdapter(this)
        var memos = dbHelper!!.selectMemo()
        adapter.listDate.addAll(memos)
        listsize= adapter.listDate.size
        recycleView2.adapter = adapter
        recycleView2.layoutManager = GridLayoutManager(ct,1)
        if(listsize==0){ nolist!!.setVisibility(View.VISIBLE) }
        else{ nolist!!.setVisibility(View.GONE) }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun yearMonthFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date!!.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(dayText: String) {
        var yearMonday = yearMonthFromDate(selectedDate) + "" + dayText + "일"
        input = yearMonday

        if(Main().size!=0){ Toast.makeText(ct, "Todo list 버튼을 통해 할일을 확인해보세요", Toast.LENGTH_SHORT).show() }
        else { Toast.makeText(ct, "Todo list 버튼을 통해 할일을 확인해보세요", Toast.LENGTH_SHORT).show() }
    }

    inner class CustomDialog(context: Context) {
        private val dialog = Dialog(context)
        private lateinit var onClickListener: OnDialogClickListener

        fun setOnClickListener(listener: OnDialogClickListener) {
            onClickListener = listener
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun showDialog(run : Boolean, num : Int, num2 : Int) {
            var color: Int = 1
            var text = CareFragment.Main().input2

            if (text != null) {
                java.setyearmonthday(text)
                java.setYear(text.substring(0 until 4))
                java.setMonth(text.substring(6 until 8))
                java.setDay(text.substring(9 until 11))
            }
            if (text != null) { java.setMonth(text.substring(6 until 8)) }
            if(num2==0){ dialog.setContentView(R.layout.todo_add) }
            if(num2==1){
                dialog.setContentView(R.layout.todo_fix)
                if(run){
                    var a : String= dbHelper!!.selectText(num)
                    var b : String= dbHelper!!.selectText2(num)
                    var c : Int = dbHelper!!.selectColor(num)
                    colorfixdialogret(c)
                    dialog.editMemo.setText(a)
                    dialog.editMemo2.setText(b)
                }
            }
            if(num2==2){ dbHelper!!.deleteMemo(num) }

            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

            // Custom Dialog 위치 조절, Custom Dialog 배경 설정 (다음과 같이 진행해야 좌우 여백 없이 그려짐), 클릭 날짜로 텍스트 변경
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.click_day.text = java.getYear() + "." + java.getMonth() + "." + java.getDay()
            dialog.show()

            dialog.todo_out_btn.setOnClickListener { dialog.dismiss() }

            dialog.todo_add_btn.setOnClickListener {
                var adapter = TodoAdapter(this@CareFragment)
                var memos = dbHelper!!.selectMemo()

                val content = dialog.editMemo.text.toString()
                val content2 = dialog.editMemo2.text.toString()

                dbHelper = MyDBHelper(ct, Main().input2)

                if (content.isNotEmpty()) {
                    val memo = Memo(null, color, content,content2)
                    if(run){ dbHelper!!.updateMemo(memo,num) } else { dbHelper!!.insertMemo(memo) }
                }

                adapter.listDate.addAll(memos)
                recycleView2.adapter = adapter
                recycleView2.layoutManager = GridLayoutManager(ct,1)

                adapter.listDate.clear()
                adapter.listDate.addAll(dbHelper!!.selectMemo())
                adapter.notifyDataSetChanged()

                nolist!!.setVisibility(View.GONE)

                dialog.dismiss()
            }
            dialog.color_red.setOnClickListener {
                dialogret()
                color = 1
                dialog.color_red.setBackgroundResource(R.drawable.group_57)
            }
            dialog.color_orange.setOnClickListener {
                dialogret()
                color = 2
                dialog.color_orange.setBackgroundResource(R.drawable.group_58)
            }
            dialog.color_blue.setOnClickListener {
                dialogret()
                color = 5
                dialog.color_blue.setBackgroundResource(R.drawable.group_61)
            }
            dialog.color_blue_purple.setOnClickListener {
                dialogret()
                color = 6
                dialog.color_blue_purple.setBackgroundResource(R.drawable.group_62)
            }
            dialog.color_green.setOnClickListener {
                dialogret()
                color = 4
                dialog.color_green.setBackgroundResource(R.drawable.group_60)
            }
            dialog.color_pink.setOnClickListener {
                dialogret()
                color = 8
                dialog.color_pink.setBackgroundResource(R.drawable.group_56)
            }
            dialog.color_purple.setOnClickListener {
                dialogret()
                color = 7
                dialog.color_purple.setBackgroundResource(R.drawable.group_63)
            }
            dialog.color_yello.setOnClickListener {
                dialogret()
                color = 3
                dialog.color_yello.setBackgroundResource(R.drawable.group_59)
            }
            dialog.tag_no.setOnClickListener{
                tagdialogret()
                dialog.tag_no.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_no.setTextColor(Color.parseColor("#FFFFFF"))
            }
            dialog.tag_medicine.setOnClickListener{
                tagdialogret()
                dialog.tag_medicine.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_medicine.setTextColor(Color.parseColor("#FFFFFF"))
            }
            dialog.tag_cut.setOnClickListener {
                tagdialogret()
                dialog.tag_cut.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_cut.setTextColor(Color.parseColor("#FFFFFF"))
            }
            dialog.tag_walk.setOnClickListener{
                tagdialogret()
                dialog.tag_walk.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_walk.setTextColor(Color.parseColor("#FFFFFF"))
            }

        }
        fun dialogret(){
            dialog.color_red.setBackgroundResource(R.drawable.red_circle)
            dialog.color_orange.setBackgroundResource(R.drawable.orange)
            dialog.color_yello.setBackgroundResource(R.drawable.yello)
            dialog.color_green.setBackgroundResource(R.drawable.green)
            dialog.color_blue.setBackgroundResource(R.drawable.blue)
            dialog.color_blue_purple.setBackgroundResource(R.drawable.bluepurple)
            dialog.color_purple.setBackgroundResource(R.drawable.purple)
            dialog.color_pink.setBackgroundResource(R.drawable.pink)
        }
        fun tagdialogret(){
            dialog.tag_no.setBackgroundResource(R.drawable.rounded_corner_white)
            dialog.tag_no.setTextColor(Color.parseColor("#5FA8D3"))
            dialog.tag_medicine.setBackgroundResource(R.drawable.rounded_corner_white)
            dialog.tag_medicine.setTextColor(Color.parseColor("#5FA8D3"))
            dialog.tag_cut.setBackgroundResource(R.drawable.rounded_corner_white)
            dialog.tag_cut.setTextColor(Color.parseColor("#5FA8D3"))
            dialog.tag_walk.setBackgroundResource(R.drawable.rounded_corner_white)
            dialog.tag_walk.setTextColor(Color.parseColor("#5FA8D3"))
        }
        fun tagfixdialogret(num : Int){
            if(num==1){
                dialog.tag_no.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_no.setTextColor(Color.parseColor("#FFFFFF"))
            } else if(num==2){
                dialog.tag_medicine.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_medicine.setTextColor(Color.parseColor("#FFFFFF"))
            }else if(num==3){
                dialog.tag_cut.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_cut.setTextColor(Color.parseColor("#FFFFFF"))
            }else if(num==4){
                dialog.tag_walk.setBackgroundResource(R.drawable.rounded_corner)
                dialog.tag_walk.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }
        fun colorfixdialogret(num : Int){
            if(num==1){
                dialog.color_red.setBackgroundResource(R.drawable.group_57)
            } else if(num==3){
                dialog.color_yello.setBackgroundResource(R.drawable.group_59)
            }else if(num==2){
                dialog.color_orange.setBackgroundResource(R.drawable.group_58)
            }else if(num==4){
                dialog.color_green.setBackgroundResource(R.drawable.group_60)
            }else if(num==5){
                dialog.color_blue.setBackgroundResource(R.drawable.group_61)
            }else if(num==6){
                dialog.color_blue_purple.setBackgroundResource(R.drawable.group_62)
            }else if(num==7){
                dialog.color_purple.setBackgroundResource(R.drawable.group_63)
            }else if(num==8){
                dialog.color_pink.setBackgroundResource(R.drawable.group_56)
            }
        }
    }


}