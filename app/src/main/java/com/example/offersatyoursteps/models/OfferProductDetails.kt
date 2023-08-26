package com.example.offersatyoursteps.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.Date


data class OfferProductDetails(
    var docId : String,
    var productImgName : String,
    var isImageAvailable : Boolean,
    var productName : String,
    var productBrandName : String,
    var productCategory : String,
    var productSubcategory : String,
    var productActualPrice : String,
    var productDiscountPrice : String,
    var productOfferStDate : String,
    var productOfferEdDate : String,
    var productDiscountPercentage : String,
    var productWeight : String,
    var productDesc : String,
    var shopName : String,
    var shopStreetName : String,
    var shopCity : String,
    var shopDistrict : String,
    var shopState : String,
    var shopPincode : String,
    
    ) : Parcelable {
    constructor(parcel : Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toBoolean(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }
    
    
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel : Parcel, flags : Int) {
        parcel.writeString(docId)
        parcel.writeString(productImgName)
        parcel.writeBoolean(isImageAvailable)
        parcel.writeString(productName)
        parcel.writeString(productBrandName)
        parcel.writeString(productCategory)
        parcel.writeString(productSubcategory)
        parcel.writeString(productActualPrice)
        parcel.writeString(productDiscountPrice)
        parcel.writeString(productOfferStDate)
        parcel.writeString(productOfferEdDate)
        parcel.writeString(productDiscountPercentage)
        parcel.writeString(productWeight)
        parcel.writeString(productDesc)
        parcel.writeString(shopName)
        parcel.writeString(shopStreetName)
        parcel.writeString(shopCity)
        parcel.writeString(shopDistrict)
        parcel.writeString(shopState)
        parcel.writeString(shopPincode)

    }
    
    override fun describeContents() : Int {
        return 0
    }
    
    companion object CREATOR : Parcelable.Creator<OfferProductDetails> {
        override fun createFromParcel(parcel : Parcel) : OfferProductDetails {
            return OfferProductDetails(parcel)
        }
        
        override fun newArray(size : Int) : Array<OfferProductDetails?> {
            return arrayOfNulls(size)
        }
        
        fun fromQuerySnapshot(subDocSnapshot : QueryDocumentSnapshot) : OfferProductDetails {
            
            return OfferProductDetails(
                subDocSnapshot.data["docId"].toString(),
                subDocSnapshot.data["productImgName"].toString(),
                subDocSnapshot.getBoolean("isImageAvailable")?:false,
//                subDocSnapshot.data["isImageAvailable"].toString(),
                subDocSnapshot.data["productName"].toString(),
                subDocSnapshot.data["productBrandName"].toString(),
                subDocSnapshot.data["productCategory"].toString(),
                subDocSnapshot.data["productSubcategory"].toString(),
                subDocSnapshot.data["productActualPrice"].toString(),
                subDocSnapshot.data["productDiscountPrice"].toString(),
                subDocSnapshot.data["productOfferStDate"].toString(),
                subDocSnapshot.data["productOfferEdDate"].toString(),
                subDocSnapshot.data["productDiscountPercentage"].toString(),
                subDocSnapshot.data["productWeight"].toString(),
                subDocSnapshot.data["productDesc"].toString(),
                subDocSnapshot.data["shopName"].toString(),
                subDocSnapshot.data["shopStreetName"].toString(),
                subDocSnapshot.data["shopCity"].toString(),
                subDocSnapshot.data["shopDistrict"].toString(),
                subDocSnapshot.data["shopState"].toString(),
                subDocSnapshot.data["shopPincode"].toString()
            )
        }
    }
}