package com.example.bibliotheque.bo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import java.util.*

@JsonClass(generateAdapter = true)
class LoansBO {

    var id: String = ""
    var title: String = ""
    var author: String = ""
    @Json(name = "date_due")
    var dueDate: Date? = null
    @Json(name = "loaned_by")
    var loanedBy: String = ""
    var library: String = ""
    @Json(name = "record")
    var recordBO: RecordBO? = null

}