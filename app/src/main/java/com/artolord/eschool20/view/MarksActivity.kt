package com.artolord.eschool20.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.artolord.controller.Controller
import com.artolord.eschool20.R
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class MarksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                textView(R.string.name)
                textView(": ${Controller.state.prsFio}")
            }
        }
    }
}
