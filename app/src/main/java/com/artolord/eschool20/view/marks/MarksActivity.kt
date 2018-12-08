package com.artolord.eschool20.view.marks

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.artolord.eschool20.R
import com.artolord.eschool20.controller.Controller
import com.artolord.eschool20.view.LoginActivity
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.startActivity

class MarksActivity : AppCompatActivity() {
    private val marksFragment = MarksFragment()
    private var lastId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            navigation_view.setCheckedItem(menuItem.itemId)
            navigation_view.menu.findItem(lastId).isChecked = false
            menuItem.isChecked = true
            lastId = menuItem.itemId
            when (lastId){
                R.id.exit -> {
                    Controller.route.delete(this)
                    startActivity<LoginActivity>()
                }
            }
            drawer_layout.closeDrawers()

            true
        }

        navigation_view.menu.getItem(0).isChecked = true
        lastId = navigation_view.menu.getItem(0).itemId

        supportFragmentManager.beginTransaction().replace(R.id.change_me, marksFragment).commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
