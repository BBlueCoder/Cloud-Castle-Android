package com.bluecoder.cloudcastle.utils

object Constants {
    const val API_BASE_URL = "http://192.168.100.80:3000/api/"
    const val API_THUMBNAIL_URL = "${API_BASE_URL}files/thumbnail/"
    const val API_FILES_URL = "${API_BASE_URL}files/"

    const val FILES_TYPE_IMAGES = "images"
    const val FILES_TYPE_VIDEOS = "videos"
    const val FILES_TYPE_AUDIOS = "audios"
    const val FILES_TYPE_OTHERS = "others"
}