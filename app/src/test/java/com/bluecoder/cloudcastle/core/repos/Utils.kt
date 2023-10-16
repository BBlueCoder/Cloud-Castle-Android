package com.bluecoder.cloudcastle.core.repos


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

fun <T> buildResponse(isResponseSuccessful : Boolean, response : T? = null, errorResponseMessage : String? = null): Response<T> {
    if(isResponseSuccessful)
        return Response.success(response!!)
    val errorBody = errorResponseMessage!!.toResponseBody("application/json".toMediaTypeOrNull())
    return Response.error(401,errorBody)
}