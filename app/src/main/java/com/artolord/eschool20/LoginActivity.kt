package com.artolord.eschool20

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.artolord.eschool20.routing.Constants
import com.artolord.eschool20.routing.Interfaces.Callback
import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.Period
import com.artolord.eschool20.routing.Routing_classes.State
import com.artolord.eschool20.routing.Routing_classes.Unit
import java.util.ArrayList

class LoginActivity : AppCompatActivity() {


    class TestCallback(route: Route):Callback<State> {
        override fun onError(errIndex: Int?) {
            Log.e("", errIndex.toString())
        }

        val route = route

        override fun callback(user: State) {
            Log.e(" ",user.prsFio + " "+ user.username + " "+ user.userId)
            route!!.getPeriods(2018,getPeriodCallback(route))
            route!!.getMarks(user.userId,97925,getMarksCallback(route))
        }

    }

    class getPeriodCallback(route: Route):Callback<ArrayList<Period>> {
        override fun onError(errIndex: Int?) {
            Log.e("", errIndex.toString())
        }

        var route: Route? = null
        override fun callback(arrayList: ArrayList<Period>?) {

            for (i in arrayList!!){
                Log.e("",i.periodName+" "+i.periodId)

            }

        }

        fun getPeriodCallback(route: Route){
            this.route = route
        }

    }


    class getMarksCallback(route: Route):Callback<ArrayList<Unit>> {
        override fun onError(errIndex: Int?) {
            Log.e("", errIndex.toString())
        }

        var route: Route? = null
        override fun callback(arrayList: ArrayList<Unit>?) {

            for (i in arrayList!!){
                Log.e("",i.unitName+" "+i.overMark+" "+i.totalmark+" "+i.rating)

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val route = Route(this.baseContext);
        val login = "artgoi"
        val password = "RhutceiljoibOw5"
        val sha256hex = Constants.computeHash(password)
        val a  = TestCallback(route)
        route.login(login, sha256hex,a)

    }
}
