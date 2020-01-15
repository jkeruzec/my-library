package com.example.bibliotheque.bo

class ConnectionInformationBO {

    var username : String = ""
    var cookies : List<String> = listOf()
    var authenticated : Boolean = false

    fun isAuthenticated(): Boolean {
        return authenticated && username.isNotBlank() && cookies.isNotEmpty()
    }

}