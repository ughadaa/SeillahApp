package com.example.ratina.sellahapp.MedicalSpecialist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class Report(val reportid: String, val aboutchild: String, val body: String, val reportdate: String, val specialist: String, val specialistemail: String): Parcelable {
    constructor() : this("", "", "", "", "","")
}