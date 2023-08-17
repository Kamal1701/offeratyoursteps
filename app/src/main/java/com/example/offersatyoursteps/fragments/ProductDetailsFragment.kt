package com.example.offersatyoursteps.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.offersatyoursteps.databinding.FragmentProductDetailsBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.utilities.EXTRA_PRODUCT

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProductDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding:FragmentProductDetailsBinding
    private lateinit var offerProductDetails : OfferProductDetails
    
    private lateinit var brandName: TextView
    private lateinit var prodName: TextView
    private lateinit var actualPrice: TextView
    private lateinit var discountPrice: TextView
    private lateinit var discountPercentage: TextView
    private lateinit var prodWeight: TextView
    private lateinit var shopName: TextView
    private lateinit var shopCity: TextView
    private lateinit var shopState: TextView
    
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
        
        brandName.text = offerProductDetails.brandName
        prodName.text = offerProductDetails.productName
        actualPrice.text = offerProductDetails.actualPrice
        discountPrice.text = offerProductDetails.discountPrice
        discountPercentage.text = offerProductDetails.discountPercentage
        prodWeight.text = offerProductDetails.prodWeight
        shopName.text = offerProductDetails.productName
        shopCity.text = offerProductDetails.location
        shopState.text = offerProductDetails.location
        
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