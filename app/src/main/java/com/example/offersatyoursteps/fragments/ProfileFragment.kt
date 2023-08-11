package com.example.offersatyoursteps.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.FragmentProfileBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 : String? = null
    private var param2 : String? = null
    private lateinit var binding : FragmentProfileBinding
    
    private lateinit var custUserName : EditText
    private lateinit var custCity : AutoCompleteTextView
    private lateinit var custDistrict : AutoCompleteTextView
    private lateinit var custState : AutoCompleteTextView
    private lateinit var custPincode : EditText
    
    private lateinit var profileProgress : ProgressBar
    
    private lateinit var mAuth : FirebaseAuth
    private lateinit var userModel : UserModel
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            userModel = arguments?.getParcelable<UserModel>(USER_INFO)!!
        }

    }
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        
        custUserName = binding.profileCustName
        custCity = binding.profileCity
        custDistrict = binding.profileDistrict
        custState = binding.profileState
        custPincode = binding.profilePincode
        
        profileProgress = binding.profileProgressBar
        profileProgress.visibility = View.INVISIBLE
    
//        Log.d("DEBUG", "ProfileFragment")
//        userModel = arguments?.getParcelable<UserModel>(USER_INFO)!!
//        Log.d("DEBUG", "ProfileFragment")
//        Log.d("DEBUG", userModel.cName.toString())
//        Log.d("DEBUG", userModel.cEmail.toString())


//        mAuth = FirebaseAuth.getInstance()

//        return inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }
    
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            ProfileFragment().apply {
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

//        val userId = mAuth.currentUser!!.uid
//        DatabaseServices.getCustomerInfoRecord("CustomerInfo", userId, userModel){ dbSuccess ->
////                        Log.d("DEBUG", "LoginActivity")
////                        Log.d("DEBUG", userModel.cName.toString())
////                        Log.d("DEBUG", userModel.cEmail.toString())
//            if(dbSuccess){
//                custUserName.setText(userModel.cName)
//                custCity.setText(userModel.cCity,false)
//                custState.setText(userModel.cState,false)
//            }
//        }
        custUserName.setText(userModel.cName)
        custCity.setText(userModel.cCity, false)
        custState.setText(userModel.cState, false)
        
    }
    
}