package com.example.bibliotheque

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.bibliotheque.bo.AccountBO
import com.example.bibliotheque.bo.ConnectionInformationBO
import com.example.bibliotheque.services.LibraryService
import com.example.bibliotheque.services.impl.LibraryHauteGoulaineServiceImpl
import com.example.bibliotheque.utils.ConnectionUtils
import org.hamcrest.core.AnyOf
import org.hamcrest.core.IsEqual
import org.junit.Test

import org.junit.Assert.*
import org.junit.Assume
import org.junit.BeforeClass
import java.io.File
import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ConnectionUnitTest {

    companion object {
        val goodLoginProperty : String = "goodLogin"
        val goodPasswordProperty : String = "goodPassword"
        val wrongLoginProperty : String = "wrongLogin"
        val wrongPasswordProperty : String = "wrongPassword"
        var goodLogin : String = ""
        var goodPassword : String = ""
        var wrongLogin : String = ""
        var wrongPassword : String = ""

        @BeforeClass
        @JvmStatic
        fun initTest() {
            val p : Properties = Properties()
            p.load(FileReader(Paths.get("application-test.properties").toFile()))
            goodLogin = p[goodLoginProperty] as String
            goodPassword = p[goodPasswordProperty] as String
            wrongLogin = p[wrongLoginProperty] as String
            wrongPassword = p[wrongPasswordProperty] as String

            println("Starting test with parameters : ")
            println(goodLogin)
            println(goodPassword)
            println(wrongLogin)
            println(wrongPassword)
            println("Starting tests... ")
        }

    }

    @Test
    fun createParamsIsCorrect() {
        val params: MutableList<String> = mutableListOf<String>()
        params.add(ConnectionUtils.createparam("login", wrongLogin))
        params.add(ConnectionUtils.createparam("password", wrongPassword))
        assertEquals("login=$wrongLogin&password=$wrongPassword", params.joinToString(separator = "&"))
    }

    @Test
    fun successfulConnectToHauteGoulaineLibrary() {
        val connectionService: LibraryService = LibraryHauteGoulaineServiceImpl()
        val authenticationInformation: ConnectionInformationBO = connectionService.authenticate(
            goodLogin, goodPassword)
        assertEquals(goodLogin, authenticationInformation.username)
        assertTrue(authenticationInformation.authenticated)
        assertTrue(authenticationInformation.cookies.isNotEmpty())
    }

    @Test
    fun unSuccessfulConnectToHauteGoulaineLibrary() {
        val connectionService: LibraryService = LibraryHauteGoulaineServiceImpl()
        val authenticationInformation: ConnectionInformationBO = connectionService.authenticate(
            wrongLogin, wrongPassword)
        assertEquals("test", authenticationInformation.username)
        assertFalse(authenticationInformation.authenticated)
        assertTrue(authenticationInformation.cookies.isNotEmpty())
    }

    @Test
    fun readLoans() {
        val connectionService: LibraryService = LibraryHauteGoulaineServiceImpl()
        val authenticationInformation: ConnectionInformationBO = connectionService.authenticate(goodLogin, goodPassword)
        assertNotNull(connectionService.getLoans(authenticationInformation))
    }

    @Test
    fun readAccount() {
        val connectionService: LibraryService = LibraryHauteGoulaineServiceImpl()
        val authenticationInformation: ConnectionInformationBO = connectionService.authenticate(goodLogin, goodPassword)
        val accountBO : AccountBO? = connectionService.getAccountInformation(authenticationInformation)
        assertNotNull(accountBO)
        if(accountBO != null) {
            assertEquals(accountBO.accountName, "Julien")
        }
    }
}
