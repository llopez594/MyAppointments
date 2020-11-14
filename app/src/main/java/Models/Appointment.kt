package Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Appointment(
    @SerializedName("id")                @Expose val id: Int,
    @SerializedName("description")       @Expose val description: String,
    @SerializedName("schedule_date")     @Expose val scheduledDate: String,
    @SerializedName("type")              @Expose val type: String,
    @SerializedName("created_at")        @Expose val createdAt: String,
    @SerializedName("status")            @Expose val status: String,
    @SerializedName("scheduled_time_12") @Expose val scheduledTime12: String,
    @SerializedName("specialty")         @Expose val specialty: Specialty,
    @SerializedName("doctor")            @Expose val doctor: Doctor
)