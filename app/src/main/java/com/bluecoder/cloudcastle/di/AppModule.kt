package com.bluecoder.cloudcastle.di

import android.content.Context
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.bluecoder.cloudcastle.CacheManager
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import com.bluecoder.cloudcastle.data.repos.UsersRepo
import com.bluecoder.cloudcastle.utils.Constants.API_BASE_URL
import com.bluecoder.cloudcastle.utils.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCacheManager(): CacheManager {
        return CacheManager()
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(@ApplicationContext context: Context) =
        SharedPreferencesManager(context)

    @Singleton
    @Provides
    fun provideOkHTTPClient(
        requestInterceptor: RequestInterceptor,
        responseInterceptor: ResponseInterceptor
    ): OkHttpClient  {
        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .addInterceptor(requestInterceptor)
            .addInterceptor(responseInterceptor)


        return builder.build()
    }

    @Provides
    fun provideOkHttpDataSource(okHttpClient: OkHttpClient): OkHttpDataSource.Factory {
        return OkHttpDataSource.Factory(okHttpClient)
    }

    @Provides
    fun provideDataSourceFactory(@ApplicationContext context: Context,okHttpDataSource: OkHttpDataSource.Factory): DefaultDataSource.Factory {
        return DefaultDataSource.Factory(context,okHttpDataSource)
    }

    @Provides
    fun provideExoPlayer(@ApplicationContext context: Context,dataSource : DefaultDataSource.Factory): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(context).setDataSourceFactory(dataSource)
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideApplicationAPI(okHttpClient: OkHttpClient) : ServerAPI {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ServerAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideUsersRepo(serverAPI: ServerAPI): UsersRepo {
        return UsersRepo(serverAPI)
    }

    @Singleton
    @Provides
    fun provideFilesRepo(serverAPI: ServerAPI, cacheManager: CacheManager): FilesRepo {
        return FilesRepo(serverAPI,cacheManager)
    }


}