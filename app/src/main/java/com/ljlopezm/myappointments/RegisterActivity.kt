package com.ljlopezm.myappointments

import Extensions.toast
import Utils.LogUtil
import Utils.sharedPreferences
import Utils.sharedPreferences.get
import Utils.sharedPreferences.set
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import io.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy { ApiService.create() }
    private val preferences by lazy { sharedPreferences.defaultPrefs(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(FLAG_SECURE,FLAG_SECURE)
        setContentView(R.layout.activity_register)

        tvGoToLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnConfirmRegister.setOnClickListener {
            this.performRegister()
        }
    }

    @SuppressLint("CheckResult")
    private fun performRegister() {
        val name = etRegisterName.text.toString().trim()
        val email = etRegisterEmail.text.toString().trim()
        val password = etRegisterPassword.text.toString()
        val passwordConfirm = etRegisterPasswordConfirmation.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            toast(getString(R.string.error_register_empty_fields))
            return
        }

        if (password != passwordConfirm) {
            toast(getString(R.string.error_register_passwords_do_not_match))
            return
        }

        apiService.postRegisteer(
            name = name,
            email = email,
            password =  password,
            passwordConfirmation = passwordConfirm
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.debugLog("Valores", "post submitted to API. $it")
                val loginResponse = it

                if (loginResponse.success) {
                    this.createSessionPreferences(loginResponse.jwt)
                    toast(getString(R.string.welcome_name, loginResponse.user.name))
                    this.goToMenuActivity()
                }else {
                    toast(getString(R.string.error_register_validarion))
                }
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(getString(R.string.error_register_validarion))
            })
    }

    private fun createSessionPreferences(jwt: String) {
        preferences["jwt"] = jwt
    }

    private fun goToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}