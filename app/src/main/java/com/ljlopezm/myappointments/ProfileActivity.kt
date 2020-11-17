package com.ljlopezm.myappointments

import Extensions.toEditable
import Extensions.toast
import Models.User
import Utils.LogUtil
import Utils.sharedPreferences
import Utils.sharedPreferences.get
import Utils.sharedPreferences.set
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy { ApiService.create() }
    private val preferences by lazy { sharedPreferences.defaultPrefs(this) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val jwt = preferences["jwt", ""]
        apiService.getUser("Bearer $jwt")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val user = it
                if (user != null)
                    this.displayProfileData(user)
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(it.message.toString())

            })

        /*
        Handler().postDelayed({
            displayProfileData()
        }, 3000)
        */
    }

    private fun displayProfileData(user: User) {
        etName.setText(user.name)
        etPhone.setText(user.phone)
        etAddress.setText(user.address)

        progressBarProfile.visibility = View.GONE
        linearLayoutProfile.visibility = View.VISIBLE

        btnSaveProfile.setOnClickListener {
            this.saveProfile()
        }
    }

    @SuppressLint("CheckResult")
    private fun saveProfile() {
        val name = etName.text.toString()
        val phone = etPhone.text.toString()
        val address = etAddress.text.toString()

        if (name.length < 4) {
            inputLayoutName.error = getString(R.string.error_profile_name)
            return
        }

        val jwt = preferences["jwt", ""]
        apiService.postUser("Bearer $jwt", name, phone, address)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                toast(getString(R.string.profile_success_message))
                finish()
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(it.message.toString())

            })
    }
}