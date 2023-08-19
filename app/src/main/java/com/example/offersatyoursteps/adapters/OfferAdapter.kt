package com.example.offersatyoursteps.adapters

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.offersatyoursteps.models.OfferDetails
import com.example.offersatyoursteps.databinding.OfferListViewBinding
import com.example.offersatyoursteps.models.OfferProductDetails

class OfferAdapter(
    val context : Context,
    val offerDetails : MutableList<OfferProductDetails>,
    val prodItemClick : (OfferProductDetails) -> Unit
) : RecyclerView.Adapter<OfferAdapter.ViewHolder>() {
    
    inner class ViewHolder(
        val binding : OfferListViewBinding,
        val prodItemClick : (OfferProductDetails) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var offerImage = binding.offerProductImg
        var prodName = binding.offerProductName
        private var actPrice = binding.offerActualPrice
        private var discPrice = binding.offerDiscountPrice
        private var discPercentage = binding.offerSavePercentage
        
        fun bindingOffers(context : Context, offerDetails : OfferProductDetails) {
            
            Glide.with(context).load(offerDetails.imgName).into(offerImage)
            prodName.text = offerDetails.productName
            actPrice.text = offerDetails.actualPrice
            actPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            discPrice.text = offerDetails.discountPrice
            discPercentage.text = "${offerDetails.discountPercentage}%"
            
            
            offerImage.setOnClickListener { prodItemClick(offerDetails) }
            
        }
    }
    
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val binding =
            OfferListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, prodItemClick)
    }
    
    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        holder?.bindingOffers(context, offerDetails[position])
    }
    
    override fun getItemCount() : Int {
        return offerDetails.size
    }
}

