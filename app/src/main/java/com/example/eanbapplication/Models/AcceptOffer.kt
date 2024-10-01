package com.example.eanbapplication.Models

import kotlinx.serialization.Serializable
import java.time.LocalDate
@Serializable

data class AcceptOffer(
    val offerId: Int,
    val requestedItemId: Int,
    val UserId: Int?,
    )