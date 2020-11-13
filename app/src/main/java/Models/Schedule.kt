package Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Schedule(
    @SerializedName("afternoon") @Expose val afternoon: ArrayList<HoursInterval>,
    @SerializedName("morning")   @Expose val morning:   ArrayList<HoursInterval>
)
