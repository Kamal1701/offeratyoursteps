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
import com.example.offersatyoursteps.databinding.FragmentCustomerBinding
import com.example.offersatyoursteps.utilities.SetTextColorSpan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CustomerFragment : Fragment() {
    
    private lateinit var binding: FragmentCustomerBinding
    private lateinit var custName : TextView
    private lateinit var custEmail : TextView
    private lateinit var custPassword : TextView
    private lateinit var custCity : AutoCompleteTextView
    private lateinit var custState : AutoCompleteTextView
    private lateinit var progressBar: ProgressBar
    private lateinit var registerBtn: Button
    
    private lateinit var mAuth : FirebaseAuth
    private lateinit var fStore:FirebaseFirestore
    


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
        
        return view
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val cName = custName.text.toString()
        val cEmail = custEmail.text.toString()
        val cPassword = custPassword.text.toString()
        val cCity = custCity.text.toString()
        val cState = custState.text.toString()
    
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
            if(cName.isNotEmpty()||cEmail.isNotEmpty()||cPassword.isNotEmpty()
                ||cCity.isNotEmpty()||cState.isNotEmpty()){
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