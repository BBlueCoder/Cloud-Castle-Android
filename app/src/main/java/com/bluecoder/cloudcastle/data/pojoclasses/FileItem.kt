package com.bluecoder.cloudcastle.data.pojoclasses

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class FileItem(
    val id : Int,
    @SerializedName("originname") val originName : String,
    @SerializedName("savedname") val savedName : String,
    @SerializedName("filetype") val fileType : String,
    @SerializedName("contentlength") val contentLength : Long,
    @SerializedName("dateinmillis") val dateInMillis : Long,
    val duration : Double?
) : Serializable
