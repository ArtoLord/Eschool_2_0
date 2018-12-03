package com.artolord.eschool20.view.marks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.artolord.controller.Controller
import com.artolord.eschool20.R
import com.artolord.eschool20.routing.Interfaces.Callback
import com.artolord.eschool20.routing.Routing_classes.Unit
import com.artolord.eschool20.view.recyclerView
import org.jetbrains.anko.*
import kotlin.collections.ArrayList

class MarksActivity : AppCompatActivity(), Callback<ArrayList<Unit>>, AdapterView.OnItemSelectedListener {


    override fun callback(callback: ArrayList<Unit>?, vararg args : Any) {
        val periodId = if (args[0] is Int) args[0] as Int else 0
        val alist : ArrayList<Unit> = callback ?: arrayListOf()
        Log.d("Logger2", "$periodId ${alist.map { it.overMark }.apply { if(size > 0) reduce { acc, s ->  acc + s}}}")
        Controller.unitByPersonMap?.set(periodId, alist)
        updateList()
    }

    override fun onError(errIndex: Int?) {}

    private var currentPeriod : Int = 0
    private lateinit var list : RecyclerView

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
            list = recyclerView {}.apply {
                layoutManager = LinearLayoutManager(this@MarksActivity, LinearLayout.VERTICAL, false)
                this.adapter = RecyclerViewAdapter(periodID = currentPeriod)
                Log.d("Logger3", this.adapter.itemCount.toString() + "g")
            }.lparams(ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }

        doAsync {
            while ((Controller.periodList?.size ?: 0) == 0 || (Controller.unitByPersonMap?.get(currentPeriod)?.size ?: 0) == 0);
            uiThread {
                Log.d("Logger3", list.adapter.itemCount.toString())

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
        Controller.periodList?.forEach {
            Log.d("Logger", "Id : ${it.periodId}")
            Controller.unitByPersonMap?.get(it.periodId)?.forEach {
                Log.d("Logger", "${it.unitName} ${it.overMark} ${it.rating}")
            }
        }
        Log.d("Logger4", "${(list.adapter as RecyclerViewAdapter).periodID}")
        list.adapter = RecyclerViewAdapter(currentPeriod)
        (list.adapter as RecyclerViewAdapter).apply {
            periodID = currentPeriod
            notifyDataSetChanged()
        }

    }
}