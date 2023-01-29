package com.example.dogtorapplication

import android.graphics.Color
import android.view.View

class yearmothday {
    private var year: String? = null
    private var month: String? = null
    private var day: String? = null
    var color : Int? =null
    var Check_view : View? = null
    private var yearmonthday : String? = null
    fun getyearmonthday(): String? {
        return yearmonthday
    }

    fun setyearmonthday(day: String?) {
        this.yearmonthday = yearmonthday
    }
    fun getCheckview() : View? {
        return Check_view
    }
    fun getDay(): String? {
        return day
    }

    fun getYear(): String? {
        return year
    }

    fun getMonth(): String? {
        return month
    }

    fun setDay(day: String?) {
        this.day = day
    }

    fun setMonth(month: String?) {
        this.month = month
    }

    fun setYear(year: String?) {
        this.year = year
    }
    fun getColor(i : Int?): Int? {
        if(i==1){
            color = Color.parseColor("#FF6E6E")
        }else if(i==2){
            color = Color.parseColor("#FFA665")
        }else if(i==2){
            color = Color.parseColor("#FFA665")
        }else if(i==3){
            color = Color.parseColor("#FFDA7B")
        }else if(i==4){
            color = Color.parseColor("#82DC91")
        }else if(i==5){
            color = Color.parseColor("#5FA8D3")
        }else if(i==6){
            color = Color.parseColor("#8091E7")
        }else if(i==7){
            color = Color.parseColor("#CC8DF2")
        }else if(i==8){
            color = Color.parseColor("#FA94E3")
        }else {
            color = Color.parseColor("#ffffff")
        }
        return color
    }
}