package com.artolord.eschool20.view.marks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.artolord.eschool20.R
import org.jetbrains.anko.linearLayout

class MarksActivity : AppCompatActivity() {
    private val marksFragment = MarksFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayout {
            orientation = LinearLayout.VERTICAL
            id = R.id.marks_fragment
            supportFragmentManager.beginTransaction().replace(R.id.marks_fragment, marksFragment).commit()

        }
    }

    override fun onStart() {
        super.onStart()
        //supportFragmentManager.beginTransaction().replace(R.id.marks_fragment, marksFragment).commit()
    }
}