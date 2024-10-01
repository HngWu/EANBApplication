package com.example.eanbapplication.Models

import kotlinx.serialization.Serializable
import java.time.LocalDate
@Serializable

data class Offer(
    val offerId: Int,
    val offerUserId: Int,
    val requestUserId: Int?,
    val name: String,
    val amount: String,
    val state: String,
    val startDate: String?,
    val endDate: String?,

)