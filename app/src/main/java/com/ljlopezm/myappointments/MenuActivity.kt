package com.ljlopezm.myappointments

import Extensions.toast
import io.ApiService
import Utils.LogUtil
import Utils.sharedPreferences
import Utils.sharedPreferences.get
import Utils.sharedPreferences.set
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy { ApiService.create() }
    private val preferences by lazy { sharedPreferences.defaultPrefs(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(FLAG_SECURE, FLAG_SECURE)
        setContentView(R.layout.activity_menu)

        val storeToken = intent.getBooleanExtra("store_token", false)
        if (storeToken)
            this.storeToken()

        btnCreateAppointment.setOnClickListener {
            val intent = Intent(this, CreateAppointmentActivity::class.java)
            startActivity(intent)
        }

        btnMyAppointments.setOnClickListener {
            val intent = Intent(this, AppointmentsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            this.performLogout()
        }
    }

    private fun storeToken() {
        val jwt = preferences["jwt", ""]

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            val deviceToken = instanceIdResult.token

            apiService.postToken(
                authHeader = "Bearer $jwt",
                fcmToken =  deviceToken
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LogUtil.debugLog(Companion.TAG,"TOKEN REGISTRADO CORRECTAMENTE")
                }, {
                    LogUtil.debugLog(Companion.TAG, "HUBO UN PROBLEMA AL REGISTRAR EL TOKEN. $it")
                })
        }
    }

    @SuppressLint("CheckResult")
    private fun performLogout() {
        val jwt = preferences["jwt", ""]
        apiService.postLogout("Bearer $jwt")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                it?.let {
                    this@MenuActivity.clearSessionPreferences()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(getString(R.string.error_logout_session))

            })
    }

    private fun clearSessionPreferences() {
        preferences["jwt"] = ""
        sharedPreferences.clearPrefs(this)
    }

    companion object {
        private const val TAG = "MenuActivity"
    }

}