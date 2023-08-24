package com.example.offersatyoursteps.utilities

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import java.util.Calendar

class PastDateValidator() : DateValidator {
    constructor(parcel : Parcel) : this() {
    }
    
//    override fun writeToParcel(parcel : Parcel, flags : Int) {
//        super.writeToParcel(parcel, flags)
//    }
    
    override fun isValid(date : Long) : Boolean {
        val currentCalender = Calendar.getInstance()
        
        val selectedCalendar = Calendar.getInstance().apply {
            timeInMillis = date - 86400000
        }
        
        return selectedCalendar >currentCalender
    }
    
    override fun describeContents() : Int {
        return 0
    }
    
    override fun writeToParcel(p0 : Parcel, p1 : Int) {
        TODO("Not yet implemented")
    }
    
    companion object CREATOR : Parcelable.Creator<PastDateValidator> {
        override fun createFromParcel(parcel : Parcel) : PastDateValidator {
            return PastDateValidator(parcel)
        }
        
        override fun newArray(size : Int) : Array<PastDateValidator?> {
            return arrayOfNulls(size)
        }
    }
}