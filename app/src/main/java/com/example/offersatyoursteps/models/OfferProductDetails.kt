package com.example.offersatyoursteps.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.QueryDocumentSnapshot


data class OfferProductDetails(
    var imgName : String,
    var productName : String,
    var brandName : String,
    var prodCategory : String,
    var prodSubcategory : String,
    var actualPrice : String,
    var discountPrice : String,
    var offerStDate : String,
    var offerEdDate : String,
    var discountPercentage : String,
    var prodWeight : String,
    var prodDesc : String,
    var location : String
//    var shopName : String
):Parcelable {
    constructor(parcel : Parcel) : this(
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
    
//    companion object {
//        fun fromQuerySnapshot(subDocSnapshot : QueryDocumentSnapshot) : OfferProductDetails {
//
//            return OfferProductDetails(
//                subDocSnapshot.data["Product_Image"].toString(),
//                subDocSnapshot.data["Product_Name"].toString(),
//                subDocSnapshot.data["Product_Brand"].toString(),
//                subDocSnapshot.data["Product_Category"].toString(),
//                subDocSnapshot.data["Product_Subcategory"].toString(),
//                subDocSnapshot.data["Product_ActualPrice"].toString(),
//                subDocSnapshot.data["Product_DiscountPrice"].toString(),
//                subDocSnapshot.data["Offer_StartDate"].toString(),
//                subDocSnapshot.data["Offer_EndDate"].toString(),
//                "2%",
//                subDocSnapshot.data["Product_Weight"].toString(),
//                subDocSnapshot.data["Product_Desc"].toString(),
//                subDocSnapshot.data["Location"].toString()
////subDocSnapshot.data["Shop_Name"].toString(),
//            )
//        }
//
//    }
    
    override fun writeToParcel(parcel : Parcel, flags : Int) {
        parcel.writeString(imgName)
        parcel.writeString(productName)
        parcel.writeString(brandName)
        parcel.writeString(prodCategory)
        parcel.writeString(prodSubcategory)
        parcel.writeString(actualPrice)
        parcel.writeString(discountPrice)
        parcel.writeString(offerStDate)
        parcel.writeString(offerEdDate)
        parcel.writeString(discountPercentage)
        parcel.writeString(prodWeight)
        parcel.writeString(prodDesc)
        parcel.writeString(location)
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
                subDocSnapshot.data["Product_Image"].toString(),
                subDocSnapshot.data["Product_Name"].toString(),
                subDocSnapshot.data["Product_Brand"].toString(),
                subDocSnapshot.data["Product_Category"].toString(),
                subDocSnapshot.data["Product_Subcategory"].toString(),
                subDocSnapshot.data["Product_ActualPrice"].toString(),
                subDocSnapshot.data["Product_DiscountPrice"].toString(),
                subDocSnapshot.data["Offer_StartDate"].toString(),
                subDocSnapshot.data["Offer_EndDate"].toString(),
                subDocSnapshot.data["Discount_Percentage"].toString(),
                subDocSnapshot.data["Product_Weight"].toString(),
                subDocSnapshot.data["Product_Desc"].toString(),
                subDocSnapshot.data["Location"].toString()
//subDocSnapshot.data["Shop_Name"].toString(),
            )
        }
    }
}