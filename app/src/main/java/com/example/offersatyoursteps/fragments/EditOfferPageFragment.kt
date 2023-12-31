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
import android.widget.CheckBox
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
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
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
    private lateinit var noProductImage : CheckBox
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
    
    private var isImageAvail : Boolean = false
    
    private lateinit var mAuth : FirebaseAuth
    
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
        
        mAuth = FirebaseAuth.getInstance()
        
        return binding.root
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        productImage = binding.editProductImage
        noProductImage = binding.editNoProductImage
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
                isImageAvail = true
                
            } else {
                productImage.tag = ""
                if (!offerProductDetails.isImageAvailable) {
                    noProductImage.isChecked = true
                }
            }
        }
        
        productImage.setOnClickListener {
            if (!noProductImage.isChecked) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "image/*"
                }
                imagePickerLauncher.launch(intent)
            } else {
                Toast.makeText(activity, "Please deselect the no product image", Toast.LENGTH_LONG)
                    .show()
            }
        }
        
        noProductImage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val resourceId = resources.getIdentifier(
                    "no_image_availablev1",
                    "drawable",
                    requireActivity().packageName
                )
                binding.editProductImage.setImageResource(resourceId)
            } else {
                if (offerProductDetails.isImageAvailable) {
                    Glide.with(this)
                        .load(offerProductDetails.productImgName)
                        .into(productImage)
                    isImageAvail = true
                    
                } else {
                    val resourceId = resources.getIdentifier(
                        "no_image_availablev1",
                        "drawable",
                        requireActivity().packageName
                    )
                    binding.editProductImage.setImageResource(resourceId)
                    println("no image set")
                    isImageAvail = false
                }
            }
            
        }
    
        productDiscountPrice.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (productActualPrice.text.toString().isNotEmpty()) {
                    if (productDiscountPrice.text.toString().isNotEmpty()) {
                        val actualPrice = productActualPrice.text.toString().toFloat()
                        val discountPrice = productDiscountPrice.text.toString().toFloat()
                    
                        if (discountPrice <= actualPrice) {
                        
                            productDiscountPercentage.text = calculatePercentate(
                                actualPrice,
                                discountPrice
                            )
                        } else {
                            productDiscountPrice.text.clear()
                            Toast.makeText(
                                activity,
                                "Discount price should be less than or equal to actual price",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    
                    } else {
                        productDiscountPrice.setSelection(productDiscountPrice.text.length)
                        Toast.makeText(
                            activity,
                            "Please update the discount price",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        
        offerStartDate.setOnClickListener {
            val yesterday = Calendar.getInstance().timeInMillis - 86400000
            val builder = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.from(yesterday)).build()
                )
            val datePicker = builder.build()
            
            datePicker.addOnPositiveButtonClickListener {
                val selectedDate = Calendar.getInstance()
                selectedDate.timeInMillis = it
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                offerStartDate.setText(formattedDate)
            }
            datePicker.show(requireActivity().supportFragmentManager, "date-picker")
            
        }
        
        offerEndDate.setOnClickListener {
            val yesterday = Calendar.getInstance().timeInMillis - 86400000
            val builder = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.from(yesterday)).build()
                )
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
            enableSpinner(true)
            
            val prodImage = productImage.tag.toString()
            val noProdImage = isImageAvail
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
                
                val prodMap = HashMap<String, Any>()
                prodMap["docId"] = offerProductDetails.docId
                prodMap["isImageAvailable"] = noProdImage
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
                
                val userId = mAuth.currentUser!!.uid
                
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
                                        userId,
                                        prodMap
                                    ) { isProdUpdateComplete ->
                                        if (isProdUpdateComplete) {
                                            Toast.makeText(
                                                activity,
                                                "Offer Product updated successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            enableSpinner(false)
                                            requireActivity().supportFragmentManager.popBackStack(
                                                "EditOffer",
                                                0
                                            )
                                        } else {
                                            Toast.makeText(
                                                activity,
                                                "Unable to update product now, please try again later",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            enableSpinner(false)
                                        }
                                    }
                                }
                        }
                } else if (noProductImage.isChecked) {
                    prodMap["productImgName"] = ""
                    DatabaseServices.updateProductDetailsRecord(
                        userId,
                        prodMap
                    ) { isProdUpdateComplete ->
                        if (isProdUpdateComplete) {
                            Toast.makeText(
                                activity,
                                "Offer Product updated successfully",
                                Toast.LENGTH_LONG
                            ).show()
//                            enableSpinner(false)
//                            requireActivity().supportFragmentManager.popBackStack("EditOffer", 0)
                        } else {
                            Toast.makeText(
                                activity,
                                "Unable to update product now, please try again later",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        enableSpinner(false)
                        requireActivity().supportFragmentManager.popBackStack("EditOffer", 0)
                    }
                } else if (!noProductImage.isChecked && offerProductDetails.productImgName != "") {
                    prodMap["productImgName"] = offerProductDetails.productImgName
                    prodMap["isImageAvailable"] = offerProductDetails.isImageAvailable
                    DatabaseServices.updateProductDetailsRecord(
                        userId,
                        prodMap
                    ) { isProdUpdateComplete ->
                        if (isProdUpdateComplete) {
                            Toast.makeText(
                                activity,
                                "Offer Product updated successfully",
                                Toast.LENGTH_LONG
                            ).show()
//                            enableSpinner(false)
                            requireActivity().supportFragmentManager.popBackStack("EditOffer", 0)
                        } else {
                            Toast.makeText(
                                activity,
                                "Unable to update product now, please try again later",
                                Toast.LENGTH_LONG
                            ).show()
//                            enableSpinner(false)
                        
                        }
                        enableSpinner(false)
                    }
                } else {
                    enableSpinner(false)
                    Toast.makeText(activity, "Please update the product image", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                enableSpinner(false)
                Toast.makeText(activity, "Please update the missing fields", Toast.LENGTH_LONG)
                    .show()
            }
            
        }
        
        deleteProductBtn.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            DatabaseServices.deleteProductDetails(
                userId,
                offerProductDetails.docId.toString(),
                offerProductDetails.productName
            ) { isDeleteSuccess ->
                if (isDeleteSuccess) {
                    if(offerProductDetails.isImageAvailable){
                        DatabaseServices
                            .deleteProductImage(userId,offerProductDetails.productImgName){isImgDeleted ->
                            if(isImgDeleted){
                                Toast.makeText(
                                    activity,
                                    "Product deleted successfully",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                requireActivity().supportFragmentManager.popBackStack("EditOffer", 0)
                            }
                        }
                    }else{
                        Toast.makeText(
                            activity,
                            "Product deleted successfully",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        requireActivity().supportFragmentManager.popBackStack("EditOffer", 0)
                    }
                } else {
                    Toast.makeText(
                        activity,
                        "unable to delete the product now, please try later",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    requireActivity().supportFragmentManager.popBackStack("EditOffer", 0)
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
        productImage.setImageResource(R.drawable.upload_image)
        noProductImage.isChecked = false
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
        
        println("updateEditPageUI")
        println(offerProductDetails.isImageAvailable)
        if (offerProductDetails.isImageAvailable) {
            Glide.with(this)
                .load(offerProductDetails.productImgName)
                .into(productImage)
        } else {
            val resourceId = resources.getIdentifier(
                "no_image_availablev1",
                "drawable",
                requireActivity().packageName
            )
            binding.editProductImage.setImageResource(resourceId)
            println("no image set")
        }
        
        noProductImage.isChecked = !offerProductDetails.isImageAvailable
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
    
   
    private fun calculatePercentate(actPrice : Float, discPric : Float) : String {

        val discPercentage : Int =
            (((actPrice - discPric) / actPrice) * 100).roundToInt()
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
    
    private fun enableSpinner(enabled : Boolean) {
        if (enabled) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
        
        deleteProductBtn.isEnabled = !enabled
        updateProductBtn.isEnabled = !enabled
    }
}