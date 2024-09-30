package com.example.eanbapplication.Models
import kotlinx.serialization.Serializable


@Serializable
data class RequestedItem (
    val requested_item_id: Int,
    val event_id: Int,
    var name: String,
    var amount: Int,
    var startDate: String?,
    var endDate: String?,
    var isFulfilled: Boolean,


)


