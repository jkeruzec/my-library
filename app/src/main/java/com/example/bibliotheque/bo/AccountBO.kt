package com.example.bibliotheque.bo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AccountBO {

    @Json(name = "label")
    var accountName: String = ""

    var login : String = ""

    @Json(name = "card")
    var cardBO : CardBO? = null
}