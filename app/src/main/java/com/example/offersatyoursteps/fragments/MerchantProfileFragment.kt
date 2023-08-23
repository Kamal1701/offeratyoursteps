package com.example.offersatyoursteps.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.FragmentMerchantProfileBinding
import com.example.offersatyoursteps.models.NavigationHeaderViewModel
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.PROFILE_TITLE
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth


class MerchantProfileFragment : Fragment() {
    
    private lateinit var binding : FragmentMerchantProfileBinding
    private lateinit var backPressedCallback : OnBackPressedCallback
    private val navHeaderChangeNotify : NavigationHeaderViewModel by activityViewModels<NavigationHeaderViewModel>()
    
    private lateinit var mtShopName: EditText
    private lateinit var mtUserName: EditText
    private lateinit var mtStreetName : EditText
    private lateinit var mtCity: AutoCompleteTextView
    private lateinit var mtDistrict: AutoCompleteTextView
    private lateinit var mtState: AutoCompleteTextView
    private lateinit var mtPincode: EditText
    private lateinit var updateBtn : Button
    private lateinit var profileProgress : ProgressBar
    
    private lateinit var mAuth : FirebaseAuth
    private var userModel = UserModel("", "", "", "", "", "","","","")
    
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
        mtStreetName = binding.mStreetName
        mtCity = binding.mprofileCity
        mtDistrict = binding.mprofileDistrict
        mtState = binding.mprofileState
        mtPincode = binding.mprofilePincode
        updateBtn = binding.updatemProfileBtn
        
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
        
        updateCustomerInfoUI()
    
        val userId = mAuth.currentUser!!.uid
    
        updateBtn.setOnClickListener {
            userModel.customerName = mtUserName.text.toString()
            userModel.customerShopName = mtShopName.text.toString()
            userModel.customerStreetName = mtStreetName.text.toString()
            userModel.customerCity = mtCity.text.toString()
            userModel.customerDistrict = mtDistrict.text.toString()
            userModel.customerState = mtState.text.toString()
            userModel.customerPincode = mtPincode.text.toString()
            profileProgress.visibility = View.VISIBLE
            updateBtn.visibility = View.INVISIBLE
            DatabaseServices.updateCustomerInfoRecord(userId, userModel){ isUpdateSuccess ->
                if (isUpdateSuccess){
                    profileProgress.visibility = View.INVISIBLE
                    updateBtn.visibility = View.VISIBLE
                    navHeaderChangeNotify.setUserName(userModel.customerName!!)
                    Toast.makeText(activity, "User profile updated successfully", Toast.LENGTH_LONG)
                        .show()
                } else{
                    profileProgress.visibility = View.INVISIBLE
                    updateBtn.visibility = View.VISIBLE
                    Toast.makeText(activity, "Unable to update now, please try later", Toast.LENGTH_LONG)
                        .show()
                }
        
            }
        }
    
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
    
    fun updateCustomerInfoUI(){
        mtUserName.setText(userModel.customerName)
        mtShopName.setText(userModel.customerShopName)
        mtStreetName.setText(userModel.customerStreetName)
        mtCity.setText(userModel.customerCity)
        mtDistrict.setText(userModel.customerDistrict)
        mtState.setText(userModel.customerState)
        mtPincode.setText(userModel.customerPincode)
    }
}