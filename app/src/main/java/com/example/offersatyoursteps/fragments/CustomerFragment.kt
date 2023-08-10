package com.example.offersatyoursteps.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.activities.HomePageActivity
import com.example.offersatyoursteps.activities.LoginActivity
import com.example.offersatyoursteps.databinding.FragmentCustomerBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.SetTextColorSpan
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class CustomerFragment : Fragment() {
    
    private lateinit var binding : FragmentCustomerBinding
    private lateinit var custName : TextView
    private lateinit var custEmail : TextView
    private lateinit var custPassword : TextView
    private lateinit var custCity : AutoCompleteTextView
    private lateinit var custState : AutoCompleteTextView
    private lateinit var progressBar : ProgressBar
    private lateinit var registerBtn : Button
    private lateinit var customerData : UserModel
    
    private lateinit var mAuth : FirebaseAuth
    private lateinit var fStore : FirebaseFirestore
    
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerBinding.inflate(inflater, container, false)
        val view = binding.root
        
        custName = binding.regCustUserNameTxt
        custEmail = binding.regCustUserEmailTxt
        custPassword = binding.regCustPasswordTxt
        custCity = binding.registerCustCity
        custState = binding.registerCustState
        progressBar = binding.regCustProgressBar
        registerBtn = binding.regCustUserRegisterBtn
        
        progressBar.visibility = View.INVISIBLE
        
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser==null){
            Toast.makeText(activity,"mAuth user is null", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(activity,"mAuth user is not null", Toast.LENGTH_LONG).show()
        }
        
        fStore = FirebaseFirestore.getInstance()
        
        return view
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val cityList = resources.getStringArray(R.array.CityList)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, cityList)
        binding!!.registerCustCity.setAdapter(cityAdapter)
        
        val stateList = resources.getStringArray(R.array.StateList)
        val stateAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, stateList)
        binding!!.registerCustState.setAdapter(stateAdapter)
        
        val backToLogin = binding.regCustLoginBtn
        val colorSpan = SetTextColorSpan(backToLogin.text.toString())
        backToLogin.text = colorSpan.setTextColorSpan()
        

//        val backToLogin = binding.regCustLoginBtn
        backToLogin.setOnClickListener {
            val loginActivity = Intent(activity, LoginActivity::class.java)
            startActivity(loginActivity)
            activity?.supportFragmentManager?.popBackStack()
        }
        
  
        
        
        registerBtn.setOnClickListener {
    
//            if(mAuth.currentUser!=null){
                var cName = custName.text.toString()
                val cEmail = custEmail.text.toString()
                val cPassword = custPassword.text.toString()
                val cCity = custCity.text.toString()
                val cState = custState.text.toString()

//            mAuth = FirebaseAuth.getInstance()
//            fStore = FirebaseFirestore.getInstance()
    
                Log.d("DEBUG", cName)
                Log.d("DEBUG", cEmail)
    
                Log.d("DEBUG", cCity)
                Log.d("DEBUG", cState)

//            if (TextUtils.isEmpty(cName)) {
//                Toast.makeText(
//                    activity,
//                    "Please fill user name field",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            if (TextUtils.isEmpty(cEmail)) {
//                Toast.makeText(
//                    activity,
//                    "Please fill email field",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            if (TextUtils.isEmpty(cPassword)) {
//                Toast.makeText(
//                    activity,
//                    "Please fill password field",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            if (TextUtils.isEmpty(cCity)) {
//                Toast.makeText(
//                    activity,
//                    "Please fill city field",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            if (TextUtils.isEmpty(cState)) {
//                Toast.makeText(
//                    activity,
//                    "Please fill state field",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
                registerBtn.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
//            customerData = UserModel(cName, cEmail, cPassword, "NA", "N", cCity, cState)
//            val customerMap = customerData.customerJson()
                val customerMap = HashMap<String, String>()
                customerMap["User_Name"] = cName
                customerMap["User_EmailId"] = cEmail
                customerMap["User_Password"] = cPassword
                customerMap["Shop_Name"] = "NA"
                customerMap["IsMerchant"] = "N"
                customerMap["City"] = cCity
                customerMap["State"] = cState
    

    
                if (cName.isNotEmpty() && cEmail.isNotEmpty() && cPassword.isNotEmpty() &&
                    cCity.isNotEmpty() && cState.isNotEmpty()
                ) {
                    mAuth.createUserWithEmailAndPassword(cEmail, cPassword)
                        .addOnCompleteListener { task : Task<AuthResult> ->
                            if (task.isSuccessful) {
                                Log.d("ERROR", mAuth.currentUser!!.uid)
                                val userId = mAuth.currentUser!!.uid
                                Log.d("DEBUG", userId)
                                fStore.collection("CustomerInfo").document(userId).set(customerMap)
                                    .addOnCompleteListener {
                                        Toast.makeText(
                                            activity,
                                            "User registered successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
//                                    custName.text = ""
//                                    custEmail.text = ""
//                                    custPassword.text = ""
//                                    custCity.text.clear()
//                                    custState.text.clear()
                                    loadHomePage()
//                                        getDatafromFirestore()
                            
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            activity,
                                            "Unable to register, please try again",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.d("EXEC", it.localizedMessage)
                                    }
                            }
//                            else {
//                                Toast.makeText(activity, task.exception.toString(), Toast.LENGTH_SHORT)
//                                    .show()
//                                Log.d("EXEC", task.exception.toString())
////                                Log.d("EXEC", task.result.toString())
//                                registerBtn.visibility = View.VISIBLE
//                                progressBar.visibility = View.INVISIBLE
//                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_SHORT).show()
                            Log.d("EXEC", it.localizedMessage)
                            registerBtn.visibility = View.VISIBLE
                            progressBar.visibility = View.INVISIBLE
                        }
                } else {
                    Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_SHORT)
                        .show()
    
                    registerBtn.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
        
//            } else{
//                Toast.makeText(activity,"Current user is null", Toast.LENGTH_LONG).show()
//            }
        }
    }
    
    private fun loadHomePage() {
        val homeActivityIntent = Intent(activity, HomePageActivity::class.java)
        startActivity(homeActivityIntent)
//        finish()
//        activity?.supportFragmentManager?.popBackStack()
    }
    
    private fun getDatafromFirestore(){
        val userId = mAuth.currentUser!!.uid
        fStore.collection("CustomerInfo").document(userId)
            .get().addOnSuccessListener { customerDocument ->
                if(customerDocument != null){
                    val name = customerDocument.get("User_Name")
                    Log.d("DEBUG", name.toString())
                } else{
                    Log.d("DEBUG", "No such document")
                }
            }
            .addOnFailureListener {
                Log.d("DEBUG", it.localizedMessage)
            }
    }
}