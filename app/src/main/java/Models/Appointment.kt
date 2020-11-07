package Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Appointment (
    @SerializedName(value = "id")            @Expose val id: Int,
    @SerializedName(value = "doctorName")    @Expose val doctorName: String,
    @SerializedName(value = "scheduledDate") @Expose val scheduledDate: String,
    @SerializedName(value = "scheduledTime") @Expose val scheduledTime: String
)