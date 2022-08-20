package com.example.ratina.sellahapp.Games

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class SAVEDATAA(

    val id: String,
    val userplaying: String,
    val right_answear_counter:Int,
    val wrong_filler_counter:Int,
    val total_counter:Int,
    val time_counter:Double,
    val date:String,

    val time1:Double,
    val time2:Double,
    val time3:Double,
    val time4:Double,
    val time5:Double,
    val time6:Double,
    val time7:Double,
    val time8:Double,
    val time9:Double,
    val time10:Double,

    val percent2: String

): Parcelable {


    constructor():this(" ","",0,0,0,0.0,"",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,""){}
}
