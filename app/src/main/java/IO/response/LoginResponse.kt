package IO.response
import Models.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("jwt")     @Expose val jwt: String,
    @SerializedName("success") @Expose val success: Boolean,
    @SerializedName("user")    @Expose val user: User
)

