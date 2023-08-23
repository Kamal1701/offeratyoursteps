package com.example.offersatyoursteps.services

import android.os.Build
import android.os.Parcel
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
                    println("get customer info record success")
//                    val userModel = custDoc.toObject(UserModel::class.java)!!
//                    val parcel = Parcel.obtain()
//                    userModel.writeToParcel(parcel,0)
//                    parcel.setDataPosition(0)
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
    
    fun createCustomerInfoRecord(
        collectPath : String,
        userId : String,
        userModel : UserModel,
        complete : (Boolean) -> Unit
    ) {
        
        val gson = Gson()
        val custJson = gson.toJson(userModel)
        val userMap = gson.fromJson(custJson, Map::class.java) as Map<String, Any>
        
        
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
    
//        val custMap = HashMap<String, String>()
//        custMap["customerName"] = userModel.customerName.toString()
//        custMap["customerShopName"] = userModel.customerShopName.toString()
//        custMap["customerStreetName"] = userModel.customerStreetName.toString()
//        custMap["customerCity"] = userModel.customerCity.toString()
//        custMap["customerDistrict"] = userModel.customerDistrict.toString()
//        custMap["customerState"] = userModel.customerState.toString()
//        custMap["customerPincode"] = userModel.customerPincode.toString()
    
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
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocationProductDetails(
        collectPath : String,
        productList : MutableList<OfferProductDetails>,
        location : String,
        complete : (Boolean) -> Unit
    ) {
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)
        println(formattedDate)
        val parentCollectionRef = fStore.collection(collectPath)
        println("getLocationProductDetails")
        println(collectPath)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                if (!parentCollectionSnapshot.isEmpty) {
                    for (parentDoc in parentCollectionSnapshot.documents) {
                        val subcollectionRef : Query =
                            parentDoc.reference.collection(PRODUCT_INFO_SUB_COLLECTION_TABLE)
                                .whereEqualTo("customerCity", location)
                                .whereGreaterThanOrEqualTo("Offer_EndDate", formattedDate)
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