package com.example.offersatyoursteps.services

import android.util.Log
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

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
//        println(collectPath)
        
//        val documents = async {
//            val collectRef = fStore.collection(collectPath)
//            val querySnapshot = collectRef.get()
//            return@async querySnapshot.result
//        }
//
//        runBlocking {
//            val result = documents.await()
//            for(doc in result){
//                println(doc.data)
//            }
//        }
        
        
        val parentCollectionRef = fStore.collection(collectPath)
        println(parentCollectionRef.id)
        GlobalScope.launch(Dispatchers.Main) {
            val parentDocRef = parentCollectionRef.get().await().documents
            for (parentDoc in parentDocRef){
                val parentDocId = parentDoc.id
                println(parentDocId)
            }
        }
        
        parentCollectionRef.get().addOnSuccessListener {
            parentCollectionSnapshot ->
            for (parentDoc in parentCollectionSnapshot.documents){
                val parentDocId = parentDoc.id
                println(parentDocId)
                val subcollectionRef = parentDoc.reference.collection("OfferProductDetails")
    
//                parentDocId.collection("OfferProductDetails")
                subcollectionRef
                    .get().addOnSuccessListener { querySnapshot ->
                        println(querySnapshot.isEmpty)
                        if (!querySnapshot.isEmpty) {
                            var prodCount = 0
                            for (doc in querySnapshot) {
                                val data = doc.data
                    
                                var imgName = data["Product_Image"].toString()
                                var brandName = data["Product_Brand"].toString()
                                var productName = data["Product_Name"].toString()
                                var prodCategory = data["Product_Category"].toString()
                                var prodSubcategory = data["Product_Subcategory"].toString()
                                var actualPrice = data["Product_ActualPrice"].toString()
                                var discountPrice = data["Product_DiscountPrice"].toString()
                                var offerStDate = data["Offer_StartDate"].toString()
                                var offerEdDate = data["Offer_EndDate"].toString()
                                var location = data["Location"].toString()
                                var prodWeight = data["Product_Weight"].toString()
                                var prodDesc = data["Product_Desc"].toString()
//                        var shopName = data["Shop_Name"].toString()
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
            .addOnFailureListener {
                println(parentCollectionRef.get().isSuccessful)
            }
        
//        println(parentDocRef)
        
//        parentDocRef.collection("OfferProductDetails")
//            .get().addOnSuccessListener { querySnapshot ->
//                println(querySnapshot.isEmpty)
//                if (!querySnapshot.isEmpty) {
//                    var prodCount = 0
//                    for (doc in querySnapshot) {
//                        val data = doc.data
//
//                        var imgName = data["Product_Image"].toString()
//                        var brandName = data["Product_Brand"].toString()
//                        var productName = data["Product_Name"].toString()
//                        var prodCategory = data["Product_Category"].toString()
//                        var prodSubcategory = data["Product_Subcategory"].toString()
//                        var actualPrice = data["Product_ActualPrice"].toString()
//                        var discountPrice = data["Product_DiscountPrice"].toString()
//                        var offerStDate = data["Offer_StartDate"].toString()
//                        var offerEdDate = data["Offer_EndDate"].toString()
//                        var location = data["Location"].toString()
//                        var prodWeight = data["Product_Weight"].toString()
//                        var prodDesc = data["Product_Desc"].toString()
////                        var shopName = data["Shop_Name"].toString()
//                        var discountPercentage = "2%"
//
//                        productList.add(
//                            prodCount, OfferProductDetails(
//                                imgName,
//                                productName,
//                                brandName,
//                                prodCategory,
//                                prodSubcategory,
//                                actualPrice,
//                                discountPrice,
//                                offerStDate,
//                                offerEdDate,
//                                discountPercentage,
//                                prodWeight,
//                                prodDesc,
//                                location
////                            shopName
//                            )
//                        )
//                        prodCount++
//                    }
//                    complete(true)
//                }
//
//            }
//            .addOnFailureListener {
//                Log.d("DEBUG", it.localizedMessage)
//                complete(false)
//            }
    }
    
}