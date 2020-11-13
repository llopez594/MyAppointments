package Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HoursInterval(
    @SerializedName("end")   @Expose val end: String,
    @SerializedName("start") @Expose val start: String
) {
    override fun toString(): String = "$start - $end"
}