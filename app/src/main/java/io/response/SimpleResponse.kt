package io.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("success") @Expose val success: Boolean
)