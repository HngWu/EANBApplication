package com.example.eanbapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.example.eanbapplication.Models.RequestedItem
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import androidx.compose.ui.unit.dp
import com.example.eanbapplication.Ultilities.httpgetrequestsservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_requests, container, false)
        view.findViewById<ComposeView>(R.id.compose_view2).setContent {
            RequestsScreen(requireContext())
        }
        return view
    }

    @Composable
    fun RequestsScreen(context: Context) {
        var requestedItems by remember { mutableStateOf<List<RequestedItem>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()
        var httpservice = remember{httpgetrequestsservice(context)}


        LaunchedEffect(Unit) {
            val fetchedItems = withContext(Dispatchers.IO) {
                httpservice.fetchRequests()// Replace with actual userId
            }
            requestedItems = fetchedItems ?: emptyList()
            isLoading = false

        }
        if (isLoading) {
            Text(text = "Loading...", modifier = androidx.compose.ui.Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(requestedItems) { item ->
                    RequestedItemView(requestedItem = item)
                }
            }
        }


//        if (isOffline) {
//            Column {
//                Text("Device is offline. Please reload the page.")
//                Button(onClick = { isOffline = false }) {
//                    Text("Reload")
//                }
//            }
//        } else {
//        }
    }
    @Composable
    fun RequestedItemView(requestedItem: RequestedItem) {
        val startTime = requestedItem.startDate
        val endTime = requestedItem.endDate
        val timespan = "$startTime - $endTime"

        Column() {
            Text(text = "Item: ${requestedItem.name}")
            Text(text = "Amount: ${requestedItem.amount}")
            Text(text = "Timespan: $timespan")
            Text(text = "Fulfilled: ${requestedItem.isFulfilled}")
        }
    }
    @Composable
    fun RequestedItemsList(requestedItems: List<RequestedItem>) {
        LazyColumn {
            items(requestedItems) { item ->
                RequestedItemView(requestedItem = item)
            }
        }
    }


}