package com.ljlopezm.myappointments

import Utils.sharedPreferences
import Utils.sharedPreferences.get
import Utils.sharedPreferences.set
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // shared preferences
        preferences = sharedPreferences.defaultPrefs(this)

        if (preferences["session", false]) {
            this.goToMenuActivity()
        }

        btnLogin.setOnClickListener {
            // validates
            createSessionPreferences()
            this.goToMenuActivity()
        }

        tvGoToRegister.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.please_fill_your_register_data),
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createSessionPreferences() {
        preferences["session"] = true
    }

    private fun goToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}