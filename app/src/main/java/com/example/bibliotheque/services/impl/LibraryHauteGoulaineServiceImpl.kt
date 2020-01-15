package com.example.bibliotheque.services.impl

import com.example.bibliotheque.bo.ConnectionInformationBO
import com.example.bibliotheque.services.LibraryService
import com.example.bibliotheque.utils.ConnectionUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Global access for all services to Haute Goulaine library
 * TODO: Create seperate store for each type of object in future
 * TODO : Async calls :)
 */
class LibraryHauteGoulaineServiceImpl : LibraryService {

    companion object {
        val unsucccessfullConnection:String = "Identifiant ou mot de passe incorrect."
        val successfullConnection:String = "me deconnecter"
        val loginURl : String = "/auth/login"
        val baseURL : String = "http://www.bibliothequehautegoulaine.net"
        val loansURL : String = "/api/user/loans"
    }

    override fun authenticate(login: String, password: String): ConnectionInformationBO {

        val authentication : ConnectionInformationBO =
            ConnectionInformationBO()
        authentication.username = login

        val hauteGoulaineLoginURL: URL = URL(baseURL + loginURl)
        val params: MutableList<String> = mutableListOf<String>()
        params.add(ConnectionUtils.createparam("username", login))
        params.add(ConnectionUtils.createparam("password", password))
        params.add(ConnectionUtils.createparam("redirect", baseURL))

        with(hauteGoulaineLoginURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"
            doOutput = true
            instanceFollowRedirects = false
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val wr = OutputStreamWriter(getOutputStream());
            var paramsString = params.joinToString(separator = "&")
            wr.write(paramsString);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")

            var cookies:List<String>? = headerFields[ConnectionUtils.cookieHeader]
            if(cookies != null) {
                println(cookies.joinToString())
            }

            var redirect = false

            // normally, 3xx is redirect
            // normally, 3xx is redirect
            if (responseCode != HttpURLConnection.HTTP_OK) {
                if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER
                ) redirect = true
            }
            println("Redirect : $redirect")

            if(redirect && cookies != null && cookies.isNotEmpty()) {
                val location: String? = getHeaderField(ConnectionUtils.location)
                println("New location : $location")
                val newFollowLocationUrl: URL  = URL(location)
                with(newFollowLocationUrl.openConnection() as HttpURLConnection) {
                    cookies.forEach { cookie -> setRequestProperty(ConnectionUtils.cookie, cookie) }
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        println("Response : $response")

                        if(response.contains(unsucccessfullConnection, true)) {
                            authentication.cookies = cookies
                            return authentication
                        } else if (response.contains(successfullConnection, true)){
                            authentication.authenticated = true
                            authentication.cookies = cookies
                            return authentication
                        }

                    }
                }

            } else {
                // Problem with connection to website
                println("Connection problem")
            }

        }

        return authentication

    }

    override fun getLoans(connectionInformationBO: ConnectionInformationBO) {

        if(connectionInformationBO.isAuthenticated()) {
            val hauteGoulaineLoansURL = URL(baseURL + loansURL)
            with(hauteGoulaineLoansURL.openConnection() as HttpURLConnection) {
                connectionInformationBO.cookies.forEach { cookie -> setRequestProperty(ConnectionUtils.cookie, cookie) }
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    println("Response : $response")
                }
            }
        }

    }

}