package com.artolord.controller

import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.Period
import com.artolord.eschool20.routing.Routing_classes.State
import com.artolord.eschool20.routing.Routing_classes.Unit

object Controller {
    var route : Route? = null
    var state : State? = null
    var periodList : ArrayList<Period>? = null
    var unitByPersonMap: MutableMap<Int, ArrayList<Unit>>? = mutableMapOf()

    fun exit() {
        route = null
        state = null
        periodList = null
        unitByPersonMap = null
    }

    fun initRoute() {route = Route()}
}