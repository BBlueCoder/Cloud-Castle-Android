package com.bluecoder.cloudcastle.core.repos.files

import com.bluecoder.cloudcastle.CacheManager
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.data.pojoclasses.FilesCount
import com.bluecoder.cloudcastle.core.repos.buildResponse
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import com.bluecoder.cloudcastle.utils.Utils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class FilesRepoTest{

    private lateinit var serverAPI: ServerAPI
    private lateinit var cacheManager: CacheManager

    private lateinit var filesRepo: FilesRepo

    private val fileType = "image"

    private val itemsList = listOf(
        FileItem(1,"IMG_1232.png","1678120444436_IMG_1232.png","image/png",197220,1678120444500,null),
        FileItem(2,"IMG_1232.png","1678120444436_IMG_1232.png","image/png",197220,1678120444500,null),
        )

    @Before
    fun setup(){
        serverAPI = mockk()
        cacheManager = spyk(CacheManager())
        stubApi()
        filesRepo = FilesRepo(serverAPI,cacheManager)
    }

    private fun stubApi(){
        coEvery { serverAPI.getFiles(fileType) } returns buildResponse(
            isResponseSuccessful = true,
            response = itemsList
        )

        coEvery { serverAPI.getFiles("invalid") } returns buildResponse(
            isResponseSuccessful = false,
            errorResponseMessage = "Invalid request"
        )

        coEvery { serverAPI.getFilesCountByType(fileType) } returns buildResponse(
            isResponseSuccessful = true,
            response = FilesCount(2)
        )

        coEvery { serverAPI.getFileById(itemsList.first().id) } returns buildResponse(
            isResponseSuccessful = true,
            response = itemsList.first()
        )

        coEvery { serverAPI.getFileById(0) } returns buildResponse(
            isResponseSuccessful = false,
            errorResponseMessage = "Invalid id"
        )

    }

    @Test
    fun `getFiles with success response and cache response`(): Unit = runBlocking {
        assertNull(cacheManager.getCachedResponse("${FilesRepo.GET_FILES_CALL}$fileType"))

        val results = filesRepo.getFiles(fileType)
        results.collect{
            assertTrue(it.isSuccess)
            assertEquals(itemsList,it.getOrNull())
        }

        verify {
            cacheManager.cacheResponse("${FilesRepo.GET_FILES_CALL}$fileType",itemsList)
        }

        assertEquals(cacheManager.getCachedResponse("${FilesRepo.GET_FILES_CALL}$fileType"),itemsList)
    }

    @Test
    fun `getFiles from cache`(): Unit = runBlocking {
        cacheManager.cacheResponse("${FilesRepo.GET_FILES_CALL}$fileType",itemsList)

        val results = filesRepo.getFiles(fileType)
        results.collect {
            assertTrue(it.isSuccess)
            assertEquals(itemsList,it.getOrNull())
        }

        coVerify(exactly = 0) {
            serverAPI.getFiles(fileType)
        }
    }

    @Test
    fun `getFiles from expired cache`() : Unit = runBlocking {
        cacheManager.cacheResponse("${FilesRepo.GET_FILES_CALL}$fileType",itemsList)

        val expiredTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(6)

        mockkObject(Utils)
        every { Utils.getTimeInMillis() } returns expiredTime
        
        val results = filesRepo.getFiles(fileType)
        results.collect {
            assertTrue(it.isSuccess)
            assertEquals(itemsList,it.getOrNull())
        }

        coVerify(exactly = 1) { serverAPI.getFiles(fileType) }

    }

    @Test
    fun `getFiles with failed response`() : Unit = runBlocking {
        val result = filesRepo.getFiles("invalid")
        result.collect {
            assertTrue(it.isFailure)
        }
    }

    @Test
    fun `getFilesCountByType with success response and cache response`() : Unit = runBlocking {
        val results = filesRepo.getFilesCountByType(fileType)
        results.collect{
            assertTrue(it.isSuccess)
            assertEquals(2,it.getOrNull()!!.count)
        }

        verify {
            cacheManager.cacheResponse("${FilesRepo.GET_FILES_COUNT}$fileType",2)
        }

        assertEquals(cacheManager.getSingleCachedValue("${FilesRepo.GET_FILES_COUNT}$fileType")!!.count,2)
    }

    @Test
    fun `getFilesCountByType from cache`() : Unit = runBlocking {
        cacheManager.cacheResponse("${FilesRepo.GET_FILES_COUNT}$fileType",2)

        val result = filesRepo.getFilesCountByType(fileType)
        result.collect{
            assertTrue(it.isSuccess)
            assertEquals(2,it.getOrNull()!!.count)
        }

        coVerify(exactly = 0){ serverAPI.getFilesCountByType(fileType) }
    }

    @Test
    fun `getFileById with success response`(): Unit = runBlocking {

        val results = filesRepo.getFileById(itemsList.first().id)
        results.let{
            assertTrue(it.isSuccess)
            assertEquals(itemsList.first(),it.getOrNull())
        }
    }

    @Test
    fun `getFileById from cache`(): Unit = runBlocking {

        filesRepo.getFiles(fileType).collect{

        }

        val results = filesRepo.getFileById(itemsList.first().id)
        results.let{
            assertTrue(it.isSuccess)
            assertEquals(itemsList.first(),it.getOrNull())
        }

        coVerify(exactly = 0) { serverAPI.getFileById(itemsList.first().id) }
    }

    @Test
    fun `getFileById with invalid id`() : Unit = runBlocking {
        val result = filesRepo.getFileById(0)
        assertTrue(result.isFailure)
    }
}