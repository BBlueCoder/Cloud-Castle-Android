package com.bluecoder.cloudcastle.di

import com.bluecoder.cloudcastle.data.pojoclasses.UserJWT
import com.bluecoder.cloudcastle.utils.Constants
import com.bluecoder.cloudcastle.utils.SharedPreferencesManager
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ResponseInterceptor @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : Interceptor{

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if(response.code == 401){ // token is expired or invalid
            val username = sharedPreferencesManager.getPreference("username")
            val password = sharedPreferencesManager.getPreference("password")

            if(username != null && password != null){
                val userJWT = login(username,password)
                userJWT?.let {
                    sharedPreferencesManager.addPreference("token",userJWT.token)
                    val newResponse = makeNewCall(chain,it.token)

                    if(newResponse.isSuccessful)
                        return newResponse

                }
            }
        }

        return response
    }

    private fun login(username : String,password : String): UserJWT?{

        val requestData = """
            {
                "username":"$username",
                "password":"$password"
            }
        """.trimIndent()

        val requestBody = requestData.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("${Constants.API_BASE_URL}users/login")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        if(response.isSuccessful) {
            return Gson().fromJson(response.body!!.string(), UserJWT::class.java)
        }
        return null
    }

    private fun makeNewCall(chain : Interceptor.Chain,token : String) : Response{
        return  client.newCall(
            chain.request().newBuilder()
                .addHeader("authentication",token)
                .build()
        ).execute()
    }
}