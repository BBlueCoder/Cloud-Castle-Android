package com.bluecoder.cloudcastle

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.media3.exoplayer.ExoPlayer
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.pojoclasses.FilesCount
import com.bluecoder.cloudcastle.data.pojoclasses.UserJWT
import com.bluecoder.cloudcastle.di.AppModule
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import com.bluecoder.cloudcastle.data.repos.UsersRepo
import com.bluecoder.cloudcastle.ui.screens.ScreenArgs
import com.bluecoder.cloudcastle.ui.screens.auth.AuthViewModel
import com.bluecoder.cloudcastle.ui.screens.content.ContentScreenViewModel
import com.bluecoder.cloudcastle.ui.screens.main.MainViewModel
import com.bluecoder.cloudcastle.utils.CacheManager
import com.bluecoder.cloudcastle.utils.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
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
        val serverAPI = mockk<ServerAPI>()
        stubApi(serverAPI)
        return serverAPI
    }

    private fun stubApi(serverAPI: ServerAPI) {
        coEvery { serverAPI.signup(validUser) } returns buildResponse(
            isResponseSuccessful = true,
            response = UserJWT(validToken)
        )

        coEvery { serverAPI.login(validUser) } returns buildResponse(
            isResponseSuccessful = true,
            response = UserJWT(validToken)
        )

        coEvery { serverAPI.getFiles(photosFileType) } returns buildResponse(
            isResponseSuccessful = true,
            response = fakeFilesData.filter { it.fileType.contains("image") || it.fileType.contains("video") }
        )

        coEvery { serverAPI.getFiles(documentsFileType) } returns buildResponse(
            isResponseSuccessful = true,
            response = fakeFilesData.filter { it.fileType.contains(documentsFileType) }
        )

        coEvery { serverAPI.getFiles(audiosFileType) } returns buildResponse(
            isResponseSuccessful = true,
            response = fakeFilesData.filter { it.fileType.contains(audiosFileType) }
        )

        coEvery { serverAPI.getFilesCountByType("image") } returns buildResponse(
            isResponseSuccessful = true,
            response = FilesCount(fakeFilesData.filter { it.fileType.contains("image") }.size)
        )

        coEvery { serverAPI.getFilesCountByType("video") } returns buildResponse(
            isResponseSuccessful = true,
            response = FilesCount(fakeFilesData.filter { it.fileType.contains("video") }.size)
        )

        coEvery { serverAPI.getFilesCountByType("application") } returns buildResponse(
            isResponseSuccessful = true,
            response = FilesCount(fakeFilesData.filter { it.fileType.contains("application") }.size)
        )

        coEvery { serverAPI.getFilesCountByType("audio") } returns buildResponse(
            isResponseSuccessful = true,
            response = FilesCount(fakeFilesData.filter { it.fileType.contains("audio") }.size)
        )
    }

    @Singleton
    @Provides
    fun provideCacheManager(): CacheManager {
        return CacheManager()
    }

    @Singleton
    @Provides
    fun provideFilesRepo(serverAPI: ServerAPI,cacheManager: CacheManager): FilesRepo {
        return FilesRepo(serverAPI, cacheManager)
    }

    @Singleton
    @Provides
    fun provideUsersRepo(serverAPI: ServerAPI): UsersRepo{
        return UsersRepo(serverAPI)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }

    @Singleton
    @Provides
    fun provideAuthViewModel(usersRepo: UsersRepo,sharedPreferencesManager: SharedPreferencesManager): AuthViewModel {
        return AuthViewModel(usersRepo,sharedPreferencesManager)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
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