package Adapters

import Extensions.getString
import Models.Appointment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ljlopezm.myappointments.R
import kotlinx.android.synthetic.main.item_appointment.view.*

class AppointmentsAdapter(private val appointments: ArrayList<Appointment>) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder>() {

    class AppointmentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(appointment: Appointment) = with(itemView){

            tvAppointmentId.text = context.getString(R.string.item_appointment_id, appointment.id)

            tvDoctorName.text    = appointment.doctorName

            tvScheduledDate.text = context.getString(R.string.item_appointment_date, appointment.scheduledDate)

            tvScheduledTime.text = context.getString(R.string.item_appointment_time, appointment.scheduledTime)
        }
    }

    // inflates XML items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)

        return AppointmentsViewHolder(view)
    }

    // Binds data
    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) = holder.bind(appointments[position])

    // Returns of elements
    override fun getItemCount() = appointments.size
}