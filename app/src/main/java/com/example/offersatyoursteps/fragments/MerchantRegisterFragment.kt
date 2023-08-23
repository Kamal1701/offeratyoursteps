package com.example.offersatyoursteps.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.activities.LoginActivity
import com.example.offersatyoursteps.databinding.FragmentMerchantRegisterBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.CUSTOMER_INFO_TABLE
import com.example.offersatyoursteps.utilities.SetTextColorSpan
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

private val IS_MERCHANT = "Y"

class MerchantRegisterFragment : Fragment() {
    
    private lateinit var binding : FragmentMerchantRegisterBinding
    private lateinit var merchantName : EditText
    private lateinit var merchantEmail : EditText
    private lateinit var merchantPassword : EditText
    private lateinit var merchantShopName : EditText
    private lateinit var merchantStreeName : EditText
    private lateinit var merchantCity : AutoCompleteTextView
    private lateinit var merchantDistrict : AutoCompleteTextView
    private lateinit var merchantState : AutoCompleteTextView
    private lateinit var merchantPincode : EditText
    private lateinit var progressBar : ProgressBar
    private lateinit var registerBtn : Button
    
//    private lateinit var backPressedCallback : OnBackPressedCallback
    
    private lateinit var mAuth : FirebaseAuth
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentMerchantRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        
        merchantName = binding.regMercUserNameTxt
        merchantEmail = binding.regMercUserEmailTxt
        merchantPassword = binding.regMercPasswordTxt
        merchantShopName = binding.regMercShopNameTxt
        merchantStreeName = binding.regMercStreetNameTxt
        merchantCity = binding.registerMercCity
        merchantDistrict = binding.registerMercDistrict
        merchantState = binding.registerMercState
        merchantPincode = binding.regMercPincodeTxt
        progressBar = binding.regMercProgressBar
        registerBtn = binding.regMercUserRegisterBtn
        
        progressBar.visibility = View.INVISIBLE
        
        mAuth = FirebaseAuth.getInstance()
        
        return view
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val cityList = resources.getStringArray(R.array.CityList)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, cityList)
        binding!!.registerMercCity.setAdapter(cityAdapter)
        
        val districtList = resources.getStringArray(R.array.DistrictList)
        val districtAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, districtList)
        binding!!.registerMercDistrict.setAdapter(districtAdapter)
        
        val stateList = resources.getStringArray(R.array.StateList)
        val stateAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, stateList)
        binding!!.registerMercState.setAdapter(stateAdapter)
        
        val backToLogin = binding.regMercLoginBtn
        val colorSpan = SetTextColorSpan(backToLogin.text.toString())
        backToLogin.text = colorSpan.setTextColorSpan()
        
        backToLogin.setOnClickListener {
            val loginActivity = Intent(activity, LoginActivity::class.java)
            startActivity(loginActivity)
            activity?.supportFragmentManager?.popBackStack()
        }
        
        registerBtn.setOnClickListener {
            
            val mName = merchantName.text.toString()
            val mEmail = merchantEmail.text.toString()
            val mPassword = merchantPassword.text.toString()
            val mShopName = merchantShopName.text.toString()
            val mStreetName = merchantStreeName.text.toString()
            val mCity = merchantCity.text.toString()
            val mDistrict = merchantDistrict.text.toString()
            val mState = merchantState.text.toString()
            val mPincode = merchantPincode.text.toString()
            
            registerBtn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            
            if (mName.isNotEmpty() || mEmail.isNotEmpty() || mPassword.isNotEmpty()
                || mShopName.isNotEmpty() || mCity.isNotEmpty() || mState.isNotEmpty()
            ) {
                
//                val merchantMap = HashMap<String, String>()
//                merchantMap["User_Name"] = mName
//                merchantMap["User_EmailId"] = mEmail
//                merchantMap["User_Password"] = mPassword
//                merchantMap["Shop_Name"] = mShopName
//                merchantMap["IsMerchant"] = "Y"
//                merchantMap["Street_Name"] = mStreetName
//                merchantMap["City"] = mCity
//                merchantMap["District"] = mDistrict
//                merchantMap["State"] = mState
//                merchantMap["Pincode"] = mPincode
    
                val merchant = UserModel(mName,
                    mEmail,
                    mShopName,
                    IS_MERCHANT,
                    mStreetName,
                    mCity,
                    mDistrict,
                    mState,
                    mPincode)
                
                mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener { task : Task<AuthResult> ->
                        if (task.isSuccessful) {
//                            Log.d("ERROR", mAuth.currentUser!!.uid)
                            val userId = mAuth.currentUser!!.uid
                            Log.d("DEBUG", userId)
                            DatabaseServices.createCustomerInfoRecord(
                                CUSTOMER_INFO_TABLE,
                                userId,
                                merchant
                            ) { isSetComplete ->
                                if (isSetComplete) {
                                    Toast.makeText(
                                        activity,
                                        "User registered successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    loadHomePage()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "Unable to register, please try again",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    
                                }
                            }
                        }
                    }
            } else {
                Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                
                registerBtn.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
            
        }
        
//        backPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                requireActivity().supportFragmentManager.popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            backPressedCallback
//        )
    }
    private fun loadHomePage() {
        
        val loginActivity = Intent(activity, LoginActivity::class.java)
        startActivity(loginActivity)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
//        backPressedCallback.remove()
    }
}