
package com.example.dogtorapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class CheckMemo(var ItemPlus: Int?, var Color: Int)
var TableName2 : String? = CareFragment.Main().input2 + "DOT"
class DotDBHelper (
    context: Context?,
    name : String?
): SQLiteOpenHelper(context, name,null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DBTABLE", "$TableName2")
        var sql : String = "create table checkmemo(`Itemplus` integer primary key, Color integer)"
        Log.d("DBTABLE", "$TableName2")
        db!!.execSQL(sql)
        Log.d("DBTABLE", "create")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //테이블 변경사항이 있는 경우 불러오는 함수
    }

    fun insertMemo(memo: CheckMemo){
        // db 가져오기
        val wd = writableDatabase
        //wd.execSQL("INSERT INTO memo VALUES ('" + memo.ItemPlus.toString()+"',"+memo.Color.toString()+","+memo.Text.toString()+");")
        //Memo를 입력타입으로 변환
        val values = ContentValues()
        values.put("ItemPlus",memo.ItemPlus)
        values.put("Color",memo.Color)

        //db에 넣기
        wd.insert("checkmemo", null,values)
        //db닫기
        wd.close()
    }
    //데이터 조회 함수
    @SuppressLint("Range")
    fun selectMemo() : MutableList<CheckMemo>{
        val list = mutableListOf<CheckMemo>()

        val select = "select * from checkmemo" // content
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null) // 위 sql과 다르게 반환값 존재
        while(cursor.moveToNext()){
            val itemcolor = cursor.getInt(0)
            val color = cursor.getInt(1)
            val memo = CheckMemo(itemcolor, color)
            list.add(memo)
        }
        cursor.close()
        rd.close()
        return list
    }
    //아직 check 추가 안함
    //데이터 수정 함수
    fun updateMemo(memo: CheckMemo){
        val wd = writableDatabase

        val values = ContentValues()
        values.put("ItemPlus",memo.ItemPlus)
        values.put("Color",memo.Color)

        wd.update("memo", values, "no = ${memo.ItemPlus}",null)
        wd.close()
    }
    //데이터 삭제함수
    fun deleteMemo(memo: CheckMemo){
        val wd = writableDatabase
        //val delete = "delete from memo where no = ${memo.no}"
        //wd.execSQL(delete)

        wd.delete("memo","no=${memo.ItemPlus}",null)
        wd.close()
    }
}
