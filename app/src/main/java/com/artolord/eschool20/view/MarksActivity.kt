package com.artolord.eschool20.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.artolord.eschool20.routing.CallBackInterface
import com.artolord.eschool20.routing.Constants
import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.State
import java.util.*

class MarksActivity : AppCompatActivity() {

    class TestCallbeck :CallBackInterface{
        override fun callback(user: State) {
            Log.e(" ",user.prsFio + " "+ user.username + " "+ user.userId)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
