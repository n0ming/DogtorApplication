package com.example.dogtorapplication

import TodoAdapter
import Todoupdate
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_community.view.*
import kotlinx.android.synthetic.main.item_calendar_body.*
import kotlinx.android.synthetic.main.layout_todo.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.todo_add.*
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates

var gotomain : Boolean = false
val java = yearmothday()
var input : String? = ""
var dbHelper : MyDBHelper ?=null
var listsize : Int? =0
class CareFragment : DialogFragment(), OnItemListener {
    var aa : Boolean = false
    val java1 = yearmothday()
    val fragment : Fragment = this
    val fragmentM : FragmentManager? = getFragmentManager()
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

    //var selectedDate : LocalDate? =null
    //val mActivity = activity as MainActivity
    var ct: Context? = null

    //db
    //lateinit var dbHelper: MyDBHelper
    lateinit var database: SQLiteDatabase
    //private lateinit var binding: CareFragment

    //db로 삽입해야하는 것들
    lateinit var todoList : TextView
    lateinit var rectangle : ImageView

    //db로 저장해야 하는 것들
    class Main() {
        var input2: String? = input
        var size : Int? = listsize
    }
    //db
/*    lateinit var adapter: TodoAdapter
    lateinit var myHelper : MyDBHelper*/
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ct = container?.getContext();
        val v1: View = inflater.inflate(R.layout.todo_add, container, false)
        val button = v1.findViewById<ImageButton>(R.id.todo_out_btn) as ImageButton
        val v2: View = inflater.inflate(R.layout.item_calendar_body, container, false)
        val item_calendar_body = inflater.inflate(R.layout.item_calendar_body, container, false)
        val layout_todo = inflater.inflate(R.layout.layout_todo, container, false)

        //val aaa = inflater.inflate(R.layout.test,container,false)
        monthYearText = v2.findViewById<TextView>(R.id.monthYearText) as TextView
        recyclerView = v2.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView

        //recyclerView2 = v2.findViewById<RecyclerView>(R.id.recycleView2)as RecyclerView
        //db 할때 사용할 것들들
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
/*
        myHelper = MyDBHelper(ct!!,"groupDo",1)
        adapter = TodoAdapter()

        val recycleView2 : RecyclerView = v2.findViewById<RecyclerView>(R.id.recycleView2)as RecyclerView
        val dbHelper = MyDBHelper(ct, "mydb.db", 1)
        val adapter: TodoAdapter = TodoAdapter()

        val memos = dbHelper.selectMemo()
        adapter.lisData.addAll(memos)
        recycleView2.adapter = adapter
        recycleView2.layoutManager = LinearLayoutManager(this)

        val buttonSave : Button = test.findViewById(R.id.buttonSave)
        val editMemo : EditText = findViewById(R.id.editMemo)
        buttonSave.setOnClickListener{
            val content = editMemo.text.toString()
            Log.d("메모","content = $content")
            if(content.isNotEmpty()){
                val memo = Memo(null,1,content,System.currentTimeMillis())
                dbHelper.insertMemo(memo)
                Log.d("메모","content is not empty")
            }
        }*/
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
        var updateBtn = view.findViewById<ImageButton>(R.id.update_btn)
        selectedDate = LocalDate.now()
        // 띄우는 창 레이아웃 연결
        setMonthView()

        listscrollBtn.setOnClickListener {
            dbHelper = MyDBHelper(ct, Main().input2)
            setTodoView()
        }

        plusBtn.setOnClickListener {
            val dialog = CustomDialog(ct!!)
            // Custom Dialog 표시
            dialog.showDialog(false,0)
        }
        updateBtn.setOnClickListener {
            val dialog = CustomDialog(ct!!)
            // Custom Dialog 표시
            dialog.showDialog(true, Todoupdate().num!!+1)
        }

        //클릭해도 onclick 안되고 여기서 되면 dialog 바로 등장 시키고 수정뒤 ...
        Todoupdate().buf_view4?.findViewById<ImageButton>(R.id.updatebtn)?.setOnClickListener {
            Log.d("aa","clickㅁㅁㅁㅁㅁㅁㅁㅁㅁ")
            val dialog = CustomDialog(ct!!)
            // Custom Dialog 표시
            dialog.showDialog(false,0)
        }

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
                if (i <= dayOfWeek || i > lastDay + dayOfWeek && (i - dayOfWeek) <= 0) {
                    continue
                } else if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                    dayList.add("")
                } else {
                    dayList.add((i - dayOfWeek).toString())
                }
            }
        } else {
            for (i in 1..42) {
                if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                    dayList.add("")
                } else {
                    dayList.add((i - dayOfWeek).toString())
                }
            }
        }
        return dayList
    }

    //getActivity()?.getApplicationContext()
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun setMonthView() {
        monthYearText!!.text = monthYearFromDate(selectedDate)
        val dayList = daysInMonthArray(selectedDate)
        val adapter = CalendarAdapter(dayList, this)
        val manager: RecyclerView.LayoutManager =
            GridLayoutManager(ct, 7)//GridLayoutManager(applicationContext, 7)
        recyclerView!!.layoutManager = manager
        recyclerView!!.adapter = adapter
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun setTodoView() {
        var adapter = TodoAdapter()
        var memos = dbHelper!!.selectMemo()
        Log.d("메모", "set here")
        adapter.listDate.addAll(memos)
        listsize= adapter.listDate.size
        recycleView2.adapter = adapter
        Log.d("메모", "set here3")
        recycleView2.layoutManager = GridLayoutManager(ct,1)
        if(listsize==0){
            nolist!!.setVisibility(View.VISIBLE)
        }
        else{
            nolist!!.setVisibility(View.GONE)

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun yearMonthFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date!!.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(dayText: String) {
        var yearMonday = yearMonthFromDate(selectedDate) + "" + dayText + "일"

        //날짜 저장
        input = yearMonday
        //이벤트
        if(Main().size!=0){
        Toast.makeText(ct, "Todo list 버튼을 통해 할일을 확인해보세요", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(ct, "Todo list 버튼을 통해 할일을 확인해보세요", Toast.LENGTH_SHORT).show()
        }
    }

    inner class CustomDialog(context: Context) {
        private lateinit var onItemListener: OnItemListener
        private lateinit var binding: CustomDialog
        private val dialog = Dialog(context)
        private lateinit var onClickListener: OnDialogClickListener


        /*fun onCreate() {
            setContentView(R.layout.item_calendar_body)
            val todo_body_layout = findViewById<LinearLayout>(R.id.todo_body_layout)
            todo_body_layout.addView(createimage())
        }*/

        fun setOnClickListener(listener: OnDialogClickListener) {
            onClickListener = listener
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun showDialog(run : Boolean, num : Int) {
            //color 저장
            var color: Int = 1
            //클릭된 날짜로 변경
            var text = CareFragment.Main().input2
            if (text != null) {
                java.setyearmonthday(text)
                java.setYear(text.substring(0 until 4))
                java.setMonth(text.substring(6 until 8))
                java.setDay(text.substring(9 until 11))
            }
            if (text != null) {
                java.setMonth(text.substring(6 until 8))
            }

            dialog.setContentView(R.layout.todo_add)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Custom Dialog 위치 조절
            dialog.window?.setGravity(Gravity.BOTTOM)
            // Custom Dialog 배경 설정 (다음과 같이 진행해야 좌우 여백 없이 그려짐)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            text = java.getYear() + "." + java.getMonth() + "." + java.getDay()
            //클릭 날짜로 텍스트 변경
            dialog.click_day.text = java.getYear() + "." + java.getMonth() + "." + java.getDay()
            if(run){
                var a : String= dbHelper!!.selectText(num)
                Log.d("text","$a")
                dialog.editMemo.setText(a)
            }
            dialog.show()

            dialog.todo_out_btn.setOnClickListener {
                //창 닫기
                dialog.dismiss()
                //onCreate()
            }

            dialog.todo_add_btn.setOnClickListener {
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)?.setVisibility(View.VISIBLE)
                /* val recycleView2: RecyclerView? =
                    item_calendar_body?.findViewById(R.id.recycleView2)
                val dbHelper = MyDBHelper(ct, "mydb.db", 1)
                val adapter: TodoAdapter = TodoAdapter()

                val memos = dbHelper.selectMemo()
                adapter.lisData.addAll(memos)
                recycleView2?.adapter = adapter
                recycleView2?.layoutManager = LinearLayoutManager(ct)

                val editMemo: EditText? = layout_todo?.findViewById(R.id.editMemo)
                */
                Log.d("DBTABLE", "${Main().input2}")
                dbHelper = MyDBHelper(ct, Main().input2)
                val content = dialog.editMemo.text.toString()
                Log.d("메모", "content!!!!!!! = $content")
                if (content.isNotEmpty()) {
                    val memo = Memo(null, color, content)
                    Log.d("메모", "memo is inserted")
                    Log.d("메모", "helper is not empty, content!!!!!!! = $dbHelper")
                    dbHelper!!.insertMemo(memo)
                    Log.d("메모", "content is not empty")
                }
                var adapter = TodoAdapter()
                var memos = dbHelper!!.selectMemo()
                Log.d("메모", "set here")
                adapter.listDate.addAll(memos)
                Log.d("메모", "set here2")
                recycleView2.adapter = adapter
                Log.d("메모", "set here3")
                recycleView2.layoutManager = GridLayoutManager(ct,1)
                //Log.d("메모", "content here4")

                nolist!!.setVisibility(View.GONE)

                dialog.dismiss()
            }
            dialog.color_red.setOnClickListener {
                dialogret()
                color = 1
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round)
                dialog.color_red.setBackgroundResource(R.drawable.group_57)
            }
            dialog.color_orange.setOnClickListener {
                dialogret()
                color = 2
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round_orange)
                dialog.color_orange.setBackgroundResource(R.drawable.group_58)
            }
            dialog.color_blue.setOnClickListener {
                dialogret()
                color = 5
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round_blue)
                dialog.color_blue.setBackgroundResource(R.drawable.group_61)
            }
            dialog.color_blue_purple.setOnClickListener {
                dialogret()
                color = 6
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round_bluepurple)
                dialog.color_blue_purple.setBackgroundResource(R.drawable.group_62)
            }
            dialog.color_green.setOnClickListener {
                dialogret()
                color = 4
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round_green)
                dialog.color_green.setBackgroundResource(R.drawable.group_60)
            }
            dialog.color_pink.setOnClickListener {
                dialogret()
                color = 8
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round_pink)
                dialog.color_pink.setBackgroundResource(R.drawable.group_56)
            }
            dialog.color_purple.setOnClickListener {
                dialogret()
                color = 7
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round_purple)
                dialog.color_purple.setBackgroundResource(R.drawable.group_63)
            }
            dialog.color_yello.setOnClickListener {
                dialogret()
                color = 3
                Calendar().buf_view2?.findViewById<ImageView>(R.id.todo1)
                    ?.setBackgroundResource(R.drawable.check_round_yello)
                dialog.color_yello.setBackgroundResource(R.drawable.group_59)
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

    }
}
