package com.example.scoot_scoot.android.Data

import java.time.Instant
import java.util.Date

data class RegisterUser(
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var birthdate: Date = Date.from(Instant.now()),
    //var password: String = "",
)
