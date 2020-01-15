package com.example.bibliotheque.utils

import java.net.URLEncoder

class ConnectionUtils {

    companion object {

        val defaultEncoding: String = "UTF-8"
        val cookieHeader: String = "Set-Cookie"
        val cookie: String = "Cookie"
        val location: String = "Location"

        fun createparam(key: String, value: String): String {
            if (key.isNotBlank() && value.isNotBlank()) {
                return URLEncoder.encode(key, defaultEncoding) + "=" + URLEncoder.encode(
                    value,
                    defaultEncoding
                )
            }
            return ""
        }
    }

}