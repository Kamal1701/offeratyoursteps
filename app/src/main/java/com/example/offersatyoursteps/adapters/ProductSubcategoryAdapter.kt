package com.example.offersatyoursteps.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.offersatyoursteps.databinding.SubcategoryListViewBinding
import com.example.offersatyoursteps.models.OfferDetails

class ProductSubcategoryAdapter(val context : Context, val subcategoryList : ArrayList<OfferDetails>) :
    RecyclerView.Adapter<ProductSubcategoryAdapter.SubcategoryViewHolder>() {
    
    inner class SubcategoryViewHolder(binding : SubcategoryListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var productImage = binding.productSubcategoryImage
        val productSubcat = binding.productSubCategoryTxt
        val productOfferDesc = binding.offerDescriptionTxt
        
        fun bindingViewHolder(context : Context, offerDetails : OfferDetails) {
            val resourceId = context.resources.getIdentifier(
                offerDetails.imgName,
                "drawable",
                context.packageName
            )
            productImage.setImageResource(resourceId)
            productSubcat.text = offerDetails.productName
            productOfferDesc.text = "Offers"
            
        }
        
    }
    
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : SubcategoryViewHolder {
        val binding =
            SubcategoryListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubcategoryViewHolder(binding)
    }
    
    override fun getItemCount() : Int {
        return subcategoryList.size
    }
    
    override fun onBindViewHolder(holder : SubcategoryViewHolder, position : Int) {
        holder?.bindingViewHolder(context, subcategoryList[position])
    }
}