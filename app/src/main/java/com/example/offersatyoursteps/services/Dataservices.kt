package com.example.offersatyoursteps.services

import com.example.offersatyoursteps.models.OfferDetails
import com.example.offersatyoursteps.models.ProductSubcategory

object Dataservices {
    
    val subcategories = listOf(
        ProductSubcategory("CLEANER", "phone", "Hot Deals"),
        ProductSubcategory("GROCERIES", "phone", "Special Offers"),
        ProductSubcategory("ACCESSORIES", "phone", "Trending Now"),
    )
    
    private val groceries = listOf(
        OfferDetails("phone", "Atta", "100", "95", "2%"),
        OfferDetails("phone", "Maida", "90", "86", "3%"),
        OfferDetails("phone", "Door Dhall", "150", "145", "5%"),
        OfferDetails("phone", "Tooth paste", "80", "78", "2%")
    )
    
    private val accessories = listOf(
        OfferDetails("phone", "headphone", "100", "98", "2%"),
        OfferDetails("phone", "mouse", "200", "196", "2%"),
        OfferDetails("phone", "keypad", "300", "296", "2%"),
        OfferDetails("phone", "ram", "600", "580", "2%")
    )
    
    private val clearner = listOf(
        OfferDetails("phone", "Harpick", "120", "118", "2%"),
        OfferDetails("phone", "Domic", "115", "110", "2%"),
        OfferDetails("phone", "Neem", "95", "92", "2%"),
        OfferDetails("phone", "Ala", "85", "81", "2%"),
        OfferDetails("phone", "Red Harpick", "95", "93", "2%"),
        OfferDetails("phone", "Harpick", "120", "118", "2%"),
        OfferDetails("phone", "Domic", "115", "110", "2%"),
        OfferDetails("phone", "Neem", "95", "92", "2%"),
        OfferDetails("phone", "Ala", "85", "81", "2%"),
        OfferDetails("phone", "Red Harpick", "95", "93", "2%")
    )
    
    private val digitalGoods = listOf<OfferDetails>()
    
    fun getProducts(subcategory : String?) : List<OfferDetails> {
        
        return when (subcategory) {
            "CLEANER" -> clearner
            "GROCERIES" -> groceries
            "ACCESSORIES" -> accessories
            else -> digitalGoods
        }
        
    }
}