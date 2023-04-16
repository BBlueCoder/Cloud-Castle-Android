package com.bluecoder.cloudcastle.core.di

import com.bluecoder.cloudcastle.core.api.ServerAPI
import com.bluecoder.cloudcastle.core.repos.users.DefaultUsersRepo
import com.bluecoder.cloudcastle.core.repos.users.UsersRepo
import com.bluecoder.cloudcastle.utils.Constants.API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHTTPClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60,TimeUnit.SECONDS)
        .readTimeout(60,TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideApplicationAPI(okHttpClient: OkHttpClient) : ServerAPI{
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ServerAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideUsersRepo(serverAPI: ServerAPI): UsersRepo{
        return DefaultUsersRepo(serverAPI)
    }
}