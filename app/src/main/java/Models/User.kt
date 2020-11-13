package Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("address") @Expose val address: String,
    @SerializedName("dni")     @Expose val dni: String,
    @SerializedName("email")   @Expose val email: String,
    @SerializedName("id")      @Expose val id: Int,
    @SerializedName("name")    @Expose val name: String,
    @SerializedName("phone")   @Expose val phone: String,
    @SerializedName("role")    @Expose val role: String
)