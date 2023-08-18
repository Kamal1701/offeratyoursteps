package com.example.offersatyoursteps.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.activities.LoginActivity
import com.example.offersatyoursteps.databinding.FragmentMerchantProfileBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.PROFILE_TITLE
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth


class MerchantProfileFragment : Fragment() {
    
    private lateinit var binding : FragmentMerchantProfileBinding
    private lateinit var backPressedCallback : OnBackPressedCallback
    
    private lateinit var mtShopName: EditText
    private lateinit var mtUserName: EditText
    private lateinit var mtCity: AutoCompleteTextView
    private lateinit var mtDistrict: AutoCompleteTextView
    private lateinit var mtState: AutoCompleteTextView
    private lateinit var mtPincode: EditText
    private lateinit var profileProgress : ProgressBar
    
    private lateinit var mAuth : FirebaseAuth
    private var userModel = UserModel("", "", "", "", "", "","")
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userModel = it?.getParcelable<UserModel>(USER_INFO)!!
        }
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
    
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = PROFILE_TITLE
        
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
        
        mtShopName.setText(userModel.cShopName)
        mtUserName.setText(userModel.cName)
        mtCity.setText(userModel.cCity)
        mtState.setText(userModel.cState)
    
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack("NearMe",0)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        
    }
    
    companion object{
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            MerchantProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }
    
}