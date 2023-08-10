package com.example.offersatyoursteps.models

import android.os.Parcel
import android.os.Parcelable

class UserModel constructor(var cName:String?,var cEmail:String?,
                            var cShopName:String?, var isMerchant:String?,
                            var cCity:String?, var cState:String?):Parcelable{
    constructor(parcel : Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }
    
    override fun writeToParcel(parcel : Parcel, flags : Int) {
        parcel.writeString(cName)
        parcel.writeString(cEmail)
        parcel.writeString(cShopName)
        parcel.writeString(isMerchant)
        parcel.writeString(cCity)
        parcel.writeString(cState)
    }
    
    override fun describeContents() : Int {
        return 0
    }
    
    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel : Parcel) : UserModel {
            return UserModel(parcel)
        }
        
        override fun newArray(size : Int) : Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
    
}