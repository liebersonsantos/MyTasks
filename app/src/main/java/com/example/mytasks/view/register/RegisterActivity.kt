package com.example.mytasks.view.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mytasks.R
import com.example.mytasks.business.UserBusiness
import com.example.mytasks.util.ValidationException
import com.example.mytasks.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setListeners()
        mUserBusiness = UserBusiness(this@RegisterActivity)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnToRegister -> {
                handleSave()
            }
        }

    }

    private fun handleSave() {
        try {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassaword.text.toString()

            mUserBusiness.insert(name, email, password)

            val intent = MainActivity.getIntent(this@RegisterActivity)
            this@RegisterActivity.startActivity(intent)
            finish()

        } catch (e: ValidationException) {
            Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this@RegisterActivity, getString(R.string.generic_error), Toast.LENGTH_LONG).show()
        }

    }

    private fun setListeners() {
        btnToRegister.setOnClickListener(this)

    }

    companion object{
        fun getIntent(context: Context): Intent{
            return Intent(context, RegisterActivity::class.java)
        }
    }


}
