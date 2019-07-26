package com.example.mytasks.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.mytasks.R
import com.example.mytasks.TasksConstants.TaskConstants
import com.example.mytasks.business.PriorityBusiness
import com.example.mytasks.repository.PriorityCacheConstants
import com.example.mytasks.util.SecurityPreferences
import com.example.mytasks.view.TaskListFragment
import com.example.mytasks.view.login.LoginActivity
import com.example.mytasks.view.taskForm.TaskFormActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mPriorityBusiness: PriorityBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        mSecurityPreferences = SecurityPreferences(this@MainActivity)
        mPriorityBusiness = PriorityBusiness(this@MainActivity)

        startDefaultFragment()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null


        when (item.itemId) {
            R.id.nav_done -> fragment = TaskListFragment.newInstance(TaskConstants.COMPLETE)
            R.id.nav_todo -> fragment = TaskListFragment.newInstance(TaskConstants.TODO)
            R.id.nav_logout -> handleLogout()
        }

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameContent, fragment!!). commit()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleLogout() {
        mSecurityPreferences.removeStored(TaskConstants.USER_ID)
        mSecurityPreferences.removeStored(TaskConstants.USER_NAME)
        mSecurityPreferences.removeStored(TaskConstants.USER_EMAIL)

        val intent = LoginActivity.getIntent(this@MainActivity)
        startActivity(intent)
        finish()
    }

    private fun startDefaultFragment() {
        val fragment: Fragment = TaskListFragment.newInstance(TaskConstants.COMPLETE)
        supportFragmentManager.beginTransaction().replace(R.id.frameContent, fragment). commit()

    }

    private fun loadPriorityCache(){
        PriorityCacheConstants.setCache(mPriorityBusiness.getList())
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
