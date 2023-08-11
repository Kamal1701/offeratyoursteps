package com.example.offersatyoursteps.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.FragmentMerchantBinding
import com.example.offersatyoursteps.databinding.FragmentMerchantProfileBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.google.firebase.auth.FirebaseAuth


class MerchantProfileFragment : Fragment() {
    
    private lateinit var binding : FragmentMerchantProfileBinding
    
    private lateinit var mtShopName: EditText
    private lateinit var mtUserName: EditText
    private lateinit var mtCity: AutoCompleteTextView
    private lateinit var mtDistrict: AutoCompleteTextView
    private lateinit var mtState: AutoCompleteTextView
    private lateinit var mtPincode: EditText
    private lateinit var profileProgress : ProgressBar
    
    private lateinit var mAuth : FirebaseAuth
    private var userModel = UserModel("", "", "", "", "", "")
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

    }
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentMerchantProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        
        mtShopName = binding.mShopName
        mtUserName = binding.mCustName
        mtCity = binding.mprofileCity
        mtDistrict = binding.mprofileDistrict
        mtState = binding.mprofileState
        mtPincode = binding.mprofilePincode
        
        mAuth = FirebaseAuth.getInstance()
        
        profileProgress = binding.mprofileProgressBar
        profileProgress.visibility = View.INVISIBLE
        
        return view
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityList = resources.getStringArray(R.array.CityList)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, cityList)
        binding!!.mprofileCity.setAdapter(cityAdapter)
    
        val districtList = resources.getStringArray(R.array.DistrictList)
        val districtAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, districtList)
        binding!!.mprofileDistrict.setAdapter(districtAdapter)
    
        val stateList = resources.getStringArray(R.array.StateList)
        val stateAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, stateList)
        binding!!.mprofileState.setAdapter(stateAdapter)
    
    
        val userId = mAuth.currentUser!!.uid
        DatabaseServices.getCustomerInfoRecord("CustomerInfo", userId, userModel){ dbSuccess ->
//                        Log.d("DEBUG", "LoginActivity")
//                        Log.d("DEBUG", userModel.cName.toString())
//                        Log.d("DEBUG", userModel.cEmail.toString())
            if(dbSuccess){
                mtUserName.setText(userModel.cName)
                mtCity.setText(userModel.cCity,false)
                mtState.setText(userModel.cState,false)
            }
        }
    }
    
}