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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.FragmentCustomerProfileBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.PRODUCT_DETAIL_TITLE
import com.example.offersatyoursteps.utilities.PROFILE_TITLE
import com.example.offersatyoursteps.utilities.USER_INFO

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerProfileFragment : Fragment() {

    private lateinit var binding : FragmentCustomerProfileBinding
    private lateinit var backPressedCallback : OnBackPressedCallback
    
    private lateinit var custUserName : EditText
    private lateinit var custStreetName : EditText
    private lateinit var custCity : AutoCompleteTextView
    private lateinit var custDistrict : AutoCompleteTextView
    private lateinit var custState : AutoCompleteTextView
    private lateinit var custPincode : EditText
    
    private lateinit var profileProgress : ProgressBar
    
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
        
        binding = FragmentCustomerProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        
        custUserName = binding.profileCustName
        custStreetName = binding.profileCustStreetName
        custCity = binding.profileCity
        custDistrict = binding.profileDistrict
        custState = binding.profileState
        custPincode = binding.profilePincode
        
        profileProgress = binding.profileProgressBar
        profileProgress.visibility = View.INVISIBLE
    
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = PROFILE_TITLE
        
        return view
    }
    
    companion object {
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            CustomerProfileFragment().apply {
                arguments = Bundle().apply {
                putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val cityList = resources.getStringArray(R.array.CityList)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, cityList)
        binding!!.profileCity.setAdapter(cityAdapter)
        
        val districtList = resources.getStringArray(R.array.DistrictList)
        val districtAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, districtList)
        binding!!.profileDistrict.setAdapter(districtAdapter)
        
        val stateList = resources.getStringArray(R.array.StateList)
        val stateAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, stateList)
        binding!!.profileState.setAdapter(stateAdapter)

        custUserName.setText(userModel.cName)
        custStreetName.setText(userModel.cStreetName)
        custCity.setText(userModel.cCity, false)
        custDistrict.setText(userModel.cDistrict, false)
        custState.setText(userModel.cState, false)
        custPincode.setText(userModel.cPincode)
    
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
    
    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }
    
}