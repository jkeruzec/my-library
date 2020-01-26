package com.example.bibliotheque.bo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
class CardBO {

    var id : String = ""

    @Json(name = "expired_at")
    var expireDate: Date? = null

}