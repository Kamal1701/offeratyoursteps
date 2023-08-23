package com.example.offersatyoursteps.fragments

import android.content.Intent
import android.content.pm.PackageManager.ComponentEnabledSetting
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
import androidx.fragment.app.FragmentManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.activities.LoginActivity
import com.example.offersatyoursteps.databinding.FragmentCustomerRegisterBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.CUSTOMER_INFO_TABLE
import com.example.offersatyoursteps.utilities.SetTextColorSpan
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

private val IS_MERCHANT = "N"
private val SHOPNAME_NA = "NA"
class CustomerRegisterFragment : Fragment() {
    
    private lateinit var binding : FragmentCustomerRegisterBinding
    
    private lateinit var custName : EditText
    private lateinit var custEmail : EditText
    private lateinit var custPassword : EditText
    private lateinit var custStreetName : EditText
    private lateinit var custDistrict : EditText
    private lateinit var custCity : AutoCompleteTextView
    private lateinit var custState : AutoCompleteTextView
    private lateinit var custPincode : EditText
    private lateinit var progressBar : ProgressBar
    private lateinit var registerBtn : Button
    
//    private lateinit var backPressedCallback : OnBackPressedCallback
    
    private lateinit var mAuth : FirebaseAuth
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        
        custName = binding.regCustUserNameTxt
        custEmail = binding.regCustUserEmailTxt
        custPassword = binding.regCustPasswordTxt
        custStreetName = binding.regStreetNameTxt
        custCity = binding.registerCustCity
        custDistrict = binding.registerCustDistrict
        custState = binding.registerCustState
        custPincode = binding.regPincodeTxt
        progressBar = binding.regCustProgressBar
        registerBtn = binding.regCustUserRegisterBtn
        
        progressBar.visibility = View.INVISIBLE
        
        mAuth = FirebaseAuth.getInstance()
        
        return view
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val cityList = resources.getStringArray(R.array.CityList)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, cityList)
        binding!!.registerCustCity.setAdapter(cityAdapter)
        
        val districtList = resources.getStringArray(R.array.DistrictList)
        val districtAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, districtList)
        binding!!.registerCustDistrict.setAdapter(districtAdapter)
        
        val stateList = resources.getStringArray(R.array.StateList)
        val stateAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, stateList)
        binding!!.registerCustState.setAdapter(stateAdapter)
        
        val backToLogin = binding.regCustLoginBtn
        val colorSpan = SetTextColorSpan(backToLogin.text.toString())
        backToLogin.text = colorSpan.setTextColorSpan()

        backToLogin.setOnClickListener {
            val loginActivity = Intent(activity, LoginActivity::class.java)
            startActivity(loginActivity)
            activity?.supportFragmentManager?.popBackStack()
        }
        
        registerBtn.setOnClickListener {

            var cName = custName.text.toString()
            val cEmail = custEmail.text.toString()
            val cPassword = custPassword.text.toString()
            val cStreetName = custStreetName.text.toString()
            val cCity = custCity.text.toString()
            val cDistrict = custDistrict.text.toString()
            val cState = custState.text.toString()
            val cPincode = custPincode.text.toString()
            
            registerBtn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            
            if (cName.isNotEmpty() && cEmail.isNotEmpty() && cPassword.isNotEmpty() &&
                cCity.isNotEmpty() && cState.isNotEmpty()
            ) {
                
                val customer = UserModel(cName,
                    cEmail,
                    SHOPNAME_NA,
                    IS_MERCHANT,
                    cStreetName,
                    cCity,
                    cDistrict,
                    cState,
                    cPincode)
                
                mAuth.createUserWithEmailAndPassword(cEmail, cPassword)
                    .addOnCompleteListener { task : Task<AuthResult> ->
                        if (task.isSuccessful) {
                            val userId = mAuth.currentUser!!.uid
                            DatabaseServices.createCustomerInfoRecord(
                                userId,
                                customer
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
                    .addOnFailureListener {
                        Log.d("EXEC", it.localizedMessage)
                        Toast.makeText(
                            activity,
                            it.localizedMessage.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        registerBtn.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
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
    
    private fun enableSpinner(enabled : Boolean){
        if(enabled){
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
        
        registerBtn.isEnabled = !enabled
    }
}