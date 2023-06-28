package com.example.scoot_scoot.android

import java.math.BigDecimal

data class Scooter(
    var id: Int,
    var description: String,
    var priceMin: BigDecimal,
    var priceKm: BigDecimal,
)
