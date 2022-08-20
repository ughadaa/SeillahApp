package com.example.ratina.sellahapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddedImage(val cid: String, val image_path: String, val name: String,val nameofcategory: String): Parcelable {
    constructor() : this("","", "","")
}