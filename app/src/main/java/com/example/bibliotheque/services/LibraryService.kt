package com.example.bibliotheque.services

import com.example.bibliotheque.bo.AccountBO
import com.example.bibliotheque.bo.ConnectionInformationBO
import com.example.bibliotheque.bo.LoansBO

interface LibraryService {

    /**
     * Authenticate to library website API
     */
    fun authenticate(login: String, password: String) : ConnectionInformationBO

    /**
     * Test redirect logout is successfull
     */
    fun logout(connectionInformationBO: ConnectionInformationBO)

    fun getLoans(connectionInformationBO: ConnectionInformationBO) : List<LoansBO>?

    /**
     * Get account information for current user
     * TODO: Parse full name for Haute Goulaine and/or Pull Request to bokeh
     * @return AccountBO for current user
     */
    fun getAccountInformation(connectionInformationBO: ConnectionInformationBO): AccountBO?

}