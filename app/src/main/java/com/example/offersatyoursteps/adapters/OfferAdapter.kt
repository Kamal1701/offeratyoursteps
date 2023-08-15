package com.example.offersatyoursteps.adapters

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.offersatyoursteps.models.OfferDetails
import com.example.offersatyoursteps.databinding.OfferListViewBinding
import com.example.offersatyoursteps.models.OfferProductDetails

class OfferAdapter(val context:Context, val offerDetails : MutableList<OfferProductDetails>):RecyclerView.Adapter<OfferAdapter.ViewHolder>() {
    
    inner class ViewHolder(val binding : OfferListViewBinding):RecyclerView.ViewHolder(binding.root){
        val offerImage = binding.offerProductImg
        val prodName =  binding.offerProductName
        val actPrice = binding.offerActualPrice
        val discPrice = binding.offerDiscountPrice
        val discPercentage = binding.offerSavePercentage
        
        fun bindingOffers(context : Context, offerDetails : OfferProductDetails){
            val resourceId = context.resources.getIdentifier(offerDetails.imgName, "drawable", context.packageName)
            Log.d("DEBUG", "image name from offer adapter")
            Log.d("DEBUG", offerDetails.imgName)
            offerImage.setImageResource(resourceId)
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

