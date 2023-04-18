package com.bluecoder.cloudcastle.core.repos.files

import android.util.Log
import com.bluecoder.cloudcastle.core.api.ServerAPI
import com.bluecoder.cloudcastle.core.data.FileItem
import com.bluecoder.cloudcastle.utils.FileType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class DefaultFilesRepoTest{

    @Mock
    private lateinit var serverAPI: ServerAPI

    private lateinit var defaultFilesRepo: DefaultFilesRepo

    private val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE0MTgzMjEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgxNDE2MjIxfQ.FrF6qw730u1SMAIWy0PsY4YzxJG-a-UWPBpwQuvO0_U"
    private val expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODAxOTkyNDEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgwMTk3MTQxfQ.YmJDreTJQxM7VBEXr8tjLCYhY0PH0DosHWjexM2fq78"
    private val invalidToken = "eyJhbGciOiUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODAxOTkyNDEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgwMTk3MTQxfQ.YmJDreTJQxM7VBEXr8tjLCYhY0PH0DosHWjexM2fq78"

    private val itemsList = listOf(
        FileItem(0,"IMG_1232.png","1678120444436_IMG_1232.png","image/png",197220,1678120444500,null),
        FileItem(1,"IMG_1232.png","1678120444436_IMG_1232.png","image/png",197220,1678120444500,null),
        )

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        defaultFilesRepo = DefaultFilesRepo(serverAPI)
    }

    @Test
    fun `getFiles with success response`(): Unit = runBlocking {
        val response = Response.success(itemsList)

        `when`(serverAPI.getFiles(validToken)).thenReturn(response)

        val results = defaultFilesRepo.getFiles(validToken)
        results.collect{
            assertTrue(it.isSuccess)
            assertEquals(itemsList,it.getOrNull())
        }
    }

    @Test
    fun `getFiles with expired token`(): Unit = runBlocking {
        val msg = "token expired, You must login again to have a new valid token"
        val errorBody = ResponseBody.create(MediaType.parse("application/json"),msg)
        val response = Response.error<List<FileItem>>(401,errorBody)

        `when`(serverAPI.getFiles(expiredToken)).thenReturn(response)


        val results = defaultFilesRepo.getFiles(expiredToken)
        results.collect{
            assertTrue(it.isFailure)
            assertEquals(msg,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `getFiles with invalid token`(): Unit = runBlocking {
        val msg = "token is invalid, You must login again to have a new valid token"
        val errorBody = ResponseBody.create(MediaType.parse("application/json"),msg)
        val response = Response.error<List<FileItem>>(401,errorBody)

        `when`(serverAPI.getFiles(invalidToken)).thenReturn(response)


        val results = defaultFilesRepo.getFiles(invalidToken)
        results.collect{
            assertTrue(it.isFailure)
            assertEquals(msg,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `getFileById with success response`(): Unit = runBlocking {

        val id = 0
        val response = Response.success(itemsList.find { it.id == id })

        `when`(serverAPI.getFileById(validToken,id)).thenReturn(response)

        val results = defaultFilesRepo.getFileById(validToken,id)
        results.collect{
            assertTrue(it.isSuccess)
            assertEquals(itemsList.first(),it.getOrNull())
        }
    }

    @Test
    fun `getFileById with invalid id`(): Unit = runBlocking {
        val msg = "File not found"
        val errorBody = ResponseBody.create(MediaType.parse("application/json"),msg)
        val response = Response.error<FileItem>(401,errorBody)

        `when`(serverAPI.getFileById(validToken,3)).thenReturn(response)

        val results = defaultFilesRepo.getFileById(validToken,3)
        results.collect{
            assertTrue(it.isFailure)
            assertEquals(msg,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `deleteFileById with success response`(): Unit = runBlocking {
        val response = Response.success("")

        `when`(serverAPI.deleteFileById(validToken,0)).thenReturn(response)

        val results = defaultFilesRepo.deleteFileById(validToken,0)
        results.collect{
            assertTrue(it.isSuccess)
        }
    }

    @Test
    fun `deleteFileById with invalid id`(): Unit = runBlocking {
        val msg = "File not found"
        val errorBody = ResponseBody.create(MediaType.parse("application/json"),msg)
        val response = Response.error<String>(401,errorBody)

        `when`(serverAPI.deleteFileById(validToken,3)).thenReturn(response)

        val results = defaultFilesRepo.deleteFileById(validToken,3)
        results.collect{
            assertTrue(it.isFailure)
            assertEquals(msg,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `deleteFiles with success response`(): Unit = runBlocking {
        val response = Response.success("")

        `when`(serverAPI.deleteFiles(validToken,itemsList)).thenReturn(response)

        val results = defaultFilesRepo.deleteFiles(validToken,itemsList)
        results.collect{
            assertTrue(it.isSuccess)
        }
    }

}