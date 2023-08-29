package com.example.offersatyoursteps.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot

data class UserModel constructor(var customerName:String?,
                                 var customerEmail:String?,
                                 var customerShopName:String?,
                                 var isCustomerOrMerchant:String?,
                                 var customerStreetName:String?,
                                 var customerCity:String?,
                                 var customerDistrict:String?,
                                 var customerState:String?,
                                 var customerPincode:String?):Parcelable{
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel : Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }
    
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel : Parcel, flags : Int) {
        parcel.writeString(customerName)
        parcel.writeString(customerEmail)
        parcel.writeString(customerShopName)
        parcel.writeString(isCustomerOrMerchant)
        parcel.writeString(customerStreetName)
        parcel.writeString(customerCity)
        parcel.writeString(customerDistrict)
        parcel.writeString(customerState)
        parcel.writeString(customerPincode)
    }
    
    override fun describeContents() : Int {
        return 0
    }
    
    companion object CREATOR : Parcelable.Creator<UserModel> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel : Parcel) : UserModel {
            return UserModel(parcel)
        }
        
        override fun newArray(size : Int) : Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}