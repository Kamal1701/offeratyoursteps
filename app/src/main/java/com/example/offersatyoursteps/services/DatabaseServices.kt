package com.example.offersatyoursteps.services

import android.util.Log
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

object DatabaseServices {
    
    private var fStore = FirebaseFirestore.getInstance()
    
    fun getCustomerInfoRecord(
        collectPath : String,
        userId : String,
        userModel : UserModel,
        complete : (Boolean) -> Unit
    ) {
        
        fStore.collection(collectPath).document(userId)
            .get().addOnSuccessListener { custDoc ->
                if (custDoc != null) {
                    userModel.cName = custDoc.get("User_Name").toString()
                    userModel.cEmail = custDoc.get("User_EmailId").toString()
                    userModel.cShopName = custDoc.get("Shop_Name").toString()
                    userModel.cStreetName = custDoc.get("Street_Name").toString()
                    userModel.isMerchant = custDoc.get("IsMerchant").toString()
                    userModel.cCity = custDoc.get("City").toString()
                    userModel.cDistrict = custDoc.get("District").toString()
                    userModel.cState = custDoc.get("State").toString()
                    userModel.cPincode = custDoc.get("Pincode").toString()
                    
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
        
        fStore.collection(collectPath)
            .document(userId).set(mapOf("_id" to userId))
        
//        fStore.collection(collectPath)
//            .document(userId)
//            .collection("OfferProductDetails")
//            .document()
//            .set(productMap)
//            .addOnSuccessListener {
//                complete(true)
//            }
//            .addOnFailureListener {
//                Log.d("DEBUG", it.localizedMessage)
//                complete(false)
//            }
    
    
        val subcollectionDocId = fStore.collection(collectPath)
            .document(userId)
            .collection("OfferProductDetails")
            .document().id
    
        fStore.collection(collectPath)
            .document(userId)
            .collection("OfferProductDetails")
            .document(subcollectionDocId)
            .set(productMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    
        fStore.collection(collectPath)
            .document(userId)
            .collection("OfferProductDetails")
            .document(subcollectionDocId)
            .update(mapOf("_id" to subcollectionDocId))
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun getLocationProductDetails(
        collectPath : String,
        productList : MutableList<OfferProductDetails>,
        location : String,
        complete : (Boolean) -> Unit
    ) {
        
        val parentCollectionRef = fStore.collection(collectPath)
        println("getLocationProductDetails")
        println(collectPath)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                if (!parentCollectionSnapshot.isEmpty) {
                    for (parentDoc in parentCollectionSnapshot.documents) {
                        val subcollectionRef : Query =
                            parentDoc.reference.collection("OfferProductDetails")
                                .whereEqualTo("Location", location)
                        subcollectionRef
                            .get().addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    for ((prodCount, doc) in querySnapshot.withIndex()) {
//                                        if (doc.data["Location"].toString() == location) {
                                        productList.add(
                                            prodCount,
                                            OfferProductDetails.fromQuerySnapshot(doc)
                                        )
//                                        }
                                    }
                                    complete(true)
                                } else {
                                    complete(false)
                                }
                            }
                            .addOnFailureListener {
                                Log.d("EXEC", it.localizedMessage)
                                complete(false)
                            }
                    }
                } else {
                    complete(false)
                }
            }.addOnFailureListener {
                Log.d("EXEC", it.localizedMessage)
                complete(false)
            }
        }
        
    }
    
    fun getAllProductDetails(
        collectPath : String,
        productList : MutableList<OfferProductDetails>,
        complete : (Boolean) -> Unit
    ) {
        
        val parentCollectionRef = fStore.collection(collectPath)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                if (!parentCollectionSnapshot.isEmpty) {
                    for (parentDoc in parentCollectionSnapshot.documents) {
                        val subcollectionRef = parentDoc.reference.collection("OfferProductDetails")
                        subcollectionRef
                            .get().addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    for ((prodCount, doc) in querySnapshot.withIndex()) {
                                        productList.add(
                                            prodCount,
                                            OfferProductDetails.fromQuerySnapshot(doc)
                                        )
                                    }
                                    complete(true)
                                } else {
                                    complete(false)
                                }
                            }
                            .addOnFailureListener {
                                Log.d("EXEC", it.localizedMessage)
                                complete(false)
                            }
                    }
                } else {
                    complete(false)
                }
            }
                .addOnFailureListener {
                    Log.d("EXEC", it.localizedMessage)
                    complete(false)
                }
        }
        
    }
    
    fun getProductDetailByUserId(
        collectPath : String,
        productList : MutableList<OfferProductDetails>,
        userId : String,
        complete : (Boolean) -> Unit
    ) {
        
        val parentCollectionRef = fStore.collection(collectPath).document(userId)
        println("getProductDetailByUserId")
        println(collectPath)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                val subcollectionRef : Query =
                    parentCollectionSnapshot.reference.collection("OfferProductDetails")
                subcollectionRef
                    .get().addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            for ((prodCount, doc) in querySnapshot.withIndex()) {
                                productList.add(
                                    prodCount,
                                    OfferProductDetails.fromQuerySnapshot(doc)
                                )
                            }
                            complete(true)
                        } else {
                            complete(false)
                        }
                    }
                    .addOnFailureListener {
                        Log.d("EXEC", it.localizedMessage)
                        complete(false)
                    }
            }
        }
    }
}