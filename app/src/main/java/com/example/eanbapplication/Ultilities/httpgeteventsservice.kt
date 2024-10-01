package com.example.eanbapplication.Ultilities

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.eanbapplication.Models.Event
import com.example.eanbapplication.Models.RequestedItem
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpGetEventsService(private val context: Context) {

    fun fetchEvents(): MutableList<Event>? {
        try {
            val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            val userJSONString = sp.getString("User", null)
            val userJson = JSONArray(userJSONString).getJSONObject(0)
            val intUserId = userJson.optInt("userId")
            Log.d("userid", intUserId.toString())

            val url = URL("http://10.0.2.2:5047/api/EANB/Events/$intUserId")
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
                val eventsList = mutableListOf<Event>()
                for (i in 0 until jSONArray.length()) {
                    val event = jSONArray.getJSONObject(i)
                    val eventId = event.optInt("event_id")
                    val userId = event.optInt("user_id")
                    val locationId = event.optInt("location_id")
                    val name = event.optString("name")
                    val start = event.optString("start")
                    val end = event.optString("end")
                    val requestedItems = mutableListOf<RequestedItem>()
                    val requestedItemsArray = event.optJSONArray("requestedItems")
                    if (requestedItemsArray != null) {
                        for (j in 0 until requestedItemsArray.length()) {
                            val requestedItem = requestedItemsArray.getJSONObject(j)
                            val requestedItemId = requestedItem.optInt("requestedItemId")
                            val requestedItemName = requestedItem.optString("name")
                            val requestedItemAmount = requestedItem.optInt("amount")
                            val requestedItemStartDate = requestedItem.optString("startDate")
                            val requestedItemEndDate = requestedItem.optString("endDate")
                            val isFulfilled = requestedItem.optBoolean("isFulfilled")

                            val newRequestedItem = RequestedItem(
                                requestedItemId, eventId, requestedItemName, requestedItemAmount,
                                requestedItemStartDate, requestedItemEndDate, isFulfilled
                            )
                            requestedItems.add(newRequestedItem)
                        }
                    }
                    val newEvent = Event(eventId, userId, locationId, name, start, end, requestedItems)
                    eventsList.add(newEvent)
                }
                con.disconnect()
                return eventsList
            }
            con.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
