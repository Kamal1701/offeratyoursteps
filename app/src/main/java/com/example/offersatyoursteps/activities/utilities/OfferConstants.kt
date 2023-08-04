package com.example.offersatyoursteps.activities.utilities

import com.example.offersatyoursteps.activities.models.OfferDetails

object OfferConstants {

    fun getOfferData():ArrayList<OfferDetails>{
    
        val offerList = ArrayList<OfferDetails>()
        
        val offer1 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer1)
        val offer2 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer2)
        val offer3 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer3)
        val offer4 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer4)
        val offer5 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer5)
        val offer6 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer6)
        val offer7 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer7)
        val offer8 = OfferDetails("phone","Offer Product", "100", "80", "20%")
        offerList.add(offer8)
        
        return offerList
        
    }
}