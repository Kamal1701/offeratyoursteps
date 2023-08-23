package com.example.offersatyoursteps.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.FragmentEditOfferPageBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.EDIT_PRODUCT_TITLE
import com.example.offersatyoursteps.utilities.EXTRA_PRODUCT
import com.example.offersatyoursteps.utilities.FIREBASE_IMAGE_LOCATION
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_TABLE
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


class EditOfferPageFragment : Fragment() {
    
    private lateinit var offerProductDetails : OfferProductDetails
    private lateinit var userModel : UserModel
    private lateinit var binding : FragmentEditOfferPageBinding
    private lateinit var backPressedCallback : OnBackPressedCallback
    
    private lateinit var productImage : ImageView
    private lateinit var productName : EditText
    private lateinit var productBrand : EditText
    private lateinit var productCategory : EditText
    private lateinit var productSubcategory : EditText
    private lateinit var productActualPrice : EditText
    private lateinit var productDiscountPrice : EditText
    private lateinit var productDiscountPercentage : TextView
    private lateinit var offerStartDate : EditText
    private lateinit var offerEndDate : EditText
    private lateinit var productWeight : EditText
    private lateinit var productDesc : EditText
    private lateinit var updateProductBtn : Button
    private lateinit var deleteProductBtn : Button
    private lateinit var progressBar : ProgressBar
    
    private lateinit var uri : Uri
    private var storageRef = Firebase.storage
    
    private lateinit var imagePickerLauncher : ActivityResultLauncher<Intent>
    
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
        binding = FragmentEditOfferPageBinding.inflate(inflater, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = EDIT_PRODUCT_TITLE
        return binding.root
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        productImage = binding.editProductImage
        productName = binding.editProductTitleTxt
        productBrand = binding.editProductBrandTxt
        productCategory = binding.editProductCategoryTxt
        productSubcategory = binding.editProductSubCategoryTxt
        productActualPrice = binding.editProductActualPriceTxt
        productDiscountPrice = binding.editProductDiscountPriceTxt
        offerStartDate = binding.editDiscountStartDateTxt
        offerEndDate = binding.editDiscountEndDateTxt
        productWeight = binding.editProductWeighTxt
        productDiscountPercentage = binding.editDiscountPercTxt
        productDesc = binding.editProductDescriptionTxt
        updateProductBtn = binding.updateProductBtn
        deleteProductBtn = binding.deleteProductBtn
        progressBar = binding.editProductProgBar
        
        progressBar.visibility = View.INVISIBLE
        productImage.tag = ""
        
        updateEditPageUI()
        
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        { result : ActivityResult ->
            println(result.resultCode)
            println(Activity.RESULT_OK)
            if (result.resultCode == Activity.RESULT_OK) {
                val data : Intent? = result.data
                uri = data?.data!!
                productImage.setImageURI(uri)
                productImage.tag = "image_changed"
                println(uri)
                println(productImage.tag)
                
                
                
            } else {
                productImage.tag = ""
            }
        }
        
        productImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(intent)
        }
        
//        productDiscountPrice.setOnFocusChangeListener { _, hasFocus ->
//            if(!hasFocus){
//                if (productActualPrice.text.toString()
//                        .isNotEmpty() && productActualPrice.text.toString().isNotEmpty()
//                ) {
//                    productDiscountPercentage.text = calculatePercentate(
//                        productActualPrice.text.toString(),
//                        productDiscountPrice.text.toString()
//                    )
//                } else{
//                    Toast.makeText(activity, "please fill actual price and discount price", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
        
        
        offerStartDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val datePicker = builder.build()
            
            datePicker.addOnPositiveButtonClickListener {
                val selectedDate = Calendar.getInstance()
                selectedDate.timeInMillis = it
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                offerStartDate.setText(formattedDate)
            }
            datePicker.show(requireActivity().supportFragmentManager, "date-picker")
            
            if (productActualPrice.text.toString()
                    .isNotEmpty() && productActualPrice.text.toString().isNotEmpty()
            ) {
                productDiscountPercentage.text = calculatePercentate(
                    productActualPrice.text.toString(),
                    productDiscountPrice.text.toString()
                )
            }
        }
        
        offerEndDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val datePicker = builder.build()
            
            datePicker.addOnPositiveButtonClickListener {
                val selectedDate = Calendar.getInstance()
                selectedDate.timeInMillis = it
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                offerEndDate.setText(formattedDate)
            }
            datePicker.show(requireActivity().supportFragmentManager, "date-picker")
        }
        
        
        updateProductBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            updateProductBtn.visibility = View.INVISIBLE
            deleteProductBtn.visibility = View.INVISIBLE
            
            val prodImage = productImage.tag.toString()
            val prodName = productName.text.toString()
            val prodBrand = productBrand.text.toString()
            val prodCategory = productCategory.text.toString()
            val prodSubcategory = productSubcategory.text.toString()
            val prodActualPrice = productActualPrice.text.toString()
            val prodDiscountPrice = productDiscountPrice.text.toString()
            val prodDiscountPerc = productDiscountPercentage.text.toString()
            val ofrStartDate = offerStartDate.text.toString()
            val ofrEndDate = offerEndDate.text.toString()
            val prodWeight = productWeight.text.toString()
            val prodDesc = productDesc.text.toString()
            
            println("EditOfferPageFragment")
            println(prodImage)
            
            
            if (prodName.isNotEmpty() && prodBrand.isNotEmpty() && prodCategory.isNotEmpty()
                && prodSubcategory.isNotEmpty() && prodActualPrice.isNotEmpty() && prodDiscountPrice.isNotEmpty()
                && ofrStartDate.isNotEmpty() && ofrEndDate.isNotEmpty() && prodWeight.isNotEmpty()
                && prodDesc.isNotEmpty()
            ) {
                
                val prodMap = HashMap<String, String>()
                prodMap["docId"] = offerProductDetails.docId
                prodMap["productName"] = prodName
                prodMap["productBrandName"] = prodBrand
                prodMap["productCategory"] = prodCategory
                prodMap["productSubcategory"] = prodSubcategory
                prodMap["productActualPrice"] = prodActualPrice
                prodMap["productDiscountPrice"] = prodDiscountPrice
                prodMap["productDiscountPercentage"] = prodDiscountPerc
                prodMap["productOfferStDate"] = ofrStartDate
                prodMap["productOfferEdDate"] = ofrEndDate
                prodMap["productWeight"] = prodWeight
                prodMap["productDesc"] = prodDesc
    
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                
                if (prodImage == "image_changed") {
                    storageRef.getReference(FIREBASE_IMAGE_LOCATION)
                        .child(System.currentTimeMillis().toString())
                        .putFile(uri)
                        .addOnSuccessListener { task ->
                            task.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener {
                                    println("Image upload")
                                    println(it.toString())
                                    prodMap["productImgName"] = it.toString()
    
                                    DatabaseServices.updateProductDetailsRecord(
                                        PRODUCT_INFO_TABLE,
                                        userId,
                                        prodMap
                                    ) { isProdUpdateComplete ->
                                        if (isProdUpdateComplete) {
                                            Toast.makeText(
                                                activity,
                                                "Offer Product updated successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            progressBar.visibility = View.INVISIBLE
                                            updateProductBtn.visibility = View.VISIBLE
                                            deleteProductBtn.visibility = View.VISIBLE
                                        } else {
                                            Toast.makeText(
                                                activity,
                                                "Unable to update product now, please try again later",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            progressBar.visibility = View.INVISIBLE
                                            updateProductBtn.visibility = View.VISIBLE
                                            deleteProductBtn.visibility = View.VISIBLE
                                        }
                                    }
                                }
                        }
                }
                else {
//                    prodMap["Product_Image"] = offerProductDetails.imgName
                    DatabaseServices.updateProductDetailsRecord(
                        PRODUCT_INFO_TABLE,
                        userId,
                        prodMap
                    ) { isProdUpdateComplete ->
                        if (isProdUpdateComplete) {
                            Toast.makeText(
                                activity,
                                "Offer Product updated successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.visibility = View.INVISIBLE
                            updateProductBtn.visibility = View.VISIBLE
                            deleteProductBtn.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(
                                activity,
                                "Unable to update product now, please try again later",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.visibility = View.INVISIBLE
                            updateProductBtn.visibility = View.VISIBLE
                            deleteProductBtn.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                progressBar.visibility = View.INVISIBLE
                updateProductBtn.visibility = View.VISIBLE
                deleteProductBtn.visibility = View.VISIBLE
                Toast.makeText(activity, "Please update the missing fields", Toast.LENGTH_LONG)
                    .show()
            }
            
        }
        
        deleteProductBtn.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            DatabaseServices.deleteProductDetails(userId, offerProductDetails.docId.toString()){
                isDeleteSuccess ->
                if (isDeleteSuccess) {
                    clearProducts()
                } else{
                    Toast.makeText(activity, "unable to delete the product now", Toast.LENGTH_LONG)
                        .show()
                }
            }
            
        }
        
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack("EditOffer", 0)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        
    }
    
    private fun clearProducts() {
        productImage.tag = ""
        productImage.setImageResource(R.drawable.baseline_image_search_24)
        productName.text.clear()
        productBrand.text.clear()
        productCategory.text.clear()
        productSubcategory.text.clear()
        productActualPrice.text.clear()
        productDiscountPrice.text.clear()
        productDiscountPercentage.text = ""
        offerStartDate.text.clear()
        offerEndDate.text.clear()
        productWeight.text.clear()
        productDesc.text.clear()
    }
    
    fun updateEditPageUI() {
        Glide.with(this)
            .load(offerProductDetails.productImgName)
            .into(productImage)
        
        productName.setText(offerProductDetails.productName)
        productBrand.setText(offerProductDetails.productBrandName)
        productCategory.setText(offerProductDetails.productCategory)
        productSubcategory.setText(offerProductDetails.productSubcategory)
        productActualPrice.setText(offerProductDetails.productActualPrice)
        productDiscountPrice.setText(offerProductDetails.productDiscountPrice)
        offerStartDate.setText(offerProductDetails.productOfferStDate)
        offerEndDate.setText(offerProductDetails.productOfferEdDate)
        productWeight.setText(offerProductDetails.productWeight)
        productDiscountPercentage.text = offerProductDetails.productDiscountPercentage
        productDesc.setText(offerProductDetails.productDesc)
        
    }
    
    fun calculatePercentate(actPrice : String, discPric : String) : String {
        val actualPrice : Float = actPrice.toFloat()
        val discountPrice : Float = discPric.toFloat()
        val discPercentage : Int =
            (((actualPrice - discountPrice) / actualPrice) * 100).roundToInt()
        return "${discPercentage}%"
        
    }
    
    companion object {
        
        @JvmStatic
        fun newInstance(userModel : UserModel, offerProductDetails : OfferProductDetails) =
            EditOfferPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                    putParcelable(EXTRA_PRODUCT, offerProductDetails)
                }
            }
    }
}