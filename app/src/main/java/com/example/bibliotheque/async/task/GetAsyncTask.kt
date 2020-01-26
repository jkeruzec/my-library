package com.example.bibliotheque.async.task

import android.os.AsyncTask
import androidx.core.content.contentValuesOf
import com.example.bibliotheque.async.task.bo.RequestBO
import com.example.bibliotheque.async.task.bo.ResponseBO
import com.example.bibliotheque.utils.ConnectionUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class GetAsyncTask: AsyncTask<RequestBO, Void, ResponseBO>() {

    override fun doInBackground(vararg requestBO: RequestBO): ResponseBO {

        val responseBO : ResponseBO = ResponseBO()

        val getURL = URL(requestBO.first().baseURL + requestBO.first().specificEntryPoint)

        with(getURL.openConnection() as HttpURLConnection) {

            requestBO.first().cookies.forEach { cookie ->
                addRequestProperty(
                    ConnectionUtils.cookie, cookie
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

                responseBO.content = response.toString()
                responseBO.cookies = requestBO.first().cookies
                return responseBO
            }

        }
    }

}