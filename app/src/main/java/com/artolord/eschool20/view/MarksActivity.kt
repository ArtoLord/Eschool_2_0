package com.artolord.eschool20.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.artolord.controller.Controller
import com.artolord.eschool20.R
import com.artolord.eschool20.routing.Interfaces.Callback
import com.artolord.eschool20.routing.Routing_classes.Mark
import com.artolord.eschool20.routing.Routing_classes.Unit
import org.jetbrains.anko.*
import java.util.*
import kotlin.collections.ArrayList

class MarksActivity : AppCompatActivity(), Callback<ArrayList<Unit>>, AdapterView.OnItemSelectedListener {


    override fun callback(callback: ArrayList<Unit>?, vararg args : Any) {
        val periodId = if (args[0] is Int) args[0] as Int else 0
        val alist : ArrayList<Unit> = callback ?: arrayListOf()
        Log.d("Logger2", "$periodId ${alist.map { it.overMark }.apply { if(size > 0) reduce { acc, s ->  acc + s}}}")
        Controller.unitByPersonMap?.set(periodId, alist)
    }

    override fun onError(errIndex: Int?) {}

    private var currentPeriod : Int = 0
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var list : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPeriod = Controller.periodList?.get(0)?.periodId ?: 0
        Controller.route?.getMarks(Controller.state?.userId ?: 0, Controller.periodList?.get(0)?.periodId ?: 0, this)

        verticalLayout {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                textView(R.string.name)
                textView(": ${Controller.state?.prsFio ?: getString(R.string.error)}")
            }
            val adapter = ArrayAdapter(this@MarksActivity, android.R.layout.simple_spinner_item, Controller.periodList?.filter { it.isStudy }?.map { it.periodName } ?: arrayListOf())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner {
                this.adapter = adapter
                onItemSelectedListener = this@MarksActivity
            }
            listAdapter = ArrayAdapter(this@MarksActivity, android.R.layout.simple_spinner_item, Controller.unitByPersonMap?.get(currentPeriod)?.map { "${it.unitName} ${it.totalmark} ${it.overMark}" } ?: arrayListOf())
            listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            list = listView {
                this.adapter = listAdapter
            }
        }

        doAsync {
            while ((Controller.periodList?.size ?: 0) == 0 || (Controller.unitByPersonMap?.get(currentPeriod)?.size ?: 0) == 0);
            uiThread {
                updateList()
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currentPeriod = Controller.periodList?.get(p2)?.periodId ?: 0
        Log.d("Logger", " $currentPeriod")

        println("$p2 $currentPeriod")
        if (!Controller.unitByPersonMap!!.containsKey(currentPeriod))
            Controller.route?.getMarks(Controller.state?.userId ?: 0, currentPeriod, this@MarksActivity)
        doAsync {
            while ((Controller.periodList?.size ?: 0) == 0 || (Controller.unitByPersonMap?.get(currentPeriod)?.size ?: 0) == 0);
            uiThread {
                updateList()
            }
        }
    }

    private fun updateList()
    {
        listAdapter = ArrayAdapter(this@MarksActivity, android.R.layout.simple_spinner_item, Controller.unitByPersonMap?.get(currentPeriod)?.map { "${it.unitName} ${it.totalmark} ${it.overMark}" } ?: arrayListOf())
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Log.d("Logger", "${currentPeriod} Update ${Controller.unitByPersonMap?.get(currentPeriod)?.size}")
        Controller.periodList?.forEach {
            Log.d("Logger", "Id : ${it.periodId}")
            Controller.unitByPersonMap?.get(it.periodId)?.forEach {
                Log.d("Logger", "${it.unitName} ${it.overMark} ${it.rating}")
            }
        }
        list.adapter = listAdapter
    }
}