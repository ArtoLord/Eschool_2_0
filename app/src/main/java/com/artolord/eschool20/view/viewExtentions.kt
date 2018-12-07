package com.artolord.eschool20.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import org.jetbrains.anko.custom.ankoView

fun ViewGroup.recyclerView() = recyclerView {}
inline fun ViewGroup.recyclerView(init : ViewGroup.() -> Unit) : RecyclerView = ankoView({RecyclerView(it)}, theme = 0, init = init)