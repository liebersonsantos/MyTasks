package com.example.mytasks.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mytasks.R
import com.example.mytasks.TasksConstants.TaskConstants
import com.example.mytasks.business.UserBusiness
import com.example.mytasks.util.SecurityPreferences
import com.example.mytasks.view.main.MainActivity
import com.example.mytasks.view.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mUserBusiness = UserBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)

        setListeners()
    }

    fun setListeners() {
        btnToLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnToLogin -> handleLogin()

        }

    }

    private fun handleLogin() {
        val email = emailLogin.text.toString()
        val password = passwordLogin.text.toString()

        if (mUserBusiness.login(email, password)) {
            goToMain()

        } else {
            Toast.makeText(this@LoginActivity, getString(R.string.message_error_login), Toast.LENGTH_LONG).show()
        }
    }

    private fun goToMain() {
        val intent = MainActivity.getIntent(this@LoginActivity)
        this@LoginActivity.startActivity(intent)
        finish()
    }

    private fun verifyLoggedUser() {
        val userId = mSecurityPreferences.getStoredString(TaskConstants.USER_ID)
        val userName = mSecurityPreferences.getStoredString(TaskConstants.USER_NAME)

        if (userId != "" && userName != "") {
            goToMain()
        }

    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
