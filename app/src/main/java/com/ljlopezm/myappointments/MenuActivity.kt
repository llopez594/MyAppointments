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
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        btnProfile.setOnClickListener {
            this.editProfile()
        }

        btnCreateAppointment.setOnClickListener {
            this.createAppointment(it)
        }

        btnMyAppointments.setOnClickListener {
            val intent = Intent(this, AppointmentsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            this.performLogout()
        }
    }

    private fun editProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("CheckResult")
    private fun createAppointment(view: View) {
        val jwt = preferences["jwt", ""]
        apiService.getUser(authHeader = "Bearer $jwt")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.debugLog(Companion.TAG,"TOKEN REGISTRADO CORRECTAMENTE")

                val user = it
                val phoneLength = user?.phone?.length ?: 0

                if (phoneLength >= 6) {
                    val intent = Intent(this@MenuActivity, CreateAppointmentActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, R.string.you_need_a_phone, Snackbar.LENGTH_LONG).show()
                }
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(it.message.toString())
            })
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