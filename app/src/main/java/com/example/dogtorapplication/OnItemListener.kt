package com.example.dogtorapplication

import TodoAdapter
import android.view.View

interface OnItemListener {
    fun onItemClick(dayText: String)
    fun setMonthView()
    fun setTodoView()
/*    fun Check(view: View)
    fun Final_Check()*/
    //fun Bool(bool: Boolean): Boolean
}
interface OnDialogClickListener
{
    fun onClicked(name: String)
}