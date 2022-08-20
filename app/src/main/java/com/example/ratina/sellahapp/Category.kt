package com.example.ratina.sellahapp


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Category(val name: String, val image_path: String, val voice_path: String): Parcelable {
    constructor() : this("", "","")
}
