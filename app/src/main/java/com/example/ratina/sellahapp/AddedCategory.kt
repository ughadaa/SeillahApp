package com.example.ratina.sellahapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddedCategory(val cid: String, val image_path: String, val name: String, val arabicname: String, val useradding: String): Parcelable {
    constructor() : this("","", "","","")
}