package com.artolord.eschool20.view

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.artolord.eschool20.R
import com.artolord.eschool20.controller.Controller
import com.artolord.eschool20.routing.Routing_classes.Period
import com.artolord.eschool20.routing.Routing_classes.State
import com.artolord.eschool20.view.marks.MarksActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange

class LoginActivity : AppCompatActivity() {

    private lateinit var rootLinearLayout: LinearLayout
    private lateinit var loginTextView : EditText
    private lateinit var passwordTextView : EditText
    private var isFailed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Controller.load(this, ::onLoginCallback)

        rootLinearLayout = verticalLayout {
            gravity = Gravity.CENTER
            loginTextView = editText {
                hint = getString(R.string.login)
                inputType = InputType.TYPE_CLASS_TEXT
                singleLine = true
                maxLines = 1
            }
            passwordTextView = editText {
                hint = getString(R.string.password)
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                singleLine = true
                maxLines = 1
                transformationMethod = PasswordTransformationMethod()
            }
            linearLayout {
                textView {
                    text = getText(R.string.show_password)
                }
                checkBox {
                    onCheckedChange { _, isChecked ->
                        if (isChecked)
                            passwordTextView.transformationMethod = HideReturnsTransformationMethod()
                        else
                            passwordTextView.transformationMethod = PasswordTransformationMethod()
                    }
                }
            }
            button {
                text = getString(R.string.sign_in)
                setOnClickListener {
                    val login = loginTextView.text?.toString() ?: ""
                    val password = passwordTextView.text?.toString() ?: ""
                    Controller.login(login, password, ::onLoginCallback, ::onLoginFailed)

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

    private fun onLoginFailed() {
        if (!isFailed) rootLinearLayout.apply {
            textView(R.string.login_failed)
        }
        isFailed = true
    }

    private fun onLoginCallback(callback: State?) {
        if (callback != null) {
            Controller.state = callback
            Controller.save(this)
            Controller.uploadPeriods(2018, ::onPeriodListCallback, ::onLoginFailed)
        }
        else
            onLoginFailed()
    }

    private fun onPeriodListCallback(callback: ArrayList<Period>) {
        startActivity<MarksActivity>()
    }
}
