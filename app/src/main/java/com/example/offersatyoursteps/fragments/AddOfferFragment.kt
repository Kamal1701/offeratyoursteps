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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.offersatyoursteps.databinding.FragmentAddOfferBinding
import com.example.offersatyoursteps.models.UserModel
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
    private var userModel = UserModel("", "", "", "", "", "")
    
    private lateinit var productImage : ImageView
    private lateinit var addProductBtn : Button
    
    private var storageRef = Firebase.storage
    private lateinit var uri : Uri
    
//    private lateinit var imagePickerLauncher : ActivityResultLauncher<String>
    
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
        addProductBtn = binding.addProductBtn

//        val galleryImage = registerForActivityResult(
//            ActivityResultContracts.GetContent(),
//            ActivityResultCallback {
//                productImage.setImageURI(it)
//                uri = it!!
//            }
//        )
//
//        productImage.setOnClickListener {
//            galleryImage.launch("image/*")
//            Log.d("DEBUG", "Image from gallery")
//            Log.d("DEBUG", uri.toString())
//        }
        
        
//        imagePickerLauncher = registerForActivityResult(
//            ActivityResultContracts.GetContent(),
//            {
//                result: ActivityResult ->
//                if(result.resultCode == RESULT_OK){
//                    uri = result.data?.data!!
//                }
//            }
//        )
        
        productImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    
//        productImage.setOnClickListener {
//            imagePickerLauncher.launch("image/*")
//        }
        
        addProductBtn.setOnClickListener {
            storageRef.getReference("productImages").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            Log.d("DEBUG", "Image upload")
                            
                            val userId = FirebaseAuth.getInstance().currentUser!!.uid
                            Log.d("DEBUG", "Image upload to firestore")
                            val mapImage = mapOf(
                                "url" to it.toString()
                            )
                            val dbRef = FirebaseFirestore.getInstance().collection("productImage")
                            dbRef.document(userId).set(mapImage)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        activity,
                                        "Image uploaded successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener { error ->
                                    Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG)
                                        .show()
                                }
                        }
                    
                }
            
        }
        
    }
    
    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            uri = data?.data!!
            productImage.setImageURI(uri)
            Log.d("DEBUG", "Image from gallery")
            Log.d("DEBUG", uri.toString())
        }
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