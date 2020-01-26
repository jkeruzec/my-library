package com.example.bibliotheque.async.task

import android.os.AsyncTask
import com.example.bibliotheque.async.task.bo.ResponseBO
import com.example.bibliotheque.utils.ConnectionUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class PostWithFollowAsyncTask : AsyncTask<String, Void, ResponseBO?>() {

    override fun doInBackground(vararg params: String): ResponseBO? {
        val baseURL : String = params[0]
        val specificEntryPointURL : String = params[1]
        val postParams : String = params[2]

        val postURL: URL = URL(baseURL + specificEntryPointURL)

        with(postURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"
            doOutput = true
            instanceFollowRedirects = false
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(postParams);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")

            var cookies:List<String>? = headerFields[ConnectionUtils.cookieHeader]
            if(cookies != null) {
                println(cookies.joinToString())
            }

            var redirect = false

            // normally, 3xx is redirect
            if (responseCode != HttpURLConnection.HTTP_OK) {
                if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER
                ) redirect = true
            }
            println("Redirect : $redirect")

            if(redirect && cookies != null && cookies.isNotEmpty()) {
                val location: String? = getHeaderField(ConnectionUtils.location)
                println("New location : $location")
                val newFollowLocationUrl: URL = URL(location)
                with(newFollowLocationUrl.openConnection() as HttpURLConnection) {
                    cookies.forEach { cookie -> addRequestProperty(ConnectionUtils.cookie, cookie) }
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        println("Response : $response")
                        var responseBO: ResponseBO =
                            ResponseBO()
                        responseBO.cookies = cookies
                        responseBO.content = response.toString()
                        return responseBO
                    }
                }

            } else {
                // Problem with connection to website
                println("Connection problem")
            }

        }

        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: ResponseBO?) {
        super.onPostExecute(result)
    }
}