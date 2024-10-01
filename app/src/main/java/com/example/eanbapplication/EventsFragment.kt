package com.example.eanbapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.example.eanbapplication.Models.Event
import com.example.eanbapplication.placeholder.PlaceholderContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.eanbapplication.Models.AcceptOffer
import com.example.eanbapplication.Models.Offer
import com.example.eanbapplication.Models.RequestedItem
import com.example.eanbapplication.Ultilities.HttpGetEventsService
import com.example.eanbapplication.Ultilities.httpgetfulfilledrequestsoffer
import com.example.eanbapplication.Ultilities.httpgetoffersservice
import com.example.eanbapplication.Ultilities.httpreserveofferservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A fragment representing a list of Items.
 */
class EventsFragment : Fragment() {

    private var columnCount = 1
    var snackbarMessage by mutableStateOf("")
    var showSnackbar by mutableStateOf(false)
    var reserveOfferSuccess by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events_list, container, false)

        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
//            }
//        }

        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            EventsScreen(requireContext())
        }

        return view
    }

    fun onEventsRetrieved(result: MutableList<Event>) {

    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!",
        modifier = Modifier.padding(24.dp)
        )
    }


    @Composable
    fun EventsScreen(context: Context) {
        var eventsList by remember { mutableStateOf<List<Event>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        val httpService = remember { HttpGetEventsService(context) }

        // This effect will run only once when the Composable is first composed.
        LaunchedEffect(Unit) {
            val fetchedEvents = withContext(Dispatchers.IO) {
                httpService.fetchEvents()
            }
            eventsList = fetchedEvents ?: emptyList()
            isLoading = false
        }

        if (isLoading) {
            Text(text = "Loading...", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(eventsList) { event ->
                    EventItem(event = event)
                }
            }
        }
    }

    @Composable
    fun EventItem(event: Event) {
        Column {
            Text(
                text = "Event: ${event.name}\nStart: ${event.start}\nEnd: ${event.end}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.body1
            )
            for (requestedItem in event.requested_items) {
                RequestedItem(requestedItem)
            }
        }
        if (showSnackbar) {
            Snackbar(
                action = {
                    Button(onClick = { showSnackbar = false }) {
                        Text("OK")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = snackbarMessage)
            }
        }


    }

    @Composable
    fun RequestedItem(requestedItem: RequestedItem) {
        var isChecked = remember { mutableStateOf(requestedItem.isFulfilled) }
        var showDialog = remember { mutableStateOf(false) }
        var offers by remember { mutableStateOf<List<Offer>>(emptyList()) }
        var selectedOffer by remember { mutableStateOf<Offer?>(null) }
        var showMessageDialog = remember { mutableStateOf(false) }
        val httpService = remember { httpgetoffersservice(requestedItem.name) }
        val httpgetfulfilledrequestsoffer = remember { httpgetfulfilledrequestsoffer(requestedItem.requested_item_id) }
        var offer by remember { mutableStateOf<Offer?>(null) }
        var fetchOfferTrigger by remember { mutableStateOf(false) }

        if (showDialog.value) {
            LaunchedEffect(Unit) {
                val fetchedOffers = withContext(Dispatchers.IO) {
                    httpService.fetchOffers()
                }
                offers = fetchedOffers ?: emptyList()
            }




            AlertDialog(
                backgroundColor = MaterialTheme.colors.background,
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Offers") },
                text = {
                    Column {
                        offers.forEach { offer ->
                            SelectionContainer {
                                Text(
                                    text = "Offer: ${offer.name}, Amount: ${offer.amount}, State: ${offer.state}",
                                    modifier = Modifier.clickable {
                                        selectedOffer = offer
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        showDialog.value = false
                        selectedOffer?.let { offer ->
                            // Perform action with the selected offer
                            val acceptOffer = AcceptOffer(
                                offerId = offer.offerId,
                                requestedItemId = requestedItem.requested_item_id,
                                UserId = offer.offerUserId
                            )
                            httpreserveofferservice(
                                context = this,
                                acceptOffer = acceptOffer,
                                onSuccess = { isChecked.value = true }
                            ).execute()
                            if (reserveOfferSuccess){
                                isChecked.value = true
                                reserveOfferSuccess = false
                            }

                        }
                    }) {
                        Text("Reserve")
                    }
                },
                properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
            )
        }
        if (fetchOfferTrigger) {
            LaunchedEffect(Unit) {
                val fetchedOffer = withContext(Dispatchers.IO) {
                    httpgetfulfilledrequestsoffer.fetchOffer()
                }
                offer = fetchedOffer
                showMessageDialog.value = true
            }
        }

        if (showMessageDialog.value) {





            //offer = fetchedOffer

            AlertDialog(
                onDismissRequest = { showMessageDialog.value = false },
                title = { Text(text = "Reserved Offer") },
                text = { Text(text = "${offer?.name}\nStart: ${offer?.startDate}\nEnd:${offer?.endDate}") },
                confirmButton = {
                    Button(onClick = { showMessageDialog.value = false }) {
                        Text("OK")
                    }
                }
            )
        }
        Row(modifier = Modifier
            .padding(16.dp)
            .clickable {
                if (!isChecked.value) {
                    showDialog.value = true
                }else {
                    fetchOfferTrigger = true

                }

            }
        ) {
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = null // Make the checkbox read-only
            )
            Text(
                text = "${requestedItem.name} - Amount: ${requestedItem.amount}",
                modifier = Modifier.padding(start = 8.dp)
            )
        }


    }
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        val sampleRequestedItems = mutableListOf(
                RequestedItem(
                    requested_item_id = 1,
                    event_id = 101,
                    name = "Item 1",
                    amount = 10,
                    startDate = "2023-01-01",
                    endDate = "2023-01-10",
                    isFulfilled = false
                ),
        RequestedItem(
            requested_item_id = 2,
            event_id = 102,
            name = "Item 2",
            amount = 5,
            startDate = "2023-02-01",
            endDate = "2023-02-05",
            isFulfilled = true
        ),
        RequestedItem(
            requested_item_id = 3,
            event_id = 103,
            name = "Item 3",
            amount = 20,
            startDate = "2023-03-01",
            endDate = "2023-03-20",
            isFulfilled = false
        )
        )
        var event = Event(1, 1, 1, "Event Name", "2021-10-01T00:00:00", "2021-10-01T00:00:00", sampleRequestedItems)
        EventItem(event) // Preview for the EventsScreen
    }

    fun offerSuccess() {
        snackbarMessage = "Offer successfully reserved"
        showSnackbar = true
        reserveOfferSuccess = true
    }

    fun offerFailed() {
        snackbarMessage = "Error reserving offer"
        showSnackbar = true

    }
}