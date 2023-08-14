package com.example.offersatyoursteps.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.offersatyoursteps.databinding.SubcategoryListViewBinding
import com.example.offersatyoursteps.models.OfferDetails
import com.example.offersatyoursteps.models.ProductSubcategory

class ProductSubcategoryAdapter(val context : Context, val subcategoryList : List<ProductSubcategory>,val itemClick : (ProductSubcategory)->Unit) :
    RecyclerView.Adapter<ProductSubcategoryAdapter.SubcategoryViewHolder>() {
    
    inner class SubcategoryViewHolder(val binding : SubcategoryListViewBinding,val itemClick : (ProductSubcategory)->Unit) :
        RecyclerView.ViewHolder(binding.root) {
        var productImage = binding.productSubcategoryImage
        var productSubcat = binding.productSubCategoryTxt
        var productOfferDesc = binding.offerDescriptionTxt
        
        fun bindingViewHolder(context : Context, subcategory : ProductSubcategory) {
            val resourceId = context.resources.getIdentifier(
                subcategory.subcategoryImg,
                "drawable",
                context.packageName
            )
            productImage.setImageResource(resourceId)
            productSubcat.text = subcategory.title
            productOfferDesc.text = subcategory.discountDesc
    
            binding.productSubcategoryImage.setOnClickListener {
                itemClick(subcategory)
            }
//
//            binding.productSubCategoryTxt.setOnClickListener {
//                itemClick(offerDetails)
//            }
//
//            binding.offerDescriptionTxt.setOnClickListener {
//                itemClick(offerDetails)
//            }
            
        }
        
    }
    
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : SubcategoryViewHolder {
        val binding =
            SubcategoryListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubcategoryViewHolder(binding, itemClick)
    }
    
    override fun getItemCount() : Int {
        return subcategoryList.size
    }
    
    override fun onBindViewHolder(holder : SubcategoryViewHolder, position : Int) {
        holder?.bindingViewHolder(context, subcategoryList[position])
    }
}