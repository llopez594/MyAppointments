package com.ljlopezm.myappointments

import Extensions.toEditable
import Extensions.toast
import IO.ApiService
import Models.Doctor
import Models.Schedule
import Models.Specialty
import Utils.LogUtil
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_appointment.*
import kotlinx.android.synthetic.main.card_view_step_one.*
import kotlinx.android.synthetic.main.card_view_step_three.*
import kotlinx.android.synthetic.main.card_view_step_two.*
import java.util.*
import kotlin.collections.ArrayList

class CreateAppointmentActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy { ApiService.create() }

    private val selectedCalendar = Calendar.getInstance()
    private var selectedTimeRadioButton: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        btnNext.setOnClickListener {
            if(etDescription.text.toString().length < 3) {
                etDescription.error = getString(R.string.validate_appointment_description)
            } else {
                // continue to step 2
                cvStep1.visibility = View.GONE
                cvStep2.visibility = View.VISIBLE
            }

        }

        btnNext2.setOnClickListener {
            this.showAppointmentDataToConfirm()
            when {
                etScheduledDate.text.toString().isEmpty() -> {
                    etScheduledDate.error = getString(R.string.validate_appointment_date)
                }
                selectedTimeRadioButton == null -> {
                    Snackbar.make(createAppointmentLinearLayout, R.string.validate_appointment_time, Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    // continue to step 3
                    cvStep2.visibility = View.GONE
                    cvStep3.visibility = View.VISIBLE
                }
            }
        }

        btnConfirmAppointment.setOnClickListener {
            toast("Cita registrada correctamente")
            finish()
        }

        etDescription.text = "asd".toEditable()
        this.loadSpecialties()
        this.listenSpecialtyChanges()
        this.listenDoctorAndDateChanges()
    }

    private fun listenSpecialtyChanges() {
        spinnerSpecialties.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val specialty = adapter?.getItemAtPosition(position) as Specialty
                loadDoctors(SpecialtyId = specialty.id)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  }
        }
    }

    private fun listenDoctorAndDateChanges() {
        // doctors
        spinnerDoctors.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val doctor = adapter?.getItemAtPosition(position) as Doctor
                loadHours(doctorId = doctor.id, date = etScheduledDate.text.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  }
        }

        // schduled date
        etScheduledDate.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val doctor = spinnerDoctors.selectedItem as Doctor
                loadHours(doctorId = doctor.id, date = etScheduledDate.text.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

            override fun afterTextChanged(p0: Editable?) {  }
        } )
    }

    @SuppressLint("CheckResult")
    private fun loadSpecialties() {
        apiService.getSpecialties()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.debugLog("Valores", "post submitted to API. $it")
                spinnerSpecialties.adapter = ArrayAdapter<Specialty>(this, android.R.layout.simple_list_item_1, it)
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(getString(R.string.error_loading_specialties))
                finish()
            })
    }

    @SuppressLint("CheckResult")
    private fun loadDoctors(SpecialtyId: Int) {
        apiService.getDoctors(specialty = SpecialtyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.debugLog("Valores", "post submitted to API. $it")
                spinnerDoctors.adapter = ArrayAdapter<Doctor>(this, android.R.layout.simple_list_item_1, it)
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                toast(getString(R.string.error_loading_doctors))
                finish()
            })
    }

    @SuppressLint("CheckResult")
    private fun loadHours(doctorId: Int, date: String) {
        if (date.isEmpty()) {
            return
        }

        apiService.getHours(doctorId = doctorId, date = date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.debugLog("Valores", "post submitted to API. $it")
                tvSelectDoctorAndDate.visibility = View.GONE

                val intervals = it.morning + it.afternoon
                val hours = intervals.map { item -> item.start } as ArrayList<String>
                displayIntervalRadios(hours)
            }, {
                LogUtil.errorLog("Valores", "Error en el subscribe!!. $it")
                Toast.makeText(this@CreateAppointmentActivity, getString(R.string.error_loading_hours), Toast.LENGTH_SHORT).show()
            })
    }

    private fun showAppointmentDataToConfirm() {
        tvConfirmDescription.text = etDescription.text.toString()
        tvConfirmSpecialty.text = spinnerSpecialties.selectedItem.toString()

        val selectedRadioBtnId = radioGroupType.checkedRadioButtonId
        val selectedRadioType = radioGroupType.findViewById<RadioButton>(selectedRadioBtnId)
        tvConfirmType.text = selectedRadioType.text.toString()

        tvConfirmDoctorName.text = spinnerDoctors.selectedItem.toString()
        tvConfirmDate.text = etScheduledDate.text.toString()
        tvConfirmTime.text = selectedTimeRadioButton?.text.toString()
    }

    fun onClickScheduledDate(view: View) {
        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            selectedCalendar.set(y, m, d)

            etScheduledDate.text = resources.getString(
                R.string.date_format,
                y,
                (m+1).twoDigits(),
                d.twoDigits()
            ).toEditable()
            etScheduledDate.error = null
        }

        //new dialog
        val datePickerDialog = DatePickerDialog(this, listener, year, month, dayOfMonth)

        // set limits
        val datePicker = datePickerDialog.datePicker
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        datePicker.minDate = calendar.timeInMillis // +1
        calendar.add(Calendar.DAY_OF_MONTH, 29)
        datePicker.maxDate = calendar.timeInMillis // +30

        // show dialog
        datePickerDialog.show()
    }

    private fun displayIntervalRadios(hours: ArrayList<String>) {
        selectedTimeRadioButton = null
        radioGroupLeft.removeAllViews()
        radioGroupRight.removeAllViews()

        if(hours.isEmpty()) {
            tvNoAvailableHours.visibility = View.VISIBLE
            return
        }
        tvNoAvailableHours.visibility = View.GONE

        var goToLeft = true

        hours.forEach {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = it

            radioButton.setOnClickListener { view ->
                selectedTimeRadioButton?.isChecked = false

                selectedTimeRadioButton = view as RadioButton?
                selectedTimeRadioButton?.isChecked = true
            }

            if (goToLeft)
                radioGroupLeft.addView(radioButton)
            else
                radioGroupRight.addView(radioButton)
            goToLeft = !goToLeft
        }
    }

    private fun Int.twoDigits() = if(this>=10) this.toString() else "0$this"

    override fun onBackPressed() {
        when {
            cvStep3.visibility == View.VISIBLE -> {
                cvStep3.visibility = View.GONE
                cvStep2.visibility = View.VISIBLE
            }
            cvStep2.visibility == View.VISIBLE -> {
                cvStep2.visibility = View.GONE
                cvStep1.visibility = View.VISIBLE
            }
            cvStep1.visibility == View.VISIBLE -> {
                val builder = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_create_appointment_exit_title))
                    .setMessage(getString(R.string.dialog_create_appointment_exit_message))
                    .setPositiveButton(getString(R.string.dialog_create_appointment_exit_positive_btn)) { _, _ ->
                        finish()
                    }
                    .setNegativeButton(getString(R.string.dialog_create_appointment_exit_negative_btn)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)

                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}