package com.example.offersatyoursteps.adapters

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.offersatyoursteps.databinding.OfferListViewBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.utilities.RUPEE_SYMBOL

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
            
            Glide.with(context).load(offerDetails.productImgName).into(offerImage)
            prodName.text = offerDetails.productName
//            actPrice.text = RUPEE_SYMBOL + "${offerDetails.actualPrice}"
            "$RUPEE_SYMBOL ${offerDetails.productActualPrice}".also { actPrice.text =  it }
            actPrice.paintFlags = STRIKE_THRU_TEXT_FLAG
            "$RUPEE_SYMBOL ${offerDetails.productDiscountPrice}".also { discPrice.text =  it }
            offerDetails.productDiscountPercentage.also { discPercentage.text = it }
            
            
            offerImage.setOnClickListener {
                prodItemClick(offerDetails) }
            
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

