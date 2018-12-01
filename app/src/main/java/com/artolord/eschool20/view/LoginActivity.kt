package com.artolord.eschool20.view

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.artolord.eschool20.R
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginTextView : EditText
    private lateinit var passwordTextView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            gravity = Gravity.CENTER
            loginTextView = editText {
                hint = getString(R.string.login)

            }
            passwordTextView = editText {
                hint = getString(R.string.password)
            }
            button {
                text = getString(R.string.sign_in)
                setOnClickListener {
                    startActivity<MarksActivity>()
                }
            }
        }.applyRecursively {
            when(it)
            {
                is EditText -> {
                    (it.layoutParams as LinearLayout.LayoutParams).apply {
                        marginStart = it.dip(10)
                        marginEnd = it.dip(10)
                    }
                }
                is Button -> {
                    it.width = it.dip(100)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        it.elevation = 8f
                    }
                }
            }
        }
    }
}
