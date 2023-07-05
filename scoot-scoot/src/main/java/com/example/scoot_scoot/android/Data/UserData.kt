package com.example.scoot_scoot.android.Data


data class UserData(
    var id: Int? = null,
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var birthdate: String = "",
    var password: String = "",
    var credit: Int = 0,
    var rides: ArrayList<RideModel> = ArrayList()
)
