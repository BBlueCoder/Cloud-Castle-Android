package com.bluecoder.cloudcastle

import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.data.pojoclasses.FilesCount
import com.bluecoder.cloudcastle.utils.Utils.getTimeInMillis
import java.util.concurrent.TimeUnit

data class CachedItem(
    val time : Long,
    val data : HashMap<Int, FileItem>
)

data class CachedFilesCount(
    val time: Long,
    val filesCount: FilesCount
)
class CacheManager {

    private val cachedResponse = HashMap<Int, CachedItem>()
    private val cacheSingleValues = HashMap<Int, CachedFilesCount>()

    fun cacheResponse(call : String,data : List<FileItem>){
        cachedResponse[call.hashCode()] = CachedItem(getTimeInMillis(), HashMap(data.associateBy { it.id }))
    }

    fun cacheResponse(call : String,count : Int){
        cacheSingleValues[call.hashCode()] = CachedFilesCount(getTimeInMillis(), FilesCount(count))
    }

    fun getCachedResponse(call : String): List<FileItem>? {
        cachedResponse[call.hashCode()]?.let {
            if(!isCacheExpired(it.time))
                return it.data.values.toList()
        }
        return null
    }

    fun getFileItemFromCache(id: Int): FileItem? {
        cachedResponse.keys.forEach {
            if(!isCacheExpired(cachedResponse[it]!!.time)){
                cachedResponse[it]?.let { map ->
                    return map.data[id]
                }
            }
        }
        return null
    }

    fun getSingleCachedValue(call : String): FilesCount? {
        cacheSingleValues[call.hashCode()]?.let {
            if(!isCacheExpired(it.time))
                return it.filesCount
        }
        return null
    }

    fun clearAllCache(){
        cacheSingleValues.clear()
        cachedResponse.clear()
    }

    private fun isCacheExpired(cacheTime : Long): Boolean {
        return getTimeInMillis() - cacheTime > TimeUnit.MINUTES.toMillis(5)
    }
}