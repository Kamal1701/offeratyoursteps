package com.example.offersatyoursteps.services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.CUSTOMER_INFO_TABLE
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_SUB_COLLECTION_TABLE
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_TABLE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DatabaseServices {
    
    private var fStore : FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
        set(value) = TODO()
    
    
    fun createCustomerInfoRecord(
        userId : String,
        userModel : UserModel,
        complete : (Boolean) -> Unit
    ) {
        
        val gson = Gson()
        val custJson = gson.toJson(userModel)
        val userMap = gson.fromJson(custJson, Map::class.java) as Map<String, Any>
        
        fStore.collection(CUSTOMER_INFO_TABLE).document(userId).set(userMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    
    fun getCustomerInfoRecord(
        userId : String,
        userModel : UserModel,
        complete : (Boolean) -> Unit
    ) {
        
        fStore.collection(CUSTOMER_INFO_TABLE).document(userId)
            .get().addOnSuccessListener { custDoc ->
                if (custDoc != null) {
                    userModel.customerName = custDoc.get("customerName").toString()
                    userModel.customerEmail = custDoc.get("customerEmail").toString()
                    userModel.customerShopName = custDoc.get("customerShopName").toString()
                    userModel.customerStreetName = custDoc.get("customerStreetName").toString()
                    userModel.isCustomerOrMerchant = custDoc.get("isCustomerOrMerchant").toString()
                    userModel.customerCity = custDoc.get("customerCity").toString()
                    userModel.customerDistrict = custDoc.get("customerDistrict").toString()
                    userModel.customerState = custDoc.get("customerState").toString()
                    userModel.customerPincode = custDoc.get("customerPincode").toString()
                    
                    complete(true)
                }
                
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
        
        val gson = Gson()
        val custJson = gson.toJson(userModel)
        val custMap = gson.fromJson(custJson, Map::class.java) as Map<String, Any>
        
        fStore.collection(CUSTOMER_INFO_TABLE).document(userId).update(custMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun createProductDetailsRecord(
        userId : String,
        offerProduct : OfferProductDetails,
        complete : (Boolean) -> Unit
    ) {
        
        val gson = Gson()
        val offerJson = gson.toJson(offerProduct)
        val productMap = gson.fromJson(offerJson, Map::class.java) as Map<String, Any>
        
        fStore.collection(PRODUCT_INFO_TABLE)
            .document(userId).set(mapOf("_id" to userId))
        
        val subcollectionDocId = fStore.collection(PRODUCT_INFO_TABLE)
            .document(userId)
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
            .document().id
        
        fStore.collection(PRODUCT_INFO_TABLE)
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
        
        fStore.collection(PRODUCT_INFO_TABLE)
            .document(userId)
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
            .document(subcollectionDocId)
            .update(mapOf("docId" to subcollectionDocId))
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun updateProductDetailsRecord(
        userId : String,
        productMap : HashMap<String, String>,
        complete : (Boolean) -> Unit
    ) {
        
        fStore.collection(PRODUCT_INFO_TABLE)
            .document(userId)
            .collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
            .document(productMap["docId"].toString())
            .update(productMap as Map<String, Any>)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    }
    
    fun deleteProductDetails(userId : String, documentId : String, complete : (Boolean) -> Unit) {
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
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocationProductDetails(
        productList : MutableList<OfferProductDetails>,
        location : String,
        complete : (Boolean) -> Unit
    ) {
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)
        println(formattedDate)
        val parentCollectionRef = fStore.collection(PRODUCT_INFO_TABLE)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                println("parentcollect is empty")
                println(parentCollectionSnapshot.isEmpty)
                if (!parentCollectionSnapshot.isEmpty) {
                    for (parentDoc in parentCollectionSnapshot.documents) {
                        val subcollectionRef : Query =
                            parentDoc.reference.collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
                                .whereEqualTo("shopCity", location)
                        subcollectionRef
                            .get().addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    for ((prodCount, doc) in querySnapshot.withIndex()) {
//                                        if (doc.data["Location"].toString() == location) {
                                        
                                        val endDateString = doc.getString("productOfferEdDate")
                                        if (endDateString != null) {
//                                            val dateFormatter =
//                                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
//                                            val offerEndDt : LocalDate =
//                                                LocalDate.parse(endDateString, dateFormatter)
//                                            val currentDate : LocalDate = LocalDate.now()
//                                            val compareDate : Int =
//                                                offerEndDt.compareTo(currentDate)
//                                            println(offerEndDt)
//                                            println(currentDate)
//                                            println(compareDate)
                                            
                                            if (isOfferActive(endDateString)) {
                                                productList.add(
                                                    prodCount,
                                                    OfferProductDetails.fromQuerySnapshot(doc)
                                                )
                                            }
                                        }
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
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllProductDetails(
        productList : MutableList<OfferProductDetails>,
        complete : (Boolean) -> Unit
    ) {
        
        val parentCollectionRef = fStore.collection(PRODUCT_INFO_TABLE)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                if (!parentCollectionSnapshot.isEmpty) {
                    for (parentDoc in parentCollectionSnapshot.documents) {
                        val subcollectionRef =
                            parentDoc.reference.collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
                        subcollectionRef
                            .get().addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    for ((prodCount, doc) in querySnapshot.withIndex()) {
                                        
                                        val endDateString = doc.getString("productOfferEdDate")
                                        if (endDateString != null) {
                                            
                                            if (isOfferActive(endDateString)) {
                                                productList.add(
                                                    prodCount,
                                                    OfferProductDetails.fromQuerySnapshot(doc)
                                                )
                                            }
                                        }

//                                        productList.add(
//                                            prodCount,
//                                            OfferProductDetails.fromQuerySnapshot(doc)
//                                        )
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
        productList : MutableList<OfferProductDetails>,
        userId : String,
        complete : (Boolean) -> Unit
    ) {
        
        val parentCollectionRef = fStore.collection(PRODUCT_INFO_TABLE).document(userId)
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
                        Log.d("EXEC", it.localizedMessage.toString())
                        complete(false)
                    }
            }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun isOfferActive(offerDate : String?) : Boolean {
//        var isActive = false
        val dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val offerEndDt : LocalDate =
            LocalDate.parse(offerDate, dateFormatter)
        val currentDate : LocalDate = LocalDate.now()
        val compareDate : Int =
            offerEndDt.compareTo(currentDate)
        println(offerEndDt)
        println(currentDate)
        println(compareDate)
    
        return compareDate >= 0
    }
}