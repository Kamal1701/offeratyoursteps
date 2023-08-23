package com.example.offersatyoursteps.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.offersatyoursteps.databinding.ProductEditListViewBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.utilities.RUPEE_SYMBOL

class EditOfferAdapter(val context : Context,
                       val offerDetails : MutableList<OfferProductDetails>,
                       val prodItemClick : (OfferProductDetails) -> Unit):
    RecyclerView.Adapter<EditOfferAdapter.ViewHolder>() {
    
    inner class ViewHolder(val binding : ProductEditListViewBinding, val prodItemClick : (OfferProductDetails) -> Unit):RecyclerView.ViewHolder(binding.root){
        var editImage = binding.editProductImage
        var editProdName = binding.editProductName
        var editBrandName = binding.editBrandName
        var editActualPrice = binding.editActualPrice
        var editDiscPrice = binding.editDiscountPrice
        var editOfferStartDt = binding.editOfferStartDate
        var editOfferEndDt = binding.editOfferEndDate
        fun bindingOffers(context : Context, offerDetails : OfferProductDetails) {
            Glide.with(context).load(offerDetails.productImgName).into(editImage)
            editProdName.text = offerDetails.productName
            editBrandName.text = offerDetails.productBrandName
            "$RUPEE_SYMBOL ${offerDetails.productActualPrice}".also { editActualPrice.text =  it }
            editActualPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            "$RUPEE_SYMBOL ${offerDetails.productDiscountPrice}".also { editDiscPrice.text =  it }
//            editActualPrice.text = offerDetails.actualPrice
//            editDiscPrice.text = offerDetails.discountPrice
            editOfferStartDt.text = offerDetails.productOfferStDate
            editOfferEndDt.text = offerDetails.productOfferEdDate
    
            editImage.setOnClickListener { prodItemClick(offerDetails) }
            editBrandName.setOnClickListener { prodItemClick(offerDetails) }
            editActualPrice.setOnClickListener { prodItemClick(offerDetails) }
            editDiscPrice.setOnClickListener { prodItemClick(offerDetails) }
            editOfferStartDt.setOnClickListener { prodItemClick(offerDetails) }
            editOfferEndDt.setOnClickListener { prodItemClick(offerDetails) }
        }
        
    }
    override fun onCreateViewHolder(
        parent : ViewGroup,
        viewType : Int
    ) : EditOfferAdapter.ViewHolder {
        val binding = ProductEditListViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding, prodItemClick)
    }
    
    override fun onBindViewHolder(holder : EditOfferAdapter.ViewHolder, position : Int) {
        holder.bindingOffers(context, offerDetails[position])
    }
    
    override fun getItemCount() : Int {
        return offerDetails.size
    }
}