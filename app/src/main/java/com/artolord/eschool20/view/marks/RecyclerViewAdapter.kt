package com.artolord.eschool20.view.marks

import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.artolord.eschool20.controller.Controller
import com.artolord.eschool20.R
import org.jetbrains.anko.appcompat.v7.linearLayoutCompat
import org.jetbrains.anko.applyRecursively
import org.jetbrains.anko.find
import org.jetbrains.anko.sp
import org.jetbrains.anko.textView

class RecyclerViewAdapter(var periodID : Int) : RecyclerView.Adapter<RecyclerViewAdapter.UnitHolder>() {
    override fun onBindViewHolder(holder: UnitHolder, position: Int) {
        holder.apply {
            val currentSubj = Controller.unitByPersonMap?.get(periodID)?.get(position)
            subjectNameTextView.text = currentSubj?.unitName ?: ""
            subjectOverMarkTextView.text = (currentSubj?.overMark ?: 0.0).toString()
            subjectPlaceTextView.text = currentSubj?.rating ?: ""
            Log.d("Logger3", "${currentSubj?.unitName} ${(currentSubj?.overMark ?: 0.0)} ${currentSubj?.rating}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitHolder {
        Log.d("Logger3", itemCount.toString() + "l")
        return UnitHolder(parent.context.linearLayoutCompat {
            orientation = LinearLayoutCompat.VERTICAL
            textView {
                id = R.id.subject_name_text_view
            }
            linearLayoutCompat {
                orientation = LinearLayoutCompat.HORIZONTAL
                textView {
                    text = context.getString(R.string.over_mark)
                }
                textView {
                    id = R.id.subject_over_mark_text_view
                }
            }
            linearLayoutCompat {
                orientation = LinearLayoutCompat.HORIZONTAL
                textView {
                    text = context.getString(R.string.place)
                }
                textView {
                    id = R.id.subject_place_text_view
                }
            }

        }.applyRecursively {
            when (it) {
                is TextView -> {
                    it.textSize = it.sp(14).toFloat()
                }
            }
        })
    }

    override fun getItemCount() : Int {
        return Controller.unitByPersonMap?.get(periodID)?.size ?: 0
    }

    inner class UnitHolder(view : View) : RecyclerView.ViewHolder(view) {
        var subjectNameTextView = view.find<TextView>(R.id.subject_name_text_view)
        var subjectOverMarkTextView = view.find<TextView>(R.id.subject_over_mark_text_view)
        var subjectPlaceTextView = view.find<TextView>(R.id.subject_place_text_view)
    }

}