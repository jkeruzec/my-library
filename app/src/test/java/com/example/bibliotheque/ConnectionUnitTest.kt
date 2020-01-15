package com.example.bibliotheque

import com.example.bibliotheque.bo.ConnectionInformationBO
import com.example.bibliotheque.services.LibraryService
import com.example.bibliotheque.services.impl.LibraryHauteGoulaineServiceImpl
import com.example.bibliotheque.utils.ConnectionUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ConnectionUnitTest {

    @Test
    fun createParams_isCorrect() {
        val params: MutableList<String> = mutableListOf<String>()
        params.add(ConnectionUtils.createparam("login", "login"))
        params.add(ConnectionUtils.createparam("password", "pass"))
        assertEquals("login=login&password=pass", params.joinToString(separator = "&"))
    }

    @Test
    fun successfulConnectToHauteGoulaineLibrary() {
        val connectionService: LibraryService = LibraryHauteGoulaineServiceImpl()
        val authenticationInformation: ConnectionInformationBO = connectionService.authenticate("A00005821", "1985")
        assertEquals("A00005821", authenticationInformation.username)
        assertTrue(authenticationInformation.authenticated)
        assertTrue(authenticationInformation.cookies.isNotEmpty())
    }

    @Test
    fun unSuccessfulConnectToHauteGoulaineLibrary() {
        val connectionService: LibraryService = LibraryHauteGoulaineServiceImpl()
        val authenticationInformation: ConnectionInformationBO = connectionService.authenticate("test", "test")
        assertEquals("test", authenticationInformation.username)
        assertFalse(authenticationInformation.authenticated)
        assertTrue(authenticationInformation.cookies.isNotEmpty())
    }

    @Test
    fun readLoans() {
        val connectionService: LibraryService = LibraryHauteGoulaineServiceImpl()
        val authenticationInformation: ConnectionInformationBO = connectionService.authenticate("A00005821", "1985")
        connectionService.getLoans(authenticationInformation)

    }
}
