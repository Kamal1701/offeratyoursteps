package com.example.offersatyoursteps.fragments

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.FragmentProductDetailsBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.utilities.EXTRA_PRODUCT
import com.example.offersatyoursteps.utilities.PRODUCT_DETAIL_TITLE
import com.example.offersatyoursteps.utilities.RUPEE_SYMBOL
import com.example.offersatyoursteps.utilities.USER_INFO

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProductDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding:FragmentProductDetailsBinding
    private lateinit var offerProductDetails : OfferProductDetails
    private lateinit var userModel : UserModel
    
    
    private lateinit var prodImage : ImageView
    private lateinit var brandName: TextView
    private lateinit var prodName: TextView
    private lateinit var actualPrice: TextView
    private lateinit var discountPrice: TextView
    private lateinit var discountPercentage: TextView
    private lateinit var prodWeight: TextView
    private lateinit var offerEndDate: TextView
    private lateinit var shopName: TextView
    private lateinit var shopStName: TextView
    private lateinit var shopCity: TextView
    private lateinit var shopDistrict: TextView
    private lateinit var shopState: TextView
    
    private lateinit var backPressedCallback : OnBackPressedCallback
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userModel = it?.getParcelable<UserModel>(USER_INFO)!!
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
        offerEndDate = binding.prodDetailOfferEnd
        shopName = binding.prodDetailShopName
        shopStName = binding.prodStreetName
        shopCity = binding.prodDetailCity
        shopDistrict = binding.prodDetailDistrict
        shopState = binding.prodDetailState
    
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = PRODUCT_DETAIL_TITLE
        
        return binding.root
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Glide.with(this)
            .load(offerProductDetails.productImgName)
            .into(prodImage)
        
        brandName.text = offerProductDetails.productBrandName
        prodName.text = offerProductDetails.productName
        "$RUPEE_SYMBOL ${offerProductDetails.productActualPrice}".also { actualPrice.text = it }
        actualPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        "$RUPEE_SYMBOL ${offerProductDetails.productDiscountPrice}".also { discountPrice.text = it }
        if (offerProductDetails.productDiscountPercentage !="null"){
            "${offerProductDetails.productDiscountPercentage}".also { discountPercentage.text = it }
        } else{
            discountPercentage.text = "0%"
        }
        
        prodWeight.text = offerProductDetails.productWeight
        if(offerProductDetails.productOfferEdDate != "null"){
            offerEndDate.text = offerProductDetails.productOfferEdDate
        } else{
            offerEndDate.text = ""
        }
        
        shopName.text = offerProductDetails.shopName
        shopStName.text = offerProductDetails.shopStreetName
        shopCity.text = offerProductDetails.shopCity
        shopDistrict.text = offerProductDetails.shopDistrict
        shopState.text = offerProductDetails.shopState
    
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
        fun newInstance(userModel : UserModel, offerProductDetails : OfferProductDetails) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                    putParcelable(EXTRA_PRODUCT, offerProductDetails)
                }
            }
    }
}