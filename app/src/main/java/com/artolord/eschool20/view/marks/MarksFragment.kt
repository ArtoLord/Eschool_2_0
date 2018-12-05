package com.artolord.eschool20.view.marks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TableLayout
import com.artolord.eschool20.R
import com.artolord.eschool20.controller.Controller
import com.artolord.eschool20.routing.Routing_classes.Mark
import com.artolord.eschool20.routing.Routing_classes.Unit
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class MarksFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var currentPeriod: Int = Controller.getPeriod()
    private lateinit var list: RecyclerView
    private lateinit var table : TableLayout


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
                horizontalScrollView {
                    table = tableLayout {

                    }
                }

            }

            Controller.uploadMarks(currentPeriod, ::onMarkCallback, ::onFailed)
            updateList()
        }.view
    }

    private fun updateList() {
//        list.adapter = RecyclerViewAdapter(currentPeriod)
//        (list.adapter as RecyclerViewAdapter).apply {
//            periodID = currentPeriod
//            notifyDataSetChanged()
//        }
        table.removeAllViews()
        Controller.getMarks(currentPeriod).groupBy { it.unitId }.forEach {
            table.apply {
                tableRow {
                    textView {
                        text = Controller.unitByUnitId[it.key ?: 0]?.unitName
                    }
                    textView {
                        text = Controller.unitByUnitId[it.key ?: 0]?.overMark.toString()
                    }
                    textView {
                        text = Controller.unitByUnitId[it.key ?: 0]?.rating
                    }
                    it.value.forEach { mark ->
                        textView {
                            Log.d("Logger7", mark.markVal.toString())
                            text = mark.markVal.toString()
                        }
                    }
                }
            }
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

        Controller.uploadUnits(currentPeriod, ::onUnitCallback, ::onFailed)
    }
}