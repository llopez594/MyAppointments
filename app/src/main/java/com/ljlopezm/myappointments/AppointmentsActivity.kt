package com.ljlopezm.myappointments

import Adapters.AppointmentsAdapter
import Extensions.toast
import IO.ApiService
import Utils.LogUtil
import Utils.sharedPreferences
import Utils.sharedPreferences.get
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_appointments.*

class AppointmentsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy { ApiService.create() }
    private val preferences by lazy { sharedPreferences.defaultPrefs(this) }

    private val appointmentAdapter = AppointmentsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        this.loadAppointments()

        rvAppointments.layoutManager = LinearLayoutManager(this)
        rvAppointments.adapter = appointmentAdapter
    }

    @SuppressLint("CheckResult")
    private fun loadAppointments() {
        val jwt = preferences["jwt", ""]
        apiService.getAppointments("Bearer $jwt")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.debugLog("Valores", "post submitted to API. $it")

                it?.let {
                    appointmentAdapter.appointments = it
                    appointmentAdapter.notifyDataSetChanged()
                }
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(it.toString())
            })
    }
}