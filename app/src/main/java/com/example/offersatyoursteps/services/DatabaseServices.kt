package com.example.offersatyoursteps.services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.CUSTOMER_DETAIL_TABLE
import com.example.offersatyoursteps.utilities.DELETED_PRODUCT_INFO_TABLE
import com.example.offersatyoursteps.utilities.OFFER_PRODUCT_DETAIL_TABLE
import com.example.offersatyoursteps.utilities.PRODUCT_DETAIL_TABLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DatabaseServices {
    
    private var fStore : FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
        set(value) = TODO()
    
    private var storageRef = Firebase.storage
    
    
    fun createCustomerInfoRecord(
        userId : String,
        userModel : UserModel,
        complete : (Boolean) -> Unit
    ) {
        
        val gson = Gson()
        val custJson = gson.toJson(userModel)
        val userMap = gson.fromJson(custJson, Map::class.java) as Map<String, Any>
        
        fStore.collection(CUSTOMER_DETAIL_TABLE).document(userId).set(userMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage.toString())
                complete(false)
            }
    
        fStore.collection(CUSTOMER_DETAIL_TABLE)
            .document(userId)
            .update(mapOf("createTimeStamp" to FieldValue.serverTimestamp(),
                "updateTimeStamp" to FieldValue.serverTimestamp()))
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
        
        fStore.collection(CUSTOMER_DETAIL_TABLE).document(userId)
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
        
        fStore.collection(CUSTOMER_DETAIL_TABLE).document(userId).update(custMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
    
        fStore.collection(CUSTOMER_DETAIL_TABLE)
            .document(userId)
            .update(mapOf("updateTimeStamp" to FieldValue.serverTimestamp()))
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
        
        println("createproduct details")
        println(productMap)
        
        fStore.collection(PRODUCT_DETAIL_TABLE)
            .document(userId).set(mapOf("_id" to userId))
        
        val subcollectionDocId = fStore.collection(PRODUCT_DETAIL_TABLE)
            .document(userId)
            .collection(OFFER_PRODUCT_DETAIL_TABLE)
            .document().id
        
        fStore.collection(PRODUCT_DETAIL_TABLE)
            .document(userId)
            .collection(OFFER_PRODUCT_DETAIL_TABLE)
            .document(subcollectionDocId)
            .set(productMap)
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
                complete(false)
            }
        
        fStore.collection(PRODUCT_DETAIL_TABLE)
            .document(userId)
            .collection(OFFER_PRODUCT_DETAIL_TABLE)
            .document(subcollectionDocId)
            .update(mapOf("docId" to subcollectionDocId,
                "createTimeStamp" to FieldValue.serverTimestamp(),
                "updateTimeStamp" to FieldValue.serverTimestamp()))
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
        productMap : HashMap<String, Any>,
        complete : (Boolean) -> Unit
    ) {
    
        productMap["updateTimeStamp"] = FieldValue.serverTimestamp()
        fStore.collection(PRODUCT_DETAIL_TABLE)
            .document(userId)
            .collection(OFFER_PRODUCT_DETAIL_TABLE)
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
    
    fun deleteProductDetails(userId : String, documentId : String, productNm:String, complete : (Boolean) -> Unit) {
        fStore.collection(PRODUCT_DETAIL_TABLE)
            .document(userId)
            .collection(OFFER_PRODUCT_DETAIL_TABLE)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                complete(true)
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage.toString())
                complete(false)
            }
        
        fStore.collection(DELETED_PRODUCT_INFO_TABLE)
            .document(documentId)
            .set(mapOf("userId" to userId, "deleteTimestamp" to FieldValue.serverTimestamp(), "productName" to productNm))
            .addOnSuccessListener {
                Log.d("DEBUG", "Delete tracker updated successfully")
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage.toString())
            }
    }
    
    fun deleteProductImage(userId:String, imgUrl : String, complete : (Boolean) -> Unit){
        
        val imgRef = storageRef.getReferenceFromUrl(imgUrl)
        val user = FirebaseAuth.getInstance().currentUser
        println("deleteProductImage")
        println(imgRef)
        
        if(user?.uid == userId) {
            imgRef.delete()
                .addOnSuccessListener {
                    println("image deleted")
                    Log.d("DEBUG", "Image reference deleted")
                    complete(true)
                }
                .addOnFailureListener {
                    Log.d("EXEC", it.localizedMessage.toString())
                    complete(false)
                }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocationProductDetails(
        productList : MutableList<OfferProductDetails>,
        location : String,
        complete : (Boolean) -> Unit
    ) {
 
        val parentCollectionRef = fStore.collection(PRODUCT_DETAIL_TABLE)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                if (!parentCollectionSnapshot.isEmpty) {
                    for (parentDoc in parentCollectionSnapshot.documents) {

                        val sortedSubcollectionRef : Query =
                            parentDoc.reference.collection(OFFER_PRODUCT_DETAIL_TABLE)
                                .orderBy("updateTimeStamp", Query.Direction.DESCENDING)
    
                        sortedSubcollectionRef
                            .get().addOnSuccessListener { querySnapshot ->
                                println(querySnapshot.isEmpty)
                                if (!querySnapshot.isEmpty) {
                                    var prodCount = 0
                                    for (doc in querySnapshot) {
                                        val endDateString = doc.getString("productOfferEdDate")
                                        val shopLocation = doc.getString("shopCity")
                                        if (endDateString != null && shopLocation != null) {
                                            if (isOfferActive(endDateString) && shopLocation == location) {
                                                productList.add(
                                                    prodCount,
                                                    OfferProductDetails.fromQuerySnapshot(doc)
                                                )
                                                prodCount++
                                                println(productList)
                                            }
                                        }
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
                } else {
                    complete(false)
                }
            }.addOnFailureListener {
                Log.d("EXEC", it.localizedMessage.toString())
                complete(false)
            }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllProductDetails(
        productList : MutableList<OfferProductDetails>,
        complete : (Boolean) -> Unit
    ) {
        
        val parentCollectionRef = fStore.collection(PRODUCT_DETAIL_TABLE)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                if (!parentCollectionSnapshot.isEmpty) {
                    for (parentDoc in parentCollectionSnapshot.documents) {
                        
                        val subcollectionRef =
                            parentDoc.reference.collection(OFFER_PRODUCT_DETAIL_TABLE)
                                .orderBy("updateTimeStamp", Query.Direction.DESCENDING)
                        subcollectionRef
//                            .orderBy("updateTimeStamp", Query.Direction.DESCENDING)
                            .get().addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    var prodCount = 0
                                    for (doc in querySnapshot) {
                                        val endDateString = doc.getString("productOfferEdDate")
                                        if (endDateString != null) {
                                            if (isOfferActive(endDateString)) {
                                                productList.add(
                                                    prodCount,
                                                    OfferProductDetails.fromQuerySnapshot(doc)
                                                )
                                                prodCount++
                                            }
                                        }
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
        
        val parentCollectionRef = fStore.collection(PRODUCT_DETAIL_TABLE).document(userId)
        GlobalScope.launch(Dispatchers.IO) {
            parentCollectionRef.get().addOnSuccessListener { parentCollectionSnapshot ->
                val subcollectionRef : Query =
                    parentCollectionSnapshot.reference.collection(OFFER_PRODUCT_DETAIL_TABLE)
                        .orderBy("updateTimeStamp", Query.Direction.DESCENDING)
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
//
//    private fun getRandomString(length:Int) : String{
//        val charset = ('a'..'z') + ('A'..'Z')+('0'..'9')
//
//        return (1..length)
//            .map { charset.random() }
//            .joinToString("")
//    }
}