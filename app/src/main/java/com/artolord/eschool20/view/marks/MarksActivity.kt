package com.artolord.eschool20.view.marks

import android.app.FragmentManager
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.artolord.eschool20.controller.Controller
import com.artolord.eschool20.R
import com.artolord.eschool20.routing.Interfaces.Callback
import com.artolord.eschool20.routing.Routing_classes.Mark
import com.artolord.eschool20.routing.Routing_classes.Unit
import com.artolord.eschool20.view.recyclerView
import org.jetbrains.anko.*
import kotlin.collections.ArrayList

class MarksActivity : AppCompatActivity() {
    private val marksFragment = MarksFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        var mDrawerLayout:DrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()

            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.change_me, marksFragment).commit()

    }

    override fun onStart() {
        super.onStart()
        //supportFragmentManager.beginTransaction().replace(R.id.marks_fragment, marksFragment).commit()
    }
}
