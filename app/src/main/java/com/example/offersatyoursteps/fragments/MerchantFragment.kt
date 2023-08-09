package com.example.offersatyoursteps.fragments

import android.content.Intent
import android.os.Bundle
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
import com.example.offersatyoursteps.activities.LoginActivity
import com.example.offersatyoursteps.databinding.FragmentMerchantBinding
import com.example.offersatyoursteps.utilities.SetTextColorSpan

class MerchantFragment : Fragment() {
    
    private lateinit var binding: FragmentMerchantBinding
    private lateinit var merchantName : TextView
    private lateinit var merchantEmail : TextView
    private lateinit var merchantPassword : TextView
    private lateinit var merchantShopName : TextView
    private lateinit var merchantCity : AutoCompleteTextView
    private lateinit var merchantState : AutoCompleteTextView
    private lateinit var progressBar: ProgressBar
    private lateinit var registerBtn: Button
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentMerchantBinding.inflate(inflater, container, false)
        val view = binding.root
        
        merchantName = binding.regMercUserNameTxt
        merchantEmail = binding.regMercUserEmailTxt
        merchantPassword = binding.regMercPasswordTxt
        merchantShopName = binding.regMercShopNameTxt
        merchantCity = binding.registerMercCity
        merchantState = binding.registerMercState
        progressBar = binding.regMercProgressBar
        registerBtn = binding.regMercUserRegisterBtn
        
        progressBar.visibility = View.INVISIBLE
        
        return view
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val mName = merchantName.text.toString()
        val mEmail = merchantEmail.text.toString()
        val mPassword = merchantPassword.text.toString()
        val mShopName = merchantShopName.text.toString()
        val mCity = merchantCity.text.toString()
        val mState = merchantState.text.toString()
        
        val cityList = resources.getStringArray(R.array.CityList)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, cityList)
        binding!!.registerMercCity.setAdapter(cityAdapter)
        
        val stateList = resources.getStringArray(R.array.StateList)
        val stateAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, stateList)
        binding!!.registerMercState.setAdapter(stateAdapter)
    
        val backToLogin = binding.regMercLoginBtn
        val colorSpan = SetTextColorSpan(backToLogin.text.toString())
        backToLogin.text = colorSpan.setTextColorSpan()
    
//        val backToLogin = binding.regMercLoginBtn
        backToLogin.setOnClickListener {
            val loginActivity = Intent(activity, LoginActivity::class.java)
            startActivity(loginActivity)
            activity?.supportFragmentManager?.popBackStack()
        }
        
        registerBtn.setOnClickListener {
            if(mName.isNotEmpty()||mEmail.isNotEmpty()||mPassword.isNotEmpty()
                ||mShopName.isNotEmpty()||mCity.isNotEmpty()||mState.isNotEmpty()){
                registerBtn.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
        
            } else {
                Toast.makeText(activity, "Please fill all the details", Toast.LENGTH_SHORT).show()
                registerBtn.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
    }
}