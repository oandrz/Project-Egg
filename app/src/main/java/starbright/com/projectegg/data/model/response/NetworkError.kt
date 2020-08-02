/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 13/10/2019.
 */

package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkError(
    @Expose
    @SerializedName("status")
    val status: String = "failed",
    @Expose
    @SerializedName("code")
    val statusCode: Int = -1,
    @Expose
    @SerializedName("message")
    val message: String = "failed"
)