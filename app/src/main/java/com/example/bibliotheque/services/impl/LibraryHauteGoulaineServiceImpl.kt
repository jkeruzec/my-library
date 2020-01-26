package com.example.bibliotheque.services.impl

import com.example.bibliotheque.async.task.GetAsyncTask
import com.example.bibliotheque.async.task.PostWithFollowAsyncTask
import com.example.bibliotheque.async.task.bo.RequestBO
import com.example.bibliotheque.async.task.bo.ResponseBO
import com.example.bibliotheque.bo.AccountBO
import com.example.bibliotheque.bo.ConnectionInformationBO
import com.example.bibliotheque.bo.LoansBO
import com.example.bibliotheque.services.LibraryService
import com.example.bibliotheque.utils.ConnectionUtils
import com.squareup.moshi.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.ParameterizedType
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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
        val logoutURL: String = "/auth/logout"
        val accountURL : String = "/api/user/account"
        var customDateAdapter: Any = object : Any() {
            var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            @ToJson
            @Synchronized
            fun dateToJson(d: Date?): String? {
                return dateFormat.format(d)
            }

            @FromJson
            @Synchronized
            @Throws(ParseException::class)
            fun dateToJson(s: String?): Date? {
                return dateFormat.parse(s)
            }
        }
    }

    override fun authenticate(login: String, password: String): ConnectionInformationBO {

        val params: MutableList<String> = mutableListOf<String>()
        params.add(ConnectionUtils.createparam("username", login))
        params.add(ConnectionUtils.createparam("password", password))
        params.add(ConnectionUtils.createparam("redirect", baseURL))

        val postWithFollowAsyncTaskwAsync : PostWithFollowAsyncTask = PostWithFollowAsyncTask()
        val responseBO : ResponseBO? = postWithFollowAsyncTaskwAsync.execute(baseURL, loginURl, params.joinToString(separator = "&")).get()

        val authentication : ConnectionInformationBO = ConnectionInformationBO()
        authentication.username = login
        if(responseBO != null) {
            if(responseBO.content.contains(unsucccessfullConnection, true)) {
                authentication.cookies = responseBO.cookies
            } else if (responseBO.content.contains(successfullConnection, true)){
                authentication.authenticated = true
                authentication.cookies = responseBO.cookies
            }
        }

        return authentication
    }

    override fun logout(connectionInformationBO: ConnectionInformationBO) {

        if(connectionInformationBO.isAuthenticated()) {
            val hauteGoulaineLogoutURL = URL(baseURL + logoutURL)
            with(hauteGoulaineLogoutURL.openConnection() as HttpURLConnection) {
                connectionInformationBO.cookies.forEach { cookie ->
                    addRequestProperty(
                        ConnectionUtils.cookie,
                        cookie
                    )
                }
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                }
            }
        }
    }

    override fun getLoans(connectionInformationBO: ConnectionInformationBO) : List<LoansBO>? {
        if(connectionInformationBO.isAuthenticated()) {
            val hauteGoulaineLoansURL = URL(baseURL + loansURL)
            with(hauteGoulaineLoansURL.openConnection() as HttpURLConnection) {
                connectionInformationBO.cookies.forEach { cookie -> addRequestProperty(ConnectionUtils.cookie, cookie) }
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()

                    // We have all loans for the authenticated user
                    // remove root object from string Why do you do that ???
                    // Parse and return BO
                    val moshiBuilder: Moshi = Moshi.Builder().add(customDateAdapter).build()
                    val listType: ParameterizedType = Types.newParameterizedType(List::class.java, LoansBO::class.java)
                    val type: ParameterizedType = Types.newParameterizedType(Map::class.java, String::class.java, listType)
                    val adapter: JsonAdapter<Map<String, List<LoansBO>>> = moshiBuilder.adapter(type)
                    val loansBOs: Map<String, List<LoansBO>>? = adapter.fromJson(response.toString())
                    if(loansBOs != null) {
                        return loansBOs.get("loans")
                    }
                    return listOf()
                }
            }
        }
        return listOf()
    }

    override fun getAccountInformation(connectionInformationBO: ConnectionInformationBO): AccountBO? {

        var accountBO : AccountBO? = null

        if(connectionInformationBO.isAuthenticated()) {

            val getAsyncTask : GetAsyncTask = GetAsyncTask()

            val requestBO : RequestBO = RequestBO(baseURL, accountURL, connectionInformationBO.cookies)
            val responseBO : ResponseBO = getAsyncTask.execute(requestBO).get()

            if(responseBO.content.isNotEmpty()) {
                // remove root object from string Why do you do that ???
                // Parse and return BO
                val moshiBuilder: Moshi = Moshi.Builder().add(customDateAdapter).build()
                val type: ParameterizedType = Types.newParameterizedType(
                    Map::class.java,
                    String::class.java,
                    AccountBO::class.java
                )
                val adapter: JsonAdapter<Map<String, AccountBO>> = moshiBuilder.adapter(type)
                val accountBOMap: Map<String, AccountBO>? =
                    adapter.fromJson(responseBO.content)//response.toString())
                if (accountBOMap != null) {
                    accountBO = accountBOMap.get("account")
                }
            }
            return accountBO

        }
        return accountBO
    }

}