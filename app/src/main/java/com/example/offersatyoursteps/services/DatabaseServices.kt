package com.example.offersatyoursteps.services

import android.util.Log
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.CUSTOMER_INFO_TABLE
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_SUB_COLLECTION_TABLE
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_TABLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime

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
    
    
    fun updateCustomerInfoRecord(
        userId : String,
        userModel : UserModel,
        complete : (Boolean) -> Unit
    ) {
    
        val custMap = HashMap<String, String>()
        custMap["User_Name"] = userModel.cName.toString()
        custMap["Shop_Name"] = userModel.cShopName.toString()
        custMap["Street_Name"] = userModel.cStreetName.toString()
        custMap["City"] = userModel.cCity.toString()
        custMap["District"] = userModel.cDistrict.toString()
        custMap["State"] = userModel.cState.toString()
        custMap["Pincode"] = userModel.cPincode.toString()
        
        fStore.collection(CUSTOMER_INFO_TABLE).document(userId).update(custMap as Map<String, Any>)
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
        
    
        val subcollectionDocId = fStore.collection(collectPath)
            .document(userId)
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
            .document().id
    
        fStore.collection(collectPath)
            .document(userId)
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
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
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
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
    
    fun updateProductDetailsRecord(
        collectPath : String,
        userId : String,
        productMap : HashMap<String, String>,
        complete : (Boolean) -> Unit
    ) {
        
      
        fStore.collection(collectPath)
            .document(userId)
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
            .document(productMap["_id"].toString())
            .update(productMap as Map<String, Any>)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun deleteProductDetails(userId : String, documentId : String, complete : (Boolean) -> Unit){
        fStore.collection(PRODUCT_INFO_TABLE)
            .document(userId)
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
            .document(documentId)
            .delete()
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
                            parentDoc.reference.collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
                                .whereEqualTo("Location", location)
                                .whereEqualTo("Offer_EndDate", LocalDateTime.now())
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
                        val subcollectionRef = parentDoc.reference.collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
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
                    parentCollectionSnapshot.reference.collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
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