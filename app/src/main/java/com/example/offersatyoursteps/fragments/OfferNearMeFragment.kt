package com.example.offersatyoursteps.fragments

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.adapters.OfferAdapter
import com.example.offersatyoursteps.adapters.ProductSubcategoryAdapter
import com.example.offersatyoursteps.utilities.OfferConstants
import com.example.offersatyoursteps.databinding.FragmentOfferNearMeBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.services.Dataservices
import com.example.offersatyoursteps.utilities.SPAN_COUNT
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class OfferNearMeFragment : Fragment() {
    
    private lateinit var userModel : UserModel
    private lateinit var binding : FragmentOfferNearMeBinding
    private lateinit var mAuth : FirebaseAuth
    private var productList : MutableList<OfferProductDetails> = mutableListOf()

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
        binding = FragmentOfferNearMeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    
    companion object {
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            OfferNearMeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser!!.uid
        
        DatabaseServices.getProductDetailsRecord("Product_Details",userId,productList){
            isGetComplete ->
            if(isGetComplete){
//                println(productList)
                val itemAdapter = OfferAdapter(this.requireContext(), productList)
                val offerRecycleView = binding.offerNearMeRecycleView
                offerRecycleView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
                offerRecycleView.adapter = itemAdapter
            } else{
                Log.d("DEBUG", "OfferNearMe - no record returned")
            }
        }
//        val itemAdapter = OfferAdapter(this.requireContext(), Dataservices.getProducts(userModel.prodSubcategory))
//        val offerRecycleView = binding.offerNearMeRecycleView
//        offerRecycleView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
//        offerRecycleView.adapter = itemAdapter
    }
}