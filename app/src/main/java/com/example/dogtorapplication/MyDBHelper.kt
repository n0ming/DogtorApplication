
package com.example.dogtorapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class Memo(var ItemPlus: Int?, var Color: Int, var Text: String, var Text2 : String)
var TableName : String? = CareFragment.Main().input2
class MyDBHelper (
    context: Context?,
    name : String?
): SQLiteOpenHelper(context, name,null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DBTABLE", "$TableName")
        var sql : String = "create table memo(`Itemplus` integer primary key, Color integer, Text text, Text2 text )"
        db!!.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //테이블 변경사항이 있는 경우 불러오는 함수
    }

    fun insertMemo(memo: Memo){
        val wd = writableDatabase
        val values = ContentValues()
        values.put("ItemPlus",memo.ItemPlus)
        values.put("Color",memo.Color)
        values.put("Text",memo.Text)
        values.put("Text2",memo.Text2)
        wd.insert("memo", null,values)
        wd.close()
    }

    //데이터 조회 함수
    @SuppressLint("Range")
    fun selectMemo() : MutableList<Memo>{
        val list = mutableListOf<Memo>()

        val select = "select * from memo" // content
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        while(cursor.moveToNext()){
            val itemcolor = cursor.getInt(0)
            val text = cursor.getString(2)
            val color = cursor.getInt(1)
            val text2 = cursor.getString(3)
            val memo = Memo(itemcolor, color, text, text2)
            list.add(memo)
        }
        cursor.close()
        rd.close()
        return list
    }
    fun selectText(num : Int) : String{
        Log.d("num","$num")
        val select = "select * from memo where ItemPlus = $num"
        val rd = readableDatabase
        var text = "NAME"
        var memo : Memo = Memo(0,0,"","")
        val cursor = rd.rawQuery(select, null)
        while(cursor.moveToNext()){
            text = cursor.getString(2)

        }
        cursor.close()
        rd.close()
        return text
    }
    fun selectText2(num : Int) : String{
        Log.d("num","$num")
        val select = "select * from memo where ItemPlus = $num"
        val rd = readableDatabase
        var text = "NAME"
        var memo : Memo = Memo(0,0,"","")
        val cursor = rd.rawQuery(select, null)
        while(cursor.moveToNext()){
            text = cursor.getString(3)

        }
        cursor.close()
        rd.close()
        return text
    }
    fun selectColor(num : Int) : Int{
        Log.d("num","$num")
        val select = "select * from memo where ItemPlus = $num" // content
        val rd = readableDatabase
        var color = 0
        var memo : Memo = Memo(0,0,"","")
        val cursor = rd.rawQuery(select, null)
        while(cursor.moveToNext()){
            color = cursor.getInt(1)

        }
        cursor.close()
        rd.close()
        return color
    }

    fun updateMemo(memo: Memo,num: Int){
        val wd = writableDatabase

        val values = ContentValues()
        values.put("Color",memo.Color)
        values.put("Text",memo.Text)
        values.put("Text2",memo.Text2)

        wd.update("memo", values, "ItemPlus = ${num}",null)
        Log.d("updatetext","완료")
        wd.close()
    }
    //데이터 삭제함수
    fun deleteMemo(num: Int){
        val wd = writableDatabase
        wd.delete("memo","ItemPlus=${num}",null)
        wd.close()
    }
}
