package com.example.offersatyoursteps.services

import android.util.Log
import com.example.offersatyoursteps.models.UserModel
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseServices {
    
    private lateinit var fStore : FirebaseFirestore
    
    fun getCustomerInfoRecord(collectPath:String, userId:String, userModel : UserModel,  complete : (Boolean)->Unit){
        fStore = FirebaseFirestore.getInstance()
        fStore.collection(collectPath).document(userId)
            .get().addOnSuccessListener {custDoc ->
                if(custDoc != null){
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
    
    fun createCustomerInfoRecord(collectPath:String, userId:String, userMap : HashMap<String, String>, complete : (Boolean) -> Unit){
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
    
}