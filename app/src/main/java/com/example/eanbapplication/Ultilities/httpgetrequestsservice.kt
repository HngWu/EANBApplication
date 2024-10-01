package com.example.eanbapplication.Ultilities

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.eanbapplication.Models.RequestedItem
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class httpgetrequestsservice(private val context: Context) {

    fun fetchRequests(): MutableList<RequestedItem>? {
        try {
            val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            val userJSONString = sp.getString("User", null)
            val userJson = JSONArray(userJSONString).getJSONObject(0)
            val intUserId = userJson.optInt("userId")
            Log.d("userid", intUserId.toString())

            val url = URL("http://10.0.2.2:5047/api/EANB/RequestedItems/$intUserId")
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
                val itemsList = mutableListOf<RequestedItem>()
                for (i in 0 until jSONArray.length()) {
                    val item = jSONArray.getJSONObject(i)
                    val requestedItemId = item.optInt("requested_item_id")
                    val eventId = item.optInt("event_id")
                    val name = item.optString("name")
                    val amount = item.optInt("amount")
                    val startDate = item.optString("startDate")
                    val endDate = item.optString("endDate")
                    val isFulfilled = item.optBoolean("isFulfilled")

                    val newRequestedItem = RequestedItem(
                        requested_item_id = requestedItemId,
                        event_id = eventId,
                        name = name,
                        amount = amount,
                        startDate = startDate,
                        endDate = endDate,
                        isFulfilled = isFulfilled
                    )
                    itemsList.add(newRequestedItem)
                }
                con.disconnect()
                return itemsList
            }
            con.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

