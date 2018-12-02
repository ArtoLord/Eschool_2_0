package com.artolord.eschool20.view

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.artolord.controller.Controller
import com.artolord.eschool20.R
import com.artolord.eschool20.routing.Constants
import com.artolord.eschool20.routing.Interfaces.Callback
import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.Period
import com.artolord.eschool20.routing.Routing_classes.State
import org.jetbrains.anko.*
import kotlin.collections.ArrayList

class LoginActivity : AppCompatActivity(), Callback<State> {

    class PeriodCallback : Callback<ArrayList<Period>> {
        override fun callback(callback: ArrayList<Period>?, vararg args : Any) {
            Controller.periodList = callback
        }

        override fun onError(errIndex: Int?) {}

    }

    override fun callback(callback: State?, vararg args : Any) {
        if (callback != null)
        {
            Controller.state = callback
            Toast.makeText(this, R.string.successful_login, Toast.LENGTH_SHORT).show()
            Controller.route?.getPeriods(2018, PeriodCallback())
            Toast.makeText(this, R.string.successful_login, Toast.LENGTH_SHORT).show()
            doAsync {
                while ((Controller.periodList?.size ?: 0) == 0);
                startActivity<MarksActivity>()
            }

        }
        else
        {
            loginFailed()
        }
    }

    override fun onError(errIndex: Int?) {}

    private lateinit var rootLinearLayout: LinearLayout
    private lateinit var loginTextView : EditText
    private lateinit var passwordTextView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Controller.route == null)
            Controller.route = Route(this)
        super.onCreate(savedInstanceState)
        rootLinearLayout = verticalLayout {
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
                    val login = loginTextView.text?.toString()
                    val password = passwordTextView.text?.toString()
                    val hash = Constants.computeHash(password)
                    Controller.route!!.login(login, hash, this@LoginActivity)

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

    private fun loginFailed() {
        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
        rootLinearLayout.apply {
            textView(R.string.login_failed)
        }
    }
}
