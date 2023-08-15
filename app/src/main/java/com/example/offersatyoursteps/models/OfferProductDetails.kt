package com.example.offersatyoursteps.models

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
) {
}