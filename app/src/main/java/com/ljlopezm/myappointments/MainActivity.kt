package com.ljlopezm.myappointments

import Extensions.toast
import Utils.LogUtil
import Utils.sharedPreferences
import Utils.sharedPreferences.get
import Utils.sharedPreferences.set
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private val apiService: ApiService by lazy { ApiService.create() }

    private val snackbar by lazy { Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(FLAG_SECURE, FLAG_SECURE)
        setContentView(R.layout.activity_main)

        // shared preferences
        preferences = sharedPreferences.defaultPrefs(this)

        if (preferences["jwt", ""].contains(".")) {
            this.goToMenuActivity()
        }

        btnLogin.setOnClickListener {
            // validates
            this.performLogin(
                etEmail.text.toString(),
                etPassword.text.toString()
            )
        }

        tvGoToRegister.setOnClickListener {
            toast(getString(R.string.please_fill_your_register_data), Toast.LENGTH_LONG)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("CheckResult")
    private fun performLogin(email: String, password: String) {

        if(email.trim().isEmpty() || password.trim().isEmpty())
        {
            toast(getString(R.string.error_empty_credentials))
            return
        }
        else {
            apiService.postLogin(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LogUtil.debugLog("Valores", "post submitted to API. $it")
                    val loginResponse = it

                    if (loginResponse.success) {
                        this.createSessionPreferences(loginResponse.jwt)
                        toast(getString(R.string.welcome_name, loginResponse.user.name))
                        this.goToMenuActivity(isUserInput = true)
                    } else {
                        toast(getString(R.string.error_invalid_credentials))
                    }
                }, {
                    LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                    toast("${getString(R.string.error_login_response)}. $it")
                })
        }
    }

    private fun createSessionPreferences(jwt: String) {
        preferences["jwt"] = jwt
    }

    private fun goToMenuActivity(isUserInput: Boolean = false) {
        val intent = Intent(this, MenuActivity::class.java)

        if (isUserInput) {
            intent.putExtra("store_token", true)
        }

        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (snackbar.isShown)
            super.onBackPressed()
        else
            snackbar.show()
    }
}