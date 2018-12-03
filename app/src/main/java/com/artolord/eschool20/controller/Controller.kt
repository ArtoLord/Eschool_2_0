package com.artolord.eschool20.controller

import com.artolord.eschool20.routing.Route
import com.artolord.eschool20.routing.Routing_classes.Mark
import com.artolord.eschool20.routing.Routing_classes.Period
import com.artolord.eschool20.routing.Routing_classes.State
import com.artolord.eschool20.routing.Routing_classes.Unit

object Controller {
    var route : Route? = null
    var state : State? = null
    var periodList : ArrayList<Period>? = null
    var unitByPersonMap : MutableMap<Int, ArrayList<Unit>>? = mutableMapOf()
    var marksList : MutableMap<Int, ArrayList<Mark>>? = mutableMapOf()

    fun exit() {
        route = null
        state = null
        periodList = null
        unitByPersonMap = null
        marksList = null
    }
}