package com.example.offersatyoursteps.adapters

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.offersatyoursteps.models.OfferDetails
import com.example.offersatyoursteps.databinding.OfferListViewBinding
import com.example.offersatyoursteps.models.OfferProductDetails

class OfferAdapter(val context:Context, val offerDetails : MutableList<OfferProductDetails>):RecyclerView.Adapter<OfferAdapter.ViewHolder>() {
    
    inner class ViewHolder(val binding : OfferListViewBinding):RecyclerView.ViewHolder(binding.root){
        private val offerImage = binding.offerProductImg
        private val prodName =  binding.offerProductName
        private val actPrice = binding.offerActualPrice
        private val discPrice = binding.offerDiscountPrice
        private val discPercentage = binding.offerSavePercentage
        
        fun bindingOffers(context : Context, offerDetails : OfferProductDetails){
            
            Glide.with(context).load(offerDetails.imgName).into(offerImage)
            prodName.text =offerDetails.productName
            actPrice.text = offerDetails.actualPrice
            actPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            discPrice.text = offerDetails.discountPrice
            discPercentage.text = offerDetails.discountPercentage
        }
    }
    
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val binding = OfferListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        holder?.bindingOffers(context,offerDetails[position])
    }
    
    override fun getItemCount() : Int {
        return offerDetails.size
    }
}

