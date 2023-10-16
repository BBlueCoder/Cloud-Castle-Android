package com.bluecoder.cloudcastle

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.media3.exoplayer.ExoPlayer
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.di.AppModule
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import com.bluecoder.cloudcastle.ui.screens.ScreenArgs
import com.bluecoder.cloudcastle.ui.screens.content.ContentScreenViewModel
import com.bluecoder.cloudcastle.ui.screens.main.MainViewModel
import com.bluecoder.cloudcastle.utils.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Singleton
    @Provides
    fun provideServerApi(): ServerAPI {
        return mockk()
    }

    @Singleton
    @Provides
    fun provideFilesRepo(serverAPI: ServerAPI): FilesRepo {
        return FilesRepo(serverAPI, CacheManager())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }

    @Singleton
    @Provides
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Singleton
    @Provides
    fun provideMainViewModel(filesRepo: FilesRepo): MainViewModel {
        return MainViewModel(filesRepo)
    }

    @Singleton
    @Provides
    fun provideContentScreenViewModel(filesRepo: FilesRepo): ContentScreenViewModel {
        val savedStateHandle = SavedStateHandle()
        savedStateHandle[ScreenArgs.FileType.name] = "image"
        return ContentScreenViewModel(filesRepo, savedStateHandle)
    }

}