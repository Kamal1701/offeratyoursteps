package com.example.offersatyoursteps.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.offersatyoursteps.activities.models.OfferDetails
import com.example.offersatyoursteps.databinding.OfferListViewBinding

class OfferAdapter(val context:Context, val offerDetails : ArrayList<OfferDetails>):RecyclerView.Adapter<OfferAdapter.ViewHolder>() {
    
    inner class ViewHolder(val binding : OfferListViewBinding):RecyclerView.ViewHolder(binding.root){
        val offerImage = binding.offerProductImg
        val prodName =  binding.offerProductName
        val actPrice = binding.offerActualPrice
        val discPrice = binding.offerDiscountPrice
        val discPercentage = binding.offerSavePercentage
        
        fun bindingOffers(context : Context, offerDetails : OfferDetails){
            val resourceId = context.resources.getIdentifier(offerDetails.imgName, "drawable", context.packageName)
            offerImage.setImageResource(resourceId)
            prodName.text =offerDetails.productName
            actPrice.text = offerDetails.actualPrice
            discPrice.text = offerDetails.discountPrice
            discPercentage.text = offerDetails.discountPercentage
        }
    }
    
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : OfferAdapter.ViewHolder {
        val binding = OfferListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder : OfferAdapter.ViewHolder, position : Int) {
        holder?.bindingOffers(context,offerDetails[position])
    }
    
    override fun getItemCount() : Int {
        return offerDetails.size
    }
}

