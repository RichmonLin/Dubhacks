package com.simonsays.dangerzone

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object DataProvider{
    fun getData(completion: (String) -> Unit) {
        val url =
            "https://api.spotcrime.com/crimes.json?lat=47.6614244&lon=-122.2683743&radius=1&key=heythisisforpublicspotcrime.comuse-forcommercial-or-research-use-call-877.410.1607-or-email-pyrrhus-at-spotcrime.com"
        val obj = URL(url)

        url.httpGet().responseString{request,response,result->
            when(result){
                is Result.Failure ->{
                    val ex= result.getException()
                }
                is Result.Success -> {
                    val data = result.get()
                }
            }
        }
    }
}