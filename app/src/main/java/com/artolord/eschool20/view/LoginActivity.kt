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
import com.artolord.eschool20.routing.Interfaces.LoginCallback
import com.artolord.eschool20.routing.Constants
import com.artolord.eschool20.routing.Interfaces.GetMarksCallback
import com.artolord.eschool20.routing.Interfaces.GetPeriodCallback
import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.Period
import com.artolord.eschool20.routing.Routing_classes.State
import com.artolord.eschool20.routing.Routing_classes.Unit
import org.jetbrains.anko.*
import java.util.*
import kotlin.collections.ArrayList

class LoginActivity : AppCompatActivity(), LoginCallback, GetPeriodCallback, GetMarksCallback{

    private lateinit var rootLinearLayout: LinearLayout
    private lateinit var loginTextView : EditText
    private lateinit var passwordTextView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Controller.route == null)
            Controller.route = Route()
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

    override fun loginCallback(user: State?) {
        if (user != null)
        {
            Controller.state = user
            Toast.makeText(this, R.string.successful_login, Toast.LENGTH_SHORT).show()
            Controller.route?.getPeriods(2018, this)
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

    override fun getPeriodCallback(arrayList: ArrayList<Period>?) {
        Controller.periodList = arrayList
    }

    override fun getMarksCallback(periodId : Int, list: ArrayList<Unit>?) {
        val alist : ArrayList<Unit> = list ?: arrayListOf()
        Controller.unitByPersonMap?.set(periodId, alist)
    }

    private fun loginFailed() {
        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
        rootLinearLayout.apply {
            textView(R.string.login_failed)
        }
    }
}
