package com.artolord.eschool20.view.marks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.artolord.eschool20.R
import com.artolord.eschool20.controller.Controller
import com.artolord.eschool20.routing.Interfaces.Callback
import com.artolord.eschool20.routing.Routing_classes.Mark
import com.artolord.eschool20.routing.Routing_classes.Unit
import com.artolord.eschool20.view.recyclerView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onUiThread
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.uiThread

class MarksFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var currentPeriod: Int = Controller.getPeriod()
    private lateinit var list: RecyclerView
    private lateinit var listAdapter: ArrayAdapter<String>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {

            Controller.uploadUnits(currentPeriod, ::onUnitCallback, ::onFailed)

            verticalLayout {
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    textView(R.string.name)
                    textView(": ${Controller.state.prsFio ?: getString(R.string.error)}")
                }
                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, Controller.periodList.filter { it.isStudy }.map { it.periodName })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner {
                    this.adapter = adapter
                    onItemSelectedListener = this@MarksFragment
                }
                list = recyclerView {}.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                    this.adapter = RecyclerViewAdapter(periodID = currentPeriod)
                }.lparams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }

            runOnUiThread {
                    Controller.uploadMarks(currentPeriod, ::onMarkCallback, ::onFailed)
                    updateList()
                }
        }.view
    }

    private fun updateList() {
        list.adapter = RecyclerViewAdapter(currentPeriod)
        (list.adapter as RecyclerViewAdapter).apply {
            periodID = currentPeriod
            notifyDataSetChanged()
        }
    }

    private fun onUnitCallback(callback: ArrayList<Unit>) {
        val aList : ArrayList<com.artolord.eschool20.routing.Routing_classes.Unit> = callback
        Controller.unitByPersonMap[currentPeriod] = aList
        updateList()
    }

    private fun onMarkCallback(callback: ArrayList<Mark>) {
        Controller.marksList[currentPeriod] = callback
    }

    private fun onFailed() {}

    override fun onNothingSelected(p0: AdapterView<*>?) {}


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currentPeriod = Controller.periodList[p2].periodId

        if (!Controller.unitByPersonMap.containsKey(currentPeriod))
            Controller.uploadUnits(currentPeriod, ::onUnitCallback, ::onFailed)
    }
}