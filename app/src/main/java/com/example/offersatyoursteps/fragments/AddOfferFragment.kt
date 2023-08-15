package com.example.offersatyoursteps.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.databinding.FragmentAddOfferBinding
import com.example.offersatyoursteps.models.ProductSubcategory
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.GALLERY_REQUEST_CODE
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class AddOfferFragment : Fragment() {
    // TODO: Rename and change types of parameters
    
    private lateinit var binding : FragmentAddOfferBinding
    private var userModel = UserModel("", "", "", "", "", "", "")
    
    private lateinit var productImage : ImageView
    private lateinit var productName : EditText
    private lateinit var productBrand : EditText
    private lateinit var productCategory : EditText
    private lateinit var productSubcategory : EditText
    private lateinit var productActualPrice : EditText
    private lateinit var productDiscoutPrice : EditText
    private lateinit var offerStartDate : EditText
    private lateinit var offerEndDate : EditText
    private lateinit var productWeight : EditText
    private lateinit var productDesc : EditText
    private lateinit var addProductBtn : Button
    private lateinit var cancelProductBtn : Button
    private lateinit var progressBar : ProgressBar
    
    private var storageRef = Firebase.storage
    private lateinit var uri : Uri
    
    private lateinit var imagePickerLauncher : ActivityResultLauncher<Intent>
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userModel = it?.getParcelable<UserModel>(USER_INFO)!!
        }
    }
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentAddOfferBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        productImage = binding.addProductImage
        productName = binding.addProductTitleTxt
        productBrand = binding.addProductBrandTxt
        productCategory = binding.addProductCategoryTxt
        productSubcategory = binding.addProductSubCategoryTxt
        productActualPrice = binding.addProductActualPriceTxt
        productDiscoutPrice = binding.addProductDiscountPriceTxt
        offerStartDate = binding.addDiscountStartDateTxt
        offerEndDate = binding.addDiscountEndDateTxt
        productWeight = binding.addProductWeighTxt
        productDesc = binding.addProductDescriptionTxt
        addProductBtn = binding.addProductBtn
        cancelProductBtn = binding.cancelProductBtn
        progressBar = binding.addProductProgBar
        
        progressBar.visibility = View.INVISIBLE
        productImage.tag = ""
        
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        { result : ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data : Intent? = result.data
                uri = data?.data!!
                productImage.setImageURI(uri)
                productImage.tag = "image_added"
            } else{
                productImage.tag = ""
            }
        }
        
        productImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(intent)
        }
        
        addProductBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            addProductBtn.visibility = View.INVISIBLE
            cancelProductBtn.visibility = View.INVISIBLE
            
            val prodImage = productImage.tag.toString()
            val prodName = productName.text.toString()
            val prodBrand = productBrand.text.toString()
            val prodCategory = productCategory.text.toString()
            val prodSubcategory = productSubcategory.text.toString()
            val prodActualPrice = productActualPrice.text.toString()
            val prodDiscoutPrice = productDiscoutPrice.text.toString()
            val ofrStartDate = offerStartDate.text.toString()
            val ofrEndDate = offerEndDate.text.toString()
            val prodWeight = productWeight.text.toString()
            val prodDesc = productDesc.text.toString()
            
            Log.d("DEBUG", "AddOfferFragment")
            Log.d("DEBUG", productImage.tag.toString())
            
            if (prodImage == "image_added" && prodName.isNotEmpty() && prodBrand.isNotEmpty() && prodCategory.isNotEmpty()
                && prodSubcategory.isNotEmpty() && prodActualPrice.isNotEmpty() && prodDiscoutPrice.isNotEmpty()
                && ofrStartDate.isNotEmpty() && ofrEndDate.isNotEmpty() && prodWeight.isNotEmpty()
                && prodDesc.isNotEmpty()
            ) {
    
                val prodMap = HashMap<String, String>()
                
                prodMap["Product_Name"] = prodName
                prodMap["Product_Brand"] = prodBrand
                prodMap["Product_Category"] = prodCategory
                prodMap["Product_Subcategory"] = prodSubcategory
                prodMap["Product_ActualPrice"] = prodActualPrice
                prodMap["Product_DiscountPrice"] = prodDiscoutPrice
                prodMap["Offer_StartDate"] = ofrStartDate
                prodMap["offer_EndDate"] = ofrEndDate
                prodMap["Product_Weight"] = prodWeight
                prodMap["Product_Desc"] = prodDesc
                prodMap["Location"] = userModel.cCity.toString()
                prodMap["Shop_Name"] = userModel.cShopName.toString()
                
                storageRef.getReference("productImages")
                    .child(System.currentTimeMillis().toString())
                    .putFile(uri)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {
                                Log.d("DEBUG", "Image upload")
                                prodMap["Product_Image"] = it.toString()
                                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                                DatabaseServices.createProductDetailsRecord("Product_Details",userId,prodMap){
                                    isProdCreateComplete ->
                                    if(isProdCreateComplete){
                                        Toast.makeText(activity, "Offer Product added successfully", Toast.LENGTH_LONG).show()
                                        progressBar.visibility = View.INVISIBLE
                                        addProductBtn.visibility = View.VISIBLE
                                        cancelProductBtn.visibility = View.VISIBLE
                                        clearProducts()
                                    
                                    } else {
                                        Toast.makeText(activity, "Unable to add product now, please try again", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        
                    }
                
            } else {
                progressBar.visibility = View.INVISIBLE
                addProductBtn.visibility = View.VISIBLE
                cancelProductBtn.visibility = View.VISIBLE
                Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            
        }
        
        cancelProductBtn.setOnClickListener {
            clearProducts()
        }
        
    }
    
    private fun clearProducts(){
        productImage.tag = ""
        productImage.setImageResource(R.drawable.baseline_image_search_24)
        productName.text.clear()
        productBrand.text.clear()
        productCategory.text.clear()
        productSubcategory.text.clear()
        productActualPrice.text.clear()
        productDiscoutPrice.text.clear()
        offerStartDate.text.clear()
        offerEndDate.text.clear()
        productWeight.text.clear()
        productDesc.text.clear()
    }
    
    companion object {
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            AddOfferFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
}