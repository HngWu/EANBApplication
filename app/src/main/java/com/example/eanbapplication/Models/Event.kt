package com.example.eanbapplication.Models

import kotlinx.serialization.Serializable
import java.util.Date


@Serializable

data class Event (
    val event_id: Int,
    val user_id: Int,
    var location_id: Int,
    var name: String,
    var start: String,
    var end: String,
    var requested_items: MutableList<RequestedItem>,
)