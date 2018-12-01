package com.artolord.eschool20.view

import android.content.Intent
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
import com.artolord.eschool20.routing.CallBackInterface
import com.artolord.eschool20.routing.Constants
import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.State
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity() {

    class LoginCallback(var loginActivity : LoginActivity) : CallBackInterface {
        override fun callback(user: State?) {
            if (user != null)
            {
                Controller.state = user
                loginActivity.apply {
                    Toast.makeText(this, R.string.successful_login, Toast.LENGTH_SHORT).show()
                    loginActivity.startActivity<MarksActivity>()
                }
            }
            else
            {
                loginActivity.apply {
                    loginFailed()
                }
            }
        }
    }

    private lateinit var rootLinearLayout: LinearLayout
    private lateinit var loginTextView : EditText
    private lateinit var passwordTextView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
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
                    val route = Route()
                    val hash = Constants.computeHash(password)
                    val a = LoginCallback(this@LoginActivity)
                    route.login(login, hash, a)

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

    fun loginFailed() {
        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
        rootLinearLayout.apply {
            textView(R.string.login_failed)
        }
    }
}
