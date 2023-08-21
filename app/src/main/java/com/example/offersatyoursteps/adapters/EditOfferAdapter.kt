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
                       val offerDetails : MutableList<OfferProductDetails>):
    RecyclerView.Adapter<EditOfferAdapter.ViewHolder>() {
    
    inner class ViewHolder(val binding : ProductEditListViewBinding):RecyclerView.ViewHolder(binding.root){
        var editImage = binding.editProductImage
        var editProdName = binding.editProductName
        var editBrandName = binding.editBrandName
        var editActualPrice = binding.editActualPrice
        var editDiscPrice = binding.editDiscountPrice
        var editOfferStartDt = binding.editOfferStartDate
        var editOfferEndDt = binding.editOfferEndDate
        fun bindingOffers(context : Context, offerDetails : OfferProductDetails) {
            Glide.with(context).load(offerDetails.imgName).into(editImage)
            editProdName.text = offerDetails.productName
            editBrandName.text = offerDetails.brandName
            "$RUPEE_SYMBOL ${offerDetails.actualPrice}".also { editActualPrice.text =  it }
            editActualPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            "$RUPEE_SYMBOL ${offerDetails.discountPrice}".also { editDiscPrice.text =  it }
//            editActualPrice.text = offerDetails.actualPrice
//            editDiscPrice.text = offerDetails.discountPrice
            editOfferStartDt.text = offerDetails.offerStDate
            editOfferEndDt.text = offerDetails.offerEdDate
        }
        
    }
    override fun onCreateViewHolder(
        parent : ViewGroup,
        viewType : Int
    ) : EditOfferAdapter.ViewHolder {
        val binding = ProductEditListViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder : EditOfferAdapter.ViewHolder, position : Int) {
        holder.bindingOffers(context, offerDetails[position])
    }
    
    override fun getItemCount() : Int {
        return offerDetails.size
    }
}