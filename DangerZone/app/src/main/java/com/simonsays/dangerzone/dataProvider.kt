package com.simonsays.dangerzone


import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.json.JSONObject

object DataProvider{
    fun getData(completion: (JSONObject) -> Unit) {
        val url =
            "https://api.spotcrime.com/crimes.json?lat=47.6614244&lon=-122.2683743&radius=0.05&key=heythisisforpublicspotcrime.comuse-forcommercial-or-research-use-call-877.410.1607-or-email-pyrrhus-at-spotcrime.com"

        url.httpGet().responseJson{request,response,result->
            when(result){
                is Result.Failure ->{
                    val ex= result.getException()
                }
                is Result.Success -> {
                    val data = result.get().obj()
                    completion(data)
                }
            }
        }
    }
}