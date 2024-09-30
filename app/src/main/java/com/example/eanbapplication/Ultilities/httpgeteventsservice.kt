//package com.example.eanbapplication.Ultilities
//
//import android.os.AsyncTask
//import android.preference.PreferenceManager
//import android.util.Log
//import android.widget.Toast
//import com.example.eanbapplication.Models.Event
//import com.example.eanbapplication.Models.RequestedItem
//import com.example.eanbapplication.databinding.FragmentEventsBinding
//import org.json.JSONArray
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//
//class httpgeteventsservice(
//    private var context: EventsFragment,
//    private val binding: FragmentEventsBinding
//) : AsyncTask<Void, Void, MutableList<Event>>() {
//
//    override fun doInBackground(vararg params: Void?): MutableList<Event>? {
//        try {
//            val sp = PreferenceManager.getDefaultSharedPreferences(context.context?.applicationContext ?: null)
//            val userJSONString = sp.getString("User", null)
//            val userJson = JSONArray(userJSONString).getJSONObject(0)
//            val intUserId = userJson.optInt("userId")
//            Log.d("userid", intUserId.toString())
//
//            val url = URL("http://10.0.2.2:5047/api/EANB/Events/$intUserId")
//            val con = url.openConnection() as HttpURLConnection
//            con.requestMethod = "GET"
//            con.setRequestProperty("Content-Type", "application/json; utf-8")
//            con.setRequestProperty("Accept", "application/json")
//
//            val status = con.responseCode
//            if (status == 200) {
//                val reader = BufferedReader(InputStreamReader(con.inputStream))
//                val jsonData = StringBuilder()
//                var line: String?
//                while (reader.readLine().also { line = it } != null) {
//                    jsonData.append(line)
//                }
//                reader.close()
//
//                val jSONArray = JSONArray(jsonData.toString())
//                val eventsList = mutableListOf<Event>()
//                try {
//                    for (i in 0 until jSONArray.length()) {
//                        val event = jSONArray.getJSONObject(i)
//                        val event_id = event.optInt("event_id")
//                        val user_id = event.optInt("user_id")
//                        val location_id = event.optInt("location_id")
//                        val name = event.optString("name")
//                        val start = event.optString("start")
//                        val end = event.optString("end")
//                        val requested_items = mutableListOf<RequestedItem>()
//                        val newEvent = Event(event_id, user_id, location_id, name, start, end, requested_items)
//                        eventsList.add(newEvent)
//                    }
//                    return eventsList
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//            con.disconnect()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    override fun onPostExecute(result: MutableList<Event>?) {
//        super.onPostExecute(result)
//        if (result != null) {
//            // Set up RecyclerView
//            context.onEventsRetrieved(result)
//
//        } else {
//            Toast.makeText(context.requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
//        }
//    }
//}
