package com.artolord.eschool20.controller

import android.content.Context
import com.artolord.eschool20.routing.Constants
import com.artolord.eschool20.routing.Interfaces.Callback
import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.Mark
import com.artolord.eschool20.routing.Routing_classes.Period
import com.artolord.eschool20.routing.Routing_classes.State
import com.artolord.eschool20.routing.Routing_classes.Unit

object Controller {
    var route : Route = Route()
    var state : State = State()
    var periodList : List<Period> = arrayListOf()
    var unitByPersonMap : MutableMap<Int, ArrayList<Unit>> = mutableMapOf()
    var marksList : MutableMap<Int, ArrayList<Mark>> = mutableMapOf()
    var periodByPeriodId : MutableMap<Int, Period> = mutableMapOf()
    var unitByUnitId : MutableMap<Int, Unit> = mutableMapOf()

    fun exit() {
        route = Route()
        state = State()
        periodList = arrayListOf()
        unitByPersonMap = mutableMapOf()
        marksList = mutableMapOf()
        periodByPeriodId = mutableMapOf()
        unitByUnitId = mutableMapOf()
    }

    fun load(context : Context, onCallback : (State) -> kotlin.Unit) {
        route.load(context) //Загрузка cookie
        if (!Controller.route.isCookieNull){
            Controller.route.state(LoginCallback(onCallback, {})) //Автоматический вход
        }
    }

    fun getMarks(periodId : Int) = marksList[periodId] ?: arrayListOf()

    fun uploadPeriods(year : Int, onCallback : (List<Period>) -> kotlin.Unit, onFailure : () -> kotlin.Unit)  {
        route.getPeriods(year, PeriodCallback(onCallback, onFailure))
    }

    fun uploadUnits(periodId: Int, onCallback : (ArrayList<Unit>) -> kotlin.Unit, onFailure : () -> kotlin.Unit) {
        route.getMarks(state.userId, periodId, UnitCallback(onCallback, onFailure))
    }

    fun uploadMarks(periodId: Int, onCallback : (ArrayList<Mark>) -> kotlin.Unit, onFailure : () -> kotlin.Unit) {
        route.getMarksWithWights(state.userId, periodId, MarkCallback(onCallback, onFailure))
    }

    fun save(context: Context) {
        route.save(context)
    }

    fun login(login : String, password : String, onCallback: (State) -> kotlin.Unit, onFailure: () -> kotlin.Unit) {
        val hash = Constants.computeHash(password)
        route.login(login, hash, LoginCallback(onCallback, onFailure))
    }

    class PeriodCallback(private val onCallback : (List<Period>) -> kotlin.Unit, private val onFailure : () -> kotlin.Unit) : Callback<ArrayList<Period>> {
        override fun callback(callback: ArrayList<Period>?, vararg args: Any?) {
            if (callback != null) {
                val periods = callback.filter { it.isStudy }
                Controller.periodList = periods.toMutableList()
                periods.forEach {
                    Controller.periodByPeriodId[it.periodId] = it
                }
                onCallback(periods)
            }
            else onFailure()
        }
        override fun onError(errIndex: Int?) { onFailure() }
    }

    class LoginCallback(private val onCallback : (State) -> kotlin.Unit, private val onFailure : () -> kotlin.Unit) : Callback<State> {
        override fun callback(callback: State?, vararg args: Any?) {
            if (callback != null) {
                Controller.state = callback
                onCallback(callback)
            }
            else onFailure()
        }
        override fun onError(errIndex: Int?) { onFailure() }
    }
    class UnitCallback(private val onCallback : (ArrayList<Unit>) -> kotlin.Unit, private val onFailure : () -> kotlin.Unit) : Callback<ArrayList<Unit>> {
        override fun callback(callback: ArrayList<Unit>?, vararg args: Any?) {
            if (callback != null) {
                val periodId = (args[0] as Int?) ?: 0
                Controller.unitByPersonMap[periodId] = callback
                callback.forEach {
                    Controller.unitByUnitId[it.unitId] = it
                }
                onCallback(callback)
            }
            else onFailure()
        }

        override fun onError(errIndex: Int?) { onFailure() }
    }

    class MarkCallback(private val onCallback : (ArrayList<Mark>) -> kotlin.Unit, private val onFailure : () -> kotlin.Unit) : Callback<ArrayList<Mark>> {
        override fun callback(callback: ArrayList<Mark>?, vararg args: Any?) {
            if (callback != null) {
                val periodId = (args[0] as Int?) ?: 0
                Controller.marksList[periodId] = callback
                onCallback(callback)
            }
            else onFailure()
        }

        override fun onError(errIndex: Int?) { onFailure() }
    }

    fun getPeriod() = (periodList.getOrNull(0)?.periodId) ?: 0
}