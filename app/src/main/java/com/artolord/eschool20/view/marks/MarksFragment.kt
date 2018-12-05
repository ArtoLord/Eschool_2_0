package com.artolord.eschool20.view.marks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
                scrollView {
                    horizontalScrollView {
                        table = tableLayout {}
                    }
                }

            }

            Controller.uploadMarks(currentPeriod, ::onMarkCallback, ::onFailed)
            updateList()
        }.view
    }

    private fun updateList() {
        table.removeAllViews()
        Controller.getMarks(currentPeriod).groupBy { it.unitId }.forEach {
            val unit = Controller.unitByUnitId[it.key ?: 0]
            if (unit != null) {
                table.apply {
                    tableRow {
                        textView {
                            text = unit.unitName
                        }
                        textView {
                            text = unit.overMark.toString()
                        }
                        textView {
                            text = unit.rating
                        }
                        it.value.forEach { mark ->
                            textView {
                                Log.d("Logger7", mark.markVal.toString())
                                text = mark.markVal.toString()
                            }
                        }
                    }
                }.applyRecursively { v ->
                    when (v) {
                        is TextView -> {
                            v.textSize = v.sp(8).toFloat()
                            v.minWidth = v.dip(30)
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
        updateList()
    }

    private fun onFailed() {}

    override fun onNothingSelected(p0: AdapterView<*>?) {}


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currentPeriod = Controller.periodList[p2].periodId

        Controller.uploadUnits(currentPeriod, ::onUnitCallback, ::onFailed)
    }
}