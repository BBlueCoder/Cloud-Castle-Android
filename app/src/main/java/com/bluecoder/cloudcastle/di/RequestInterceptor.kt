package com.bluecoder.cloudcastle.di

import com.bluecoder.cloudcastle.utils.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferencesManager.getPreference("token")
        val request = chain.request().newBuilder()

        val headers = chain.request().headers
        if(headers["authentication"] == null){
            token?.let {
                request.addHeader(
                    "authentication",token
                )
            }
        }

        return chain.proceed(request.build())
    }
}