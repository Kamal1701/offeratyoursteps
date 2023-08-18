package com.example.offersatyoursteps.fragments

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.offersatyoursteps.databinding.FragmentProductDetailsBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.utilities.EXTRA_PRODUCT

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProductDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding:FragmentProductDetailsBinding
    private lateinit var offerProductDetails : OfferProductDetails
    
    
    private lateinit var prodImage : ImageView
    private lateinit var brandName: TextView
    private lateinit var prodName: TextView
    private lateinit var actualPrice: TextView
    private lateinit var discountPrice: TextView
    private lateinit var discountPercentage: TextView
    private lateinit var prodWeight: TextView
    private lateinit var shopName: TextView
    private lateinit var shopCity: TextView
    private lateinit var shopState: TextView
    
    private lateinit var backPressedCallback : OnBackPressedCallback
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            offerProductDetails = it?.getParcelable<OfferProductDetails>(EXTRA_PRODUCT)!!
        }
    }
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
    
        prodImage = binding.productDetailImg
        
        brandName = binding.productDetailBrandTxt
        prodName = binding.productDetailNameTxt
        actualPrice = binding.prodDetailActualPrice
        discountPrice = binding.prodDetailDiscountPrice
        discountPercentage = binding.prodDetailDiscountPer
        prodWeight = binding.prodDetailWeight
        shopName = binding.prodDetailShopName
        shopCity = binding.prodDetailCity
        shopState = binding.prodDetailState
        
        return binding.root
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Glide.with(this)
            .load(offerProductDetails.imgName)
            .into(prodImage)
        
        brandName.text = offerProductDetails.brandName
        prodName.text = offerProductDetails.productName
        actualPrice.text = offerProductDetails.actualPrice
        actualPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        discountPrice.text = offerProductDetails.discountPrice
        discountPercentage.text = offerProductDetails.discountPercentage
        prodWeight.text = offerProductDetails.prodWeight
        shopName.text = offerProductDetails.productName
        shopCity.text = offerProductDetails.location
        shopState.text = offerProductDetails.location
    
        backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }
    
    companion object {
        
        @JvmStatic
        fun newInstance(offerProductDetails : OfferProductDetails) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_PRODUCT, offerProductDetails)
                }
            }
    }
}