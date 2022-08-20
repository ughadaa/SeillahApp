package com.example.ratina.sellahapp
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

val list = mutableListOf<String>()

@Parcelize
class User(val uid: String, val username: String, val email: String, val usertype: String, val childemail: MutableList<String>, val cgemail: MutableList<String>,val specialist: MutableList<String>, val profileImageUrl: String): Parcelable {
    constructor() : this("", "", "", "", list, list,list,"")
}