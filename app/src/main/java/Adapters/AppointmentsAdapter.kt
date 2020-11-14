package Adapters

import Models.Appointment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ljlopezm.myappointments.R
import kotlinx.android.synthetic.main.item_appointment.view.*

class AppointmentsAdapter: RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder>() {
    //assign adapter
    var appointments = ArrayList<Appointment>()

    class AppointmentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(appointment: Appointment) = with(itemView){

            with(appointment) {
                tvAppointmentId.text = context.getString(R.string.item_appointment_id, id)
                tvDoctorName.text    = doctor.name
                tvScheduledDate.text = context.getString(R.string.item_appointment_date, scheduledDate)
                tvScheduledTime.text = context.getString(R.string.item_appointment_time, scheduledTime12)

                tvSpecialty.text     = specialty.name
                tvDescription.text   = description
                tvStatus.text        = status
                tvType.text          = type
                tvCreatedAt.text     = context.getString(R.string.item_appointment_created_at, createdAt)
            }

            ibExpand.setOnClickListener {
                when(linearLayoutDetails.visibility) {
                    View.VISIBLE -> {
                        linearLayoutDetails.visibility = View.GONE
                        ibExpand.setImageResource(R.drawable.ic_expand_more)
                    }
                    View.GONE    -> {
                        linearLayoutDetails.visibility = View.VISIBLE
                        ibExpand.setImageResource(R.drawable.ic_expand_less)
                    }
                }
            }
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