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

class MarksFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var currentPeriod: Int = 0
    private lateinit var list: RecyclerView
    private lateinit var listAdapter: ArrayAdapter<String>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            Log.d("Logger6", "Create")
            currentPeriod = Controller.periodList?.get(0)?.periodId ?: 0
            Controller.route?.getMarks(Controller.state?.userId
                    ?: 0, Controller.periodList?.get(0)?.periodId ?: 0, UnitListCallback())

            verticalLayout {
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    textView(R.string.name)
                    textView(": ${Controller.state?.prsFio ?: getString(R.string.error)}")
                }
                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, Controller.periodList?.filter { it.isStudy }?.map { it.periodName }
                        ?: arrayListOf())
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

            doAsync {
                while ((Controller.periodList?.size
                                ?: 0) == 0 || (Controller.unitByPersonMap?.get(currentPeriod)?.size
                                ?: 0) == 0);
                uiThread {
                    Controller.route?.getMarksWithWights(Controller.state?.userId
                            ?: 0, currentPeriod, MarkListCallback())
                    updateList()
                }
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

    inner class MarkListCallback : Callback<ArrayList<Mark>> {
        override fun onError(errIndex: Int?) {}

        override fun callback(callback: ArrayList<Mark>?, vararg args: Any?) {
            val periodId: Int = (args[0] as Int?) ?: 0
            Controller.marksList?.set(periodId, callback ?: arrayListOf())
        }

    }

    inner class UnitListCallback : Callback<ArrayList<Unit>> {
        override fun callback(callback: ArrayList<Unit>?, vararg args : Any) {
            val periodId = if (args[0] is Int) args[0] as Int else 0
            val aList : ArrayList<com.artolord.eschool20.routing.Routing_classes.Unit> = callback ?: arrayListOf()
            Controller.unitByPersonMap?.set(periodId, aList)
            updateList()
        }

        override fun onError(errIndex: Int?) {}
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currentPeriod = Controller.periodList?.get(p2)?.periodId ?: 0
        Log.d("Logger", " $currentPeriod")

        println("$p2 $currentPeriod")
        if (!Controller.unitByPersonMap!!.containsKey(currentPeriod))
            Controller.route?.getMarks(Controller.state?.userId ?: 0, currentPeriod, UnitListCallback())
        doAsync {
            while ((Controller.periodList?.size ?: 0) == 0 || (Controller.unitByPersonMap?.get(currentPeriod)?.size ?: 0) == 0);
            uiThread {
                updateList()
            }
        }
    }


}