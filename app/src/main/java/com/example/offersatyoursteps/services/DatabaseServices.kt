package com.example.offersatyoursteps.services

import android.util.Log
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseServices {
    
    private lateinit var fStore : FirebaseFirestore
    
    fun getCustomerInfoRecord(
        collectPath : String,
        userId : String,
        userModel : UserModel,
        complete : (Boolean) -> Unit
    ) {
        fStore = FirebaseFirestore.getInstance()
        fStore.collection(collectPath).document(userId)
            .get().addOnSuccessListener { custDoc ->
                if (custDoc != null) {
                    userModel.cName = custDoc.get("User_Name").toString()
                    userModel.cEmail = custDoc.get("User_EmailId").toString()
                    userModel.cShopName = custDoc.get("Shop_Name").toString()
                    userModel.isMerchant = custDoc.get("IsMerchant").toString()
                    userModel.cCity = custDoc.get("City").toString()
                    userModel.cState = custDoc.get("State").toString()

//                    Log.d("DEBUG", "DatabaseServices")
//                    Log.d("DEBUG", userModel.cName.toString())
//                    Log.d("DEBUG", userModel.cEmail.toString())
                    complete(true)
                }
                
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun createCustomerInfoRecord(
        collectPath : String,
        userId : String,
        userMap : HashMap<String, String>,
        complete : (Boolean) -> Unit
    ) {
        fStore = FirebaseFirestore.getInstance()
        
        fStore.collection(collectPath).document(userId).set(userMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun createProductDetailsRecord(
        collectPath : String,
        userId : String,
        productMap : HashMap<String, String>,
        complete : (Boolean) -> Unit
    ) {
        fStore = FirebaseFirestore.getInstance()
        
        fStore.collection(collectPath)
            .document(userId)
            .collection("OfferProductDetails")
            .document()
            .set(productMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun getProductDetailsRecord(
        collectPath : String, userId : String,
        productList : MutableList<OfferProductDetails>,
        complete : (Boolean) -> Unit
    ) {
        fStore = FirebaseFirestore.getInstance()
        val parentDocRef = fStore.collection(collectPath).document(userId)
        println(parentDocRef.id)
        
        
        parentDocRef.collection("OfferProductDetails")
            .get().addOnSuccessListener { querySnapshot ->
                println(querySnapshot.isEmpty)
                if (!querySnapshot.isEmpty) {
                    var prodCount = 0
                    for (doc in querySnapshot) {
                        val data = doc.data
//                        opd.imgName = data.get("Product_Image").toString()
//                        opd.brandName = data.get("Product_Brand").toString()
//                        opd.productName = data.get("Product_Name").toString()
//                        opd.prodCategory = data.get("Product_Category").toString()
//                        opd.prodSubcategory = data.get("Product_Subcategory").toString()
//                        opd.actualPrice = data.get("Product_ActualPrice").toString()
//                        opd.discountPrice = data.get("Product_DiscountPrice").toString()
//                        opd.offerStDate = data.get("Offer_StartDate").toString()
//                        opd.offerEdDate = data.get("Offer_EndDate").toString()
//                        opd.location = data.get("Location").toString()
//                        opd.prodWeight = data.get("Product_Weight").toString()
//                        opd.prodDesc = data.get("Product_Desc").toString()
//                        opd.discountPercentage = "2%"
                        
                        var imgName = data.get("Product_Image").toString()
                        var brandName = data.get("Product_Brand").toString()
                        var productName = data.get("Product_Name").toString()
                        var prodCategory = data.get("Product_Category").toString()
                        var prodSubcategory = data.get("Product_Subcategory").toString()
                        var actualPrice = data.get("Product_ActualPrice").toString()
                        var discountPrice = data.get("Product_DiscountPrice").toString()
                        var offerStDate = data.get("Offer_StartDate").toString()
                        var offerEdDate = data.get("Offer_EndDate").toString()
                        var location = data.get("Location").toString()
                        var prodWeight = data.get("Product_Weight").toString()
                        var prodDesc = data.get("Product_Desc").toString()
//                        var shopName = data.get("Shop_Name").toString()
                        var discountPercentage = "2%"
                        
                        productList.add(
                            prodCount, OfferProductDetails(
                                imgName,
                                productName,
                                brandName,
                                prodCategory,
                                prodSubcategory,
                                actualPrice,
                                discountPrice,
                                offerStDate,
                                offerEdDate,
                                discountPercentage,
                                prodWeight,
                                prodDesc,
                                location
//                            shopName
                            )
                        )
                        prodCount++
                    }
                    complete(true)
                }
                
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
}