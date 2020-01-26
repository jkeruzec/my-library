package com.example.bibliotheque.bo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecordBO(val id: Int, val thumbnail: String) {

}