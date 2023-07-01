package com.example.scoot_scoot.android.Data

import java.math.BigDecimal

data class Scooter(
    var id: Int,
    var description: String,
    var priceMin: BigDecimal,
    var priceKm: BigDecimal,
)
