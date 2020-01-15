package com.example.bibliotheque.services

import com.example.bibliotheque.bo.ConnectionInformationBO

interface LibraryService {

    fun authenticate(login: String, password: String) : ConnectionInformationBO

    fun getLoans(connectionInformationBO: ConnectionInformationBO)

}