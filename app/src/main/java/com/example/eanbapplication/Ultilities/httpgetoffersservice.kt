package com.example.eanbapplication.Ultilities

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.eanbapplication.Models.Event
import com.example.eanbapplication.Models.Offer
import com.example.eanbapplication.Models.RequestedItem
import com.example.eanbapplication.Models.User
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL



class httpgetoffersservice(private val item_name: String) {

    fun fetchOffers(): MutableList<Offer>? {
        try {
//            val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
//            val userJSONString = sp.getString("User", null)
//            val userJson = JSONArray(userJSONString).getJSONObject(0)
//            val intUserId = userJson.optInt("userId")
//            Log.d("userid", intUserId.toString())

            val url = URL("http://10.0.2.2:5047/api/EANB/Offers/$item_name")
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("Content-Type", "application/json; utf-8")
            con.setRequestProperty("Accept", "application/json")

            val status = con.responseCode
            if (status == 200) {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                val jsonData = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    jsonData.append(line)
                }
                reader.close()

                val jSONArray = JSONArray(jsonData.toString())
                val offerList = mutableListOf<Offer>()
                for (i in 0 until jSONArray.length()) {
                    val offer = jSONArray.getJSONObject(i)
                    val offerId = offer.optInt("offerId")
                    val offerUserId = offer.optInt("offerUserId")
                    val requestUserId = offer.optInt("requestUserId")
                    val name = offer.optString("name")
                    val amount = offer.optString("amount")
                    val state = offer.optString("state")
                    val startDate = offer.optString("startDate")
                    val endDate = offer.optString("endDate")



                    val newOffer = Offer(offerId, offerUserId, requestUserId, name, amount, state, startDate, endDate)
                    offerList.add(newOffer)
                }
                con.disconnect()
                return offerList
            }
            con.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

