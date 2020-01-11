package com.example.antidrug

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Profile (
    var password: String? = "",
    var gmail: String? = "",
    var name: String? = ""
)