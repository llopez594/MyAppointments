package Models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Doctor(
    @SerializedName("id")   @Expose val id: Int,
    @SerializedName("name") @Expose val name: String
) {
    override fun toString(): String = name
}