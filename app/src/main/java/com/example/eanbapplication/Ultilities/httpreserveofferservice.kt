package com.example.eanbapplication.Ultilities

import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import com.example.eanbapplication.EventsFragment
import com.example.eanbapplication.LoginFragment
import com.example.eanbapplication.Models.AcceptOffer
import com.example.eanbapplication.Models.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class httpreserveofferservice(private var context: EventsFragment, private var acceptOffer: AcceptOffer,    private var onSuccess: () -> Unit
) : AsyncTask<Void, Void, Boolean>(){
    override fun doInBackground(vararg params: Void?): Boolean? {
        try{
            val url = URL("http://10.0.2.2:5047/api/EANB/Offers/Reserve")
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type", "application/json; utf-8")
            con.setRequestProperty("Accept", "application/json")



            val json = Json.encodeToString(acceptOffer)

            con.doOutput = true
            val os = OutputStreamWriter(con.outputStream)
            os.write(json)
            os.flush()
            os.close()
            val status = con.responseCode
            Log.d("Status", status.toString())
            if (status==200)
            {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                val result = reader.readLines()
//                val sharedPref = PreferenceManager.getDefaultSharedPreferences(context.context?.applicationContext)
//                val editor = sharedPref.edit()
//                editor.putString("User", result.toString())
//                editor.commit()
                return true
            }

            con.disconnect()

            if(status==200) {
                Log.d("votingapp", "True")
                return true
            }
            else
            {
                Log.d("votingapp", "false")

                return false;
            }


        }catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }
    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (result==true)
        {
            context.offerSuccess()
            onSuccess()

        }
        else
        {
            context.offerFailed()
        }
    }

}